package com.progetto.ingsw.Controller;

import com.progetto.ingsw.Controller.Handler.BarcaHandler;
import com.progetto.ingsw.DataBase.Authentication;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.Model.User;
import com.progetto.ingsw.View.SceneHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BarcaViewController {
    @FXML
    private Button addToWishlistButton, prenotaButton;
    @FXML
    private TextField amountField;
    @FXML
    private Text dolcevitaYachteSailText, descriptionField, paymentText, barcaField, titleSimilarBarcaText1, titleSimilarBarcaText2, titleSimilarBarcaText3, titleSimilarBarcaText4, titleSimilarBarcaText5, SimilarBarche , prenotaquiunavisitaText;
    @FXML
    private Text priceText, priceText1, priceText2, priceText3, priceText4, priceText5;
    @FXML
    private ImageView barcaImage, barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5;
    @FXML
    private VBox totalVbox, actionBox, vBoxSimilarBarca1, vBoxSimilarBarca2, vBoxSimilarBarca3, vBoxSimilarBarca4, vBoxSimilarBarca5;
    @FXML
    private HBox barcaImageBox;
    @FXML
    private ComboBox<Integer> annoButton, giornoButton, meseButton;

    @FXML
    void addToWishlistButtonAction(ActionEvent event) {
        User user = Authentication.getInstance().getUser();
        if (user != null){
            String email = user.email();
            String id_bar = BarcaHandler.getInstance().getBarca().id();
            boolean success = DBConnection.getInstance().insertWishlistBarcaIntoDB(email, id_bar);

            if(success){
                SceneHandler.getInstance().showAlert("Conferma", Message.aggiunta_alla_wishlist_ok, 1);
            }
            else {
                SceneHandler.getInstance().showAlert("Errore", Message.aggiunta_alla_wishlist_no, 0);
            }
        }else{
            SceneHandler.getInstance().showAlert("Error", Message.not_logged_in_error,0);
        }
    }

    ImageView[] imageViews;
    Text[] priceTexts;
    VBox[] similarBarcaVBox;
    Text[] similarBarcaText;

    void initializeArrays(){
        imageViews = new ImageView[]{barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5};
        priceTexts = new Text[]{priceText1, priceText2, priceText3, priceText4, priceText5};
        similarBarcaVBox = new VBox[]{vBoxSimilarBarca1, vBoxSimilarBarca2, vBoxSimilarBarca3, vBoxSimilarBarca4, vBoxSimilarBarca5};
        similarBarcaText = new Text[]{titleSimilarBarcaText1, titleSimilarBarcaText2, titleSimilarBarcaText3, titleSimilarBarcaText4, titleSimilarBarcaText5};
    }

    void loadBarca() {
        Barca b = BarcaHandler.getInstance().getBarca();
        String url = "immagini/" + b.id() + ".jpg";
        Image image = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(url)));
        barcaImage.setImage(image);
        barcaField.setText(b.name());
        descriptionField.setText(b.description());

        // Formatta il prezzo
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ITALIAN);
        String formattedPrice = numberFormat.format(b.price());
        priceText.setText(formattedPrice + "€");
    }


    void loadSimilarBarcaViewPage(String id, String category){
        try {
            CompletableFuture<Barca> future = DBConnection.getInstance().getBarca(id);
            Barca b = future.get(10, TimeUnit.SECONDS);

            DBConnection.getInstance().addSimilarBarca(id, category);

            BarcaHandler.getInstance().setBarca(b);
            Platform.runLater(() -> SceneHandler.getInstance().setHomeScene());

        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.load_barca_view_error,0);
        }
    }


    void loadSimilarBarche() {
        ArrayList<Barca> barche = DBConnection.getInstance().getSimilarBarche();
        for (int i = 0; i < barche.size(); i++) {
            String url = "immagini/" + barche.get(i).id() + ".jpg";
            Image image = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(url)));
            imageViews[i].setImage(image);
            similarBarcaText[i].setText(barche.get(i).name());

            // Formatta il prezzo
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ITALIAN);
            String formattedPrice = numberFormat.format(barche.get(i).price());
            priceTexts[i].setText(formattedPrice + "€");

            String id = barche.get(i).id();
            String category = barche.get(i).category();
            similarBarcaVBox[i].setOnMouseClicked(event -> loadSimilarBarcaViewPage(id, category));
        }
    }


    void populateComboBoxes() {
        giornoButton.getItems().addAll((Integer[]) IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new));

        meseButton.getItems().addAll((Integer[]) IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));

        annoButton.getItems().addAll(2024, 2025);
    }

    @FXML
    void prenotaButtonAction(ActionEvent event) {
        Integer giorno = giornoButton.getValue();
        Integer mese = meseButton.getValue();
        Integer anno = annoButton.getValue();

        if (giorno == null || mese == null || anno == null) {
            SceneHandler.getInstance().showAlert("Error", Message.data_error, 0);
            return;
        }

        try {
            LocalDate dataPrenotazione = LocalDate.of(anno, mese, giorno);

            if (dataPrenotazione.isBefore(LocalDate.now())) {
                SceneHandler.getInstance().showAlert("Error", Message.data_error2, 0);
                return;
            }

            User user = Authentication.getInstance().getUser();
            if (user != null) {
                String email = user.email();
                String id_bar = BarcaHandler.getInstance().getBarca().id();
                DBConnection.getInstance().insertPrenotazioneIntoDB(email, id_bar, giorno, mese, anno);
            } else {
                SceneHandler.getInstance().showAlert("Error", Message.not_logged_in_error, 0);
            }
        } catch (DateTimeException e) {
            SceneHandler.getInstance().showAlert("Errore", Message.data_error3, 0);
        }
    }

    @FXML
    void initialize(){
        initializeArrays();
        loadBarca();
        loadSimilarBarche();
        populateComboBoxes();

        User user = Authentication.getInstance().getUser();
        if (user != null && user.isAdmin()) {
            annoButton.setVisible(false);
            meseButton.setVisible(false);
            giornoButton.setVisible(false);
            prenotaButton.setVisible(false);
            prenotaquiunavisitaText.setVisible(false);
        }
    }


}

