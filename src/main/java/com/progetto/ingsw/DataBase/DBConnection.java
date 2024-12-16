package com.progetto.ingsw.DataBase;

import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.Model.User;
import com.progetto.ingsw.View.SceneHandler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class DBConnection {
    private static DBConnection instance = null;
    private Connection con = null;
    private ArrayList<Barca> categoryBarche;
    private ArrayList<Barca> similarBarche;
    private ArrayList<Barca> searchedBarche = new ArrayList<>();
    private Label resultLabel;
    private Barca barca;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private DBConnection() {}

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public void createConnection() throws SQLException {
        String url = "jdbc:sqlite:database.db";
        con = DriverManager.getConnection(url);
        if (con != null && !con.isClosed())
            System.out.println("Connected!");
    }

    public void closeConnection() throws SQLException {
        if (con != null)
            con.close();
        con = null;
    }

    public void close() {
        executorService.shutdownNow();
    }

    public void insertUsers(String name, String surname, String email, String password, Boolean isAdmin) {
        executorService.submit(createDaemonThread(() -> {
            try {
                if (con == null || con.isClosed())
                    return;
                PreparedStatement stmt = con.prepareStatement("INSERT INTO utenti VALUES(?, ?, ?, ?, ?);");
                stmt.setString(1, name);
                stmt.setString(2, surname);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setBoolean(5, isAdmin);
                stmt.execute();
                stmt.close();
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public Future<?> checkLogin(String email, String password) throws SQLException {
        return executorService.submit(createDaemonThread(() -> {
            try {
                if (con == null || con.isClosed())
                    return;
                String query = "select * from utenti where email=?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    boolean check = BCrypt.checkpw(password, rs.getString("password"));
                    if (check)
                        System.out.println("Password OK");
                    else
                        throw new SQLException();
                } else {
                    throw new SQLException();
                }
                stmt.close();
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public CompletableFuture<User> setUser(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                User user;
                if (con == null || con.isClosed())
                    future.complete(null);
                String query = "select * from utenti where email=?;";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    user = new User(rs.getString("email"), rs.getString("nome"), rs.getString("cognome"), rs.getBoolean("isAdmin"));
                    future.complete(user);
                }
                stmt.close();
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }));
        return future;
    }


    public String encryptedPassword(String password) {
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;
    }

    public CompletableFuture<ArrayList<Barca>> addHomePageBarche() {
        CompletableFuture<ArrayList<Barca>> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                ArrayList<Barca> barche = new ArrayList<>();
                if (this.con != null && !this.con.isClosed()) {
                    String query = "select * from barche;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Barca b = new Barca(rs.getString("id"), rs.getString("nome"), rs.getString("descrizione"), rs.getString("prezzo"), rs.getString("categoria"));
                        barche.add(b);
                    }
                    future.complete(barche);
                    stmt.close();
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }

    public void addCategoryBarche(String category) {
        executorService.submit(createDaemonThread(() -> {
            try {
                categoryBarche = new ArrayList<>();
                if (this.con != null && !this.con.isClosed()) {
                    String query = "select * from barche where categoria=?;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    stmt.setString(1, category);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Barca b = new Barca(rs.getString("id"), rs.getString("nome"), rs.getString("descrizione"), rs.getString("prezzo"), rs.getString("categoria"));
                        categoryBarche.add(b);
                    }
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public ArrayList<Barca> getCategoryBarche() {
        return categoryBarche;
    }

    public void clearCategoryBarcaList() {
        if (categoryBarche != null) {
            categoryBarche.clear();
        }
    }


    public CompletableFuture<Boolean> checkExistEmail(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                if (this.con != null && !this.con.isClosed()) {
                    String query = "SELECT * from utenti WHERE email =?;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    stmt.setString(1, email);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        future.complete(true);
                    } else {
                        future.complete(false);
                    }
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }

    public CompletableFuture<Barca> getBarca(String id) {
        CompletableFuture<Barca> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                if (this.con != null && !this.con.isClosed()) {
                    String query = "select * from barche where id=?;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    stmt.setString(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        barca = new Barca(rs.getString("id"), rs.getString("nome"), rs.getString("descrizione"), rs.getString("prezzo"), rs.getString("categoria"));
                        future.complete(barca);
                    }
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }


    public void addSimilarBarca(String id, String category) {
        executorService.submit(createDaemonThread(() -> {
            try {
                similarBarche = new ArrayList<>();
                if (this.con != null && !this.con.isClosed()) {
                    String query = "select * from barche where categoria=?;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    stmt.setString(1, category);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next() && similarBarche.size() < 5) {
                        if (!rs.getString("id").equals(id)) {
                            barca = new Barca(rs.getString("id"), rs.getString("nome"), rs.getString("descrizione"), rs.getString("prezzo"), rs.getString("categoria"));
                            similarBarche.add(barca);
                        }
                    }
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public ArrayList<Barca> getSimilarBarche() {
        return similarBarche;
    }

    public CompletableFuture<Label> searchBarca(String searchText) {
        CompletableFuture<Label> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                resultLabel = new Label();
                if (this.con != null && !this.con.isClosed()) {
                    String query = "SELECT * FROM barche WHERE chiavi LIKE ?;";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, "%" + searchText + "%");

                    ResultSet resultSet = stmt.executeQuery();
                    StringBuilder resultBuilder = new StringBuilder();
                    while (resultSet.next()) {
                        String columnValue = resultSet.getString("id");
                        resultBuilder.append(columnValue + ";");
                    }

                    resultLabel.setText(resultBuilder.toString());
                    future.complete(resultLabel);
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }

    public void addSearchedBarche(String[] products) {
        executorService.submit(createDaemonThread(() -> {
            try {
                if (this.con != null && !this.con.isClosed()) {
                    for (String id : products) {
                        String query = "select * from barche where id= ?;";
                        PreparedStatement stmt = this.con.prepareStatement(query);
                        stmt.setString(1, id);

                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            Barca b = new Barca(rs.getString("id"), rs.getString("nome"), rs.getString("descrizione"), rs.getString("prezzo"), rs.getString("categoria"));
                            searchedBarche.add(b);
                        }
                    }
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public ArrayList<Barca> getSearchedBarche() {
        return searchedBarche;
    }

    public void clearSearchedList() {
        if (searchedBarche != null) {
            searchedBarche.clear();
        }
    }

    public CompletableFuture<ArrayList<Barca>> getWishlist(String email) {
        CompletableFuture<ArrayList<Barca>> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                ArrayList<Barca> bar = new ArrayList<>();
                if (con == null || con.isClosed())
                    future.complete(bar);
                String query = "SELECT * from wishlist where id_utente=?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    CompletableFuture<Barca> future1 = getBarca(rs.getString(2));
                    Barca b = future1.get(10, TimeUnit.SECONDS);
                    bar.add(b);
                }
                future.complete(bar);
            } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }


    public void insertWishlistBarcaIntoDB(String email, String id_barca) {
        executorService.submit(createDaemonThread(() -> {
            try {
                CompletableFuture<ArrayList<Barca>> future = getWishlist(email);
                ArrayList<Barca> nBar = future.get(10, TimeUnit.SECONDS);
                boolean find = false;
                for (Barca id : nBar) {
                    if (id.id().equals(id_barca)) {
                        find = true;
                    }
                }
                if (nBar.size() < 6 && !find) {
                    if (con == null || con.isClosed())
                        return;
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO wishlist VALUES(?, ?);");
                    stmt.setString(1, email);
                    stmt.setString(2, id_barca);
                    stmt.execute();
                    stmt.close();
                }

                if (nBar.size() >= 6) {
                    Platform.runLater(() -> SceneHandler.getInstance().showAlert("Attenzione", Message.add_wishlist_max_information, 1));

                }
                if (find) {
                    Platform.runLater(() -> SceneHandler.getInstance().showAlert("Attenzione", Message.add_wishlist_find_information, 1));
                }
            } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public CompletableFuture<ArrayList<Barca>> getPrenotazioni(String email) {
        CompletableFuture<ArrayList<Barca>> future = new CompletableFuture<>();
        executorService.submit(createDaemonThread(() -> {
            try {
                ArrayList<Barca> bar = new ArrayList<>();
                if (con == null || con.isClosed())
                    future.complete(bar);
                String query = "SELECT * from prenotazioni where id_utente=?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    CompletableFuture<Barca> future1 = getBarca(rs.getString(2));
                    Barca b = future1.get(10, TimeUnit.SECONDS);
                    bar.add(b);
                }
                future.complete(bar);
            } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
        return future;
    }

    public void insertPrenotazioneIntoDB(String email, String id_barca, int giorno, int mese, int anno) {
        executorService.submit(createDaemonThread(() -> {
            try {
                CompletableFuture<ArrayList<Barca>> future = getPrenotazioni(email);
                ArrayList<Barca> nBar = future.get(10, TimeUnit.SECONDS);
                boolean find = false;
                for (Barca id : nBar) {
                    if (id.id().equals(id_barca)) {
                        find = true;
                    }
                }
                if (nBar.size() < 6 && !find) {
                    if (con == null || con.isClosed())
                        return;
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO prenotazioni VALUES(?, ?, ?, ?, ?);");
                    stmt.setString(1, email);
                    stmt.setString(2, id_barca);
                    stmt.setInt(3,giorno);
                    stmt.setInt(4,mese);
                    stmt.setInt(5,anno);
                    stmt.execute();
                    stmt.close();
                }

                if (nBar.size() >= 6) {
                    Platform.runLater(() -> SceneHandler.getInstance().showAlert("Attenzione", Message.add_prenotazioni_max_information, 1));

                }
                if (find) {
                    Platform.runLater(() -> SceneHandler.getInstance().showAlert("Attenzione", Message.add_prenotazioni_find_information, 1));
                }
            } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public void removeSelectedWishlistItem(String id, String email) {
        executorService.submit(createDaemonThread(() -> {
            try {
                if (con == null || con.isClosed())
                    return;
                String query = "DELETE FROM wishlist where id_utente=? and id_barca = ?;";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, id);
                stmt.execute();
                stmt.close();
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    public void updatePassword(String email, String password) {
        executorService.submit(createDaemonThread(() -> {
            try {
                if (this.con != null && !this.con.isClosed()) {
                    String query = "update utenti set password = ? where email=?;";
                    PreparedStatement stmt = this.con.prepareStatement(query);
                    stmt.setString(1, password);
                    stmt.setString(2, email);
                    stmt.execute();
                    stmt.close();
                }
            } catch (SQLException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        }));
    }

    private Thread createDaemonThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    }
}

