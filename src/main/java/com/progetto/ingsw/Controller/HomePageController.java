package com.progetto.ingsw.Controller;

import com.progetto.ingsw.Controller.Handler.BarcaHandler;
import com.progetto.ingsw.DataBase.Authentication;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.View.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.*;

public class HomePageController {
    @FXML
    private AnchorPane totalPane;
    @FXML
    private Button searchButton;
    @FXML
    private HBox accessHbox;
    @FXML
    private Pane bottomPane, upperPane;
    @FXML
    private ImageView loginIcon, logoutImage, dolcevitaYachteSailIcon, wishlistIcon, prenotazioniIcon;
    @FXML
    private Text lightingText, loginText, priceText, dolcevitaYachteSailInformation, generalCondition, privacyInformation;
    @FXML
    private Separator logoutSeparator;
    @FXML
    private Text barcheAMotoreText, textUpgrade, barcheAVelaText,  barcheDaPescaText,  barcheSportiveText, barcheTradizionaliText, catamaraniText, gommoniText, trimaraniText;
    @FXML
    private TextField searchBar;
    @FXML
    void dolcevitaYachteSailInformationAction(MouseEvent event) {
        try{
            SceneHandler.getInstance().showAlert("Informazioni su di noi", Message.app_information, 1);
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.app_information_error, 0);
        }
    }

    @FXML
    void generalConditionAction(MouseEvent event) {
        try{
            SceneHandler.getInstance().showAlert("Condizioni generali di uso e vendita", Message.general_condition, 1);
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.general_condition_error, 0);
        }
    }

    @FXML
    void upgradeInformation(MouseEvent event) {
        try{
            SceneHandler.getInstance().showAlert("Upgrade a premium", Message.upgrade_information, 1);
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.upgrade_information_error, 0);
        }
    }


    @FXML
    void privacyInformationAction(MouseEvent event) {
        try{
            SceneHandler.getInstance().showAlert("Informativa sulla privacy", Message.privacy_information, 1);
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.privacy_information_error, 0);
        }
    }



    @FXML
    void LoginAction(MouseEvent event) {
        try {
            if (!Authentication.getInstance().settedUser()) {
                BarcaHandler.getInstance().setNullBarca();
                SceneHandler.getInstance().setLoginScene();
            }
            else{
                bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/account.fxml"))));
            }
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.login_error, 0);
        }
    }


    @FXML
    void getHomeAction(MouseEvent event) {
        try {
            BarcaHandler.getInstance().setBarca(null);
            SceneHandler.getInstance().setHomeScene();
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.returnHome_error, 0);
        }
    }


    @FXML
    void logoutAction(MouseEvent event) throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        BarcaHandler.getInstance().setNullBarca();
        Authentication.getInstance().logout();
        initialize();
    }

    @FXML
    void wishlistAction(MouseEvent event) {
        try {
            if (Authentication.getInstance().settedUser()){
                bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/wishlist.fxml"))));
                priceText.setVisible(false);
            }
            else {
                SceneHandler.getInstance().showAlert("Errore", Message.not_logged_in_error, 0);
            }
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.wishlist_error, 0);
        }
    }


    @FXML
    void barcheAMotoreAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Barche a motore");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }

    @FXML
    void barcheAVelaAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Barche a vela");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }

    @FXML
    void barcheDaPescaAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Barche da pesca");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }

    }

    @FXML
    void barcheSportiveAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Barche sportive");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }

    @FXML
    void barcheTradizionaliAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Barche tradizionali");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }
    @FXML
    void catamaraniAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Catamarani");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }
    @FXML
    void gommoniAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Gommoni");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }

    @FXML
    void trimaraniAction(MouseEvent event) {
        try{
            DBConnection.getInstance().addCategoryBarche("Trimarani");
            bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Errore", Message.load_page_error, 0);
        }
    }


    void login() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        logoutImage.setVisible(true);
        loginText.setText("Account");
        String email = Authentication.getInstance().getUser().email();
    }

    void logout() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        logoutImage.setVisible(false);
        loginText.setText("Accesso");
    }

    @FXML
    void searchButtonAction(ActionEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        String text = searchBar.getText().toLowerCase();
        String textWithOutSpace = text.trim();
        if (!text.isEmpty() && !textWithOutSpace.isEmpty()){
            CompletableFuture<Label> future = DBConnection.getInstance().searchBarca(text);
            Label searchedBarca = future.get(10, TimeUnit.SECONDS);
            String searchedText = searchedBarca.getText();
            String[] prod = searchedText.split(";");
            DBConnection.getInstance().addSearchedBarche(prod);
            try{
                bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/category_box.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        searchBar.setText("");
    }

    @FXML
    void enterPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER){
            ActionEvent ignored = new ActionEvent();
            searchButtonAction(ignored);
        }
    }

    @FXML
    void initialize() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        try{
            if (BarcaHandler.getInstance().getBarca() != null){
                bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/barca_view.fxml"))));
            }else{
                bottomPane.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("fxmls/box_page.fxml"))));
            }
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.box_page_error, 0);
        }
        if (Authentication.getInstance().settedUser()){
            login();
        }
        else{
            logout();
        }
    }

}


