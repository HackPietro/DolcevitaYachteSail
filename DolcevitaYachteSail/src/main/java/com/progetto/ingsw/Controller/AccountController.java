package com.progetto.ingsw.Controller;

import com.progetto.ingsw.DataBase.Authentication;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.DataBase.Validation;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.User;
import com.progetto.ingsw.View.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AccountController {
    @FXML
    private Text changePasswordText, nameText1, nameText2, sendReportText, surnameText1, surnameText2, sendedText;
    @FXML
    private Button changePasswordButton, aggiungiBarcaButton, inserisciFotoBarca;
    @FXML
    private PasswordField passwordField, repeatPasswordField;
    @FXML
    private ScrollPane scrollPaneAccount;
    @FXML
    private TextArea sendReportArea;
    @FXML
    private ComboBox<String> categoriaBarcaBox;
    @FXML
    private TextArea chiaviBarcaArea, descrizioneBarcaArea;
    @FXML
    private TextField nomeBarcaText, prezzoBarcaText, idBarcaText;
    @FXML
    private VBox aggiungiBarcaVBox;

    private File temporaryImageFile;



    @FXML
    void changePasswordButtonAction(ActionEvent event) {
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        String email = Authentication.getInstance().getUser().email();
        boolean valid = Validation.getInstance().checkPassword(password, repeatPassword);
        String encryptedPassword = DBConnection.getInstance().encryptedPassword(password);
        if (valid) {
            DBConnection.getInstance().updatePassword(email, encryptedPassword);
            passwordField.clear();
            repeatPasswordField.clear();
            SceneHandler.getInstance().showAlert("Cambio password", Message.update_password_success, 1);
        }
    }

    void createInfoThread(){
        Thread thread = new Thread(() -> {
            try {
                sendedText.setVisible(true);
                Thread.sleep(1500);
                sendedText.setVisible(false);
            } catch (InterruptedException e) {
                SceneHandler.getInstance().showAlert("Errore thread", Message.thread_error, 0);
            }
        });
        thread.start();
    }

    private void loadUserData() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        User user = Authentication.getInstance().getUser();
        nameText2.setText(user.name());
        surnameText2.setText(user.surname());
    }

    @FXML
    void initialize() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        loadUserData();

        if (Authentication.getInstance().getUser().isAdmin()) {

            categoriaBarcaBox.getItems().addAll(
                    "Barche a motore",
                    "Barche a vela",
                    "Barche da pesca",
                    "Barche tradizionali",
                    "Barche sportive",
                    "Gommoni",
                    "Catamarani",
                    "Trimarani"
            );

            aggiungiBarcaVBox.setVisible(true);
            aggiungiBarcaVBox.setManaged(true);
        } else {
            aggiungiBarcaVBox.setVisible(false);
            aggiungiBarcaVBox.setManaged(false);
        }
    }

    @FXML
    void inserisciFotoBarcaAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un'immagine");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("File JPG", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(inserisciFotoBarca.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if ((int) image.getWidth() != 400 || (int) image.getHeight() != 400) {
                    SceneHandler.getInstance().showAlert("Errore immagine", "L'immagine deve essere di dimensioni 400x400.", 0);
                    return;
                }

                temporaryImageFile = selectedFile;
                SceneHandler.getInstance().showAlert("Successo", "Immagine caricata correttamente. Sarà salvata al momento dell'aggiunta della barca.", 1);

            } catch (Exception e) {
                SceneHandler.getInstance().showAlert("Errore", "Errore durante il caricamento dell'immagine: " + e.getMessage(), 0);
            }
        } else {
            SceneHandler.getInstance().showAlert("Errore", "Nessun file selezionato.", 0);
        }
    }

    @FXML
    void aggiungiBarcaButtonAction(ActionEvent event) {
        String idBarca = idBarcaText.getText().trim();
        String nomeBarca = nomeBarcaText.getText().trim();
        String categoriaBarca = (String) categoriaBarcaBox.getValue();
        String descrizioneBarca = descrizioneBarcaArea.getText().trim();
        String chiaviBarca = chiaviBarcaArea.getText().trim();
        String prezzoBarcaStr = prezzoBarcaText.getText().trim();

        // Validazione per rimuovere una barca
        if (!idBarca.isEmpty() && nomeBarca.isEmpty() && categoriaBarca == null && descrizioneBarca.isEmpty() &&
                chiaviBarca.isEmpty() && prezzoBarcaStr.isEmpty()) {

            boolean success = DBConnection.getInstance().rimuoviBarca(idBarca);

            if (success) {
                String imagePath = "src/main/resources/com/progetto/ingsw/immagini/" + idBarca + ".jpg";
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    try {
                        Files.delete(imageFile.toPath());
                    } catch (IOException e) {
                        SceneHandler.getInstance().showAlert("Errore", "La barca è stata rimossa, ma non è stato possibile rimuovere l'immagine.", 0);
                    }
                }
                idBarcaText.clear();
                nomeBarcaText.clear();
                categoriaBarcaBox.getSelectionModel().clearSelection();
                descrizioneBarcaArea.clear();
                chiaviBarcaArea.clear();
                prezzoBarcaText.clear();
            } else {
                SceneHandler.getInstance().showAlert("Errore", "Non è stato possibile rimuovere la barca. Verifica l'ID inserito.", 0);
            }
            return;
        }

        // Validazione dei campi obbligatori
        if (idBarca.isEmpty() || nomeBarca.isEmpty() || categoriaBarca == null || descrizioneBarca.isEmpty() ||
                chiaviBarca.isEmpty() || prezzoBarcaStr.isEmpty()) {
            SceneHandler.getInstance().showAlert("Errore di validazione", "Tutti i campi sono obbligatori.", 0);
            return;
        }

        // Conversione del prezzo a double
        double prezzoBarca;
        try {
            prezzoBarca = Double.parseDouble(prezzoBarcaStr);
            if (prezzoBarca <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            SceneHandler.getInstance().showAlert("Errore di validazione", "Il prezzo deve essere un numero positivo.", 0);
            return;
        }

        // Controllo dell'immagine temporanea
        if (temporaryImageFile == null) {
            SceneHandler.getInstance().showAlert("Errore", "Devi caricare un'immagine prima di aggiungere la barca.", 0);
            return;
        }

        // Verifica se esiste già un'immagine con lo stesso ID
        String outputPath = "src/main/resources/com/progetto/ingsw/immagini/" + idBarca + ".jpg";
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            SceneHandler.getInstance().showAlert("Errore", "Esiste già un'immagine associata a questo ID. Inserisci un nuovo ID o rimuovi la barca esistente.", 0);
            return;
        }

        // Aggiunta della barca al database
        boolean success = DBConnection.getInstance().aggiungiBarca(
                idBarca, nomeBarca, categoriaBarca, descrizioneBarca, chiaviBarca, prezzoBarca
        );

        if (success) {
            String imagePath = "src/main/resources/com/progetto/ingsw/immagini/" + idBarca + ".jpg";
            File outputImageFile = new File(imagePath);

            try {
                Files.copy(temporaryImageFile.toPath(), outputImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                SceneHandler.getInstance().showAlert("Errore", "La barca è stata aggiunta, ma il salvataggio dell'immagine è fallito: " + e.getMessage(), 0);
                return;
            }

            idBarcaText.clear();
            nomeBarcaText.clear();
            categoriaBarcaBox.getSelectionModel().clearSelection();
            descrizioneBarcaArea.clear();
            chiaviBarcaArea.clear();
            prezzoBarcaText.clear();
            temporaryImageFile = null;
        } else {
            SceneHandler.getInstance().showAlert("Errore", "C'è stato un problema durante l'aggiunta della barca.", 0);
        }
    }

}

