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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private Button changePasswordButton, sendReportButton;
    @FXML
    private PasswordField passwordField, repeatPasswordField;
    @FXML
    private ScrollPane scrollPaneAccount;
    @FXML
    private TextArea sendReportArea;


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

    @FXML
    void sendReportButtonAction(ActionEvent event) {
        if (!sendReportArea.getText().isEmpty()){
            createInfoThread();
        }
        sendReportArea.clear();
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
    }
}

