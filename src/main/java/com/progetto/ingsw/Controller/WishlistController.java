package com.progetto.ingsw.Controller;

import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.View.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.progetto.ingsw.DataBase.Authentication;
import java.text.NumberFormat;
import java.util.Locale;

public class WishlistController {
    @FXML
    private Text userText;
    @FXML
    private ImageView wishlistImage1, wishlistImage2, wishlistImage3, wishlistImage4, wishlistImage5, wishlistImage6;
    @FXML
    private Text wishlistPriceBar1, wishlistPriceBar2, wishlistPriceBar3, wishlistPriceBar4, wishlistPriceBar5, wishlistPriceBar6;
    @FXML
    private Button wishlistRemoveButton1, wishlistRemoveButton2, wishlistRemoveButton3, wishlistRemoveButton4, wishlistRemoveButton5, wishlistRemoveButton6;
    @FXML
    private Text wishlistTitleBar1, wishlistTitleBar2, wishlistTitleBar3, wishlistTitleBar4, wishlistTitleBar5, wishlistTitleBar6;
    @FXML
    private VBox vBoxWishlist;
    @FXML
    private HBox hBox1, hBox2, hBox3, hBox4, hBox5, hBox6;

    ImageView[] imageViews;
    Text[] titleTextBar;
    Text[] priceText;
    Button[] removeButtons;
    HBox[] hBoxes;

    void initializeObject(){
        imageViews = new ImageView[]{wishlistImage1, wishlistImage2, wishlistImage3, wishlistImage4, wishlistImage5, wishlistImage6};
        titleTextBar = new Text[]{wishlistTitleBar1, wishlistTitleBar2, wishlistTitleBar3, wishlistTitleBar4, wishlistTitleBar5, wishlistTitleBar6};
        removeButtons = new Button[]{wishlistRemoveButton1, wishlistRemoveButton2, wishlistRemoveButton3, wishlistRemoveButton4, wishlistRemoveButton5, wishlistRemoveButton6};
        priceText = new Text[]{wishlistPriceBar1, wishlistPriceBar2, wishlistPriceBar3, wishlistPriceBar4, wishlistPriceBar5, wishlistPriceBar6};
        hBoxes = new HBox[]{hBox1, hBox2, hBox3, hBox4, hBox5, hBox6};
    }

    void setAccount() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        String name = Authentication.getInstance().getUser().name();
        String surname = Authentication.getInstance().getUser().surname();
        String email = Authentication.getInstance().getUser().email();
        userText.setText(name + " " + surname);
        loadWishlist(email);
    }

    void removeItem(String id, String email) throws ExecutionException, InterruptedException, TimeoutException, SQLException {
        try {
            for (int i = 0; i < 6; i++) {
                hBoxes[i].setVisible(false);
            }
            DBConnection.getInstance().removeSelectedWishlistItem(id, email);
            SceneHandler.getInstance().showAlert("Conferma", Message.rimozione_wishlist_ok, 1);
        } catch (Exception e) {
            SceneHandler.getInstance().showAlert("Error", Message.rimozione_wishlist_no, 0);
            e.printStackTrace(); // Per il log dell'errore
        }
        loadWishlist(email);
    }

    void loadWishlist(String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<ArrayList<Barca>> future = DBConnection.getInstance().getWishlist(email);
        ArrayList<Barca> bar = future.get(10, TimeUnit.SECONDS);
        for (int i = 0; i < bar.size(); i++) {
            String url = "immagini/" + bar.get(i).id() + ".jpg";
            Image image = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(url)));
            imageViews[i].setImage(image);
            titleTextBar[i].setText(bar.get(i).name());

            // Formatta il prezzo
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ITALIAN);
            String formattedPrice = numberFormat.format(bar.get(i).price());
            priceText[i].setText(formattedPrice + "€");

            String id = bar.get(i).id();
            removeButtons[i].setOnMouseClicked(mouseEvent -> {
                try {
                    removeItem(id, email);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            hBoxes[i].setVisible(true);
        }
    }

    @FXML
    void initialize() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        initializeObject();
        setAccount();
    }

}



