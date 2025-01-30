package com.progetto.ingsw;

import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.View.SceneHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.concurrent.*;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DBConnection.getInstance().createConnection();
            SceneHandler.getInstance().init(primaryStage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
