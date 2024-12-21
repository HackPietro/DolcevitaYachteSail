package com.progetto.ingsw.Controller;

import com.progetto.ingsw.DataBase.Authentication;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.DataBase.ProxyPrenotazione;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.Model.Prenotazione;
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

public class PrenotazioniController {

    @FXML
    private Text addedToCartText;

    @FXML
    private Text balanceText;

    @FXML
    private Text dataPrenotazione1;

    @FXML
    private Text dataPrenotazione2;

    @FXML
    private Text dataPrenotazione3;

    @FXML
    private Text dataPrenotazione4;

    @FXML
    private Text dataPrenotazione5;

    @FXML
    private Text dataPrenotazione6;

    @FXML
    private HBox hBox1;

    @FXML
    private HBox hBox2;

    @FXML
    private HBox hBox3;

    @FXML
    private HBox hBox4;

    @FXML
    private HBox hBox5;

    @FXML
    private HBox hBox6;

    @FXML
    private Text prenotazioniAnnoBar1;

    @FXML
    private Text prenotazioniAnnoBar2;

    @FXML
    private Text prenotazioniAnnoBar3;

    @FXML
    private Text prenotazioniAnnoBar4;

    @FXML
    private Text prenotazioniAnnoBar5;

    @FXML
    private Text prenotazioniAnnoBar6;

    @FXML
    private Text prenotazioniGiornoBar1;

    @FXML
    private Text prenotazioniGiornoBar2;

    @FXML
    private Text prenotazioniGiornoBar3;

    @FXML
    private Text prenotazioniGiornoBar4;

    @FXML
    private Text prenotazioniGiornoBar5;

    @FXML
    private Text prenotazioniGiornoBar6;

    @FXML
    private ImageView prenotazioniImage1;

    @FXML
    private ImageView prenotazioniImage2;

    @FXML
    private ImageView prenotazioniImage3;

    @FXML
    private ImageView prenotazioniImage4;

    @FXML
    private ImageView prenotazioniImage5;

    @FXML
    private ImageView prenotazioniImage6;

    @FXML
    private Text prenotazioniMeseBar1;

    @FXML
    private Text prenotazioniMeseBar2;

    @FXML
    private Text prenotazioniMeseBar3;

    @FXML
    private Text prenotazioniMeseBar4;

    @FXML
    private Text prenotazioniMeseBar5;

    @FXML
    private Text prenotazioniMeseBar6;

    @FXML
    private Button prenotazioniRemoveButton1;

    @FXML
    private Button prenotazioniRemoveButton2;

    @FXML
    private Button prenotazioniRemoveButton3;

    @FXML
    private Button prenotazioniRemoveButton4;

    @FXML
    private Button prenotazioniRemoveButton5;

    @FXML
    private Button prenotazioniRemoveButton6;

    @FXML
    private Text prenotazioniTitleBar1;

    @FXML
    private Text prenotazioniTitleBar2;

    @FXML
    private Text prenotazioniTitleBar3;

    @FXML
    private Text prenotazioniTitleBar4;

    @FXML
    private Text prenotazioniTitleBar5;

    @FXML
    private Text prenotazioniTitleBar6;

    @FXML
    private Text trattino1Bar1;

    @FXML
    private Text trattino1Bar2;

    @FXML
    private Text trattino1Bar3;

    @FXML
    private Text trattino1Bar4;

    @FXML
    private Text trattino1Bar5;

    @FXML
    private Text trattino1Bar6;

    @FXML
    private Text trattino2Bar1;

    @FXML
    private Text trattino2Bar2;

    @FXML
    private Text trattino2Bar3;

    @FXML
    private Text trattino2Bar4;

    @FXML
    private Text trattino2Bar5;

    @FXML
    private Text trattino2Bar6;

    @FXML
    private Text userText;

    @FXML
    private Text utentePrenotatoBar1;

    @FXML
    private Text utentePrenotatoBar2;

    @FXML
    private Text utentePrenotatoBar3;

    @FXML
    private Text utentePrenotatoBar4;

    @FXML
    private Text utentePrenotatoBar5;

    @FXML
    private Text utentePrenotatoBar6;

    @FXML
    private VBox vBoxPrenotazioni;

    @FXML
    private VBox vBoxPrenotazioni1;


    ImageView[] imageViews;
    Text[] titleTextBar;
    Text[] dataPrenotazioneText;
    Text[] giornoText;
    Text[] trattino1Text;
    Text[] meseText;
    Text[] trattino2Text;
    Text[] annoText;
    Text[] utentePrenotato;
    Button[] removeButtons;
    HBox[] hBoxes;

    void initializeObject(){
        imageViews = new ImageView[]{prenotazioniImage1, prenotazioniImage2, prenotazioniImage3, prenotazioniImage4, prenotazioniImage5, prenotazioniImage6};
        titleTextBar = new Text[]{prenotazioniTitleBar1, prenotazioniTitleBar2, prenotazioniTitleBar3, prenotazioniTitleBar4, prenotazioniTitleBar5, prenotazioniTitleBar6};
        removeButtons = new Button[]{prenotazioniRemoveButton1, prenotazioniRemoveButton2, prenotazioniRemoveButton3, prenotazioniRemoveButton4, prenotazioniRemoveButton5, prenotazioniRemoveButton6};
        dataPrenotazioneText = new Text[]{dataPrenotazione1, dataPrenotazione2, dataPrenotazione3, dataPrenotazione4, dataPrenotazione5, dataPrenotazione6};
        giornoText = new Text[]{prenotazioniGiornoBar1, prenotazioniGiornoBar2,prenotazioniGiornoBar3,prenotazioniGiornoBar4, prenotazioniGiornoBar5, prenotazioniGiornoBar6};
        trattino1Text = new Text[]{trattino1Bar1, trattino1Bar2, trattino1Bar3, trattino1Bar4, trattino1Bar5, trattino1Bar6};
        meseText = new Text[]{prenotazioniMeseBar1, prenotazioniMeseBar2, prenotazioniMeseBar3, prenotazioniMeseBar4, prenotazioniMeseBar5, prenotazioniMeseBar6};
        trattino2Text = new Text[]{trattino2Bar1, trattino2Bar2, trattino2Bar3, trattino2Bar4, trattino2Bar5, trattino2Bar6};
        annoText = new Text[]{prenotazioniAnnoBar1, prenotazioniAnnoBar2, prenotazioniAnnoBar3, prenotazioniAnnoBar4, prenotazioniAnnoBar5, prenotazioniAnnoBar6};
        utentePrenotato = new Text[]{utentePrenotatoBar1, utentePrenotatoBar2, utentePrenotatoBar3, utentePrenotatoBar4, utentePrenotatoBar5, utentePrenotatoBar6};
        hBoxes = new HBox[]{hBox1, hBox2, hBox3, hBox4, hBox5, hBox6};
    }


    void setAccount() throws SQLException, ExecutionException, InterruptedException, TimeoutException {
        String name = Authentication.getInstance().getUser().name();
        String surname = Authentication.getInstance().getUser().surname();
        String email = Authentication.getInstance().getUser().email();
        userText.setText(name + " " + surname);
        loadPrenotazioni(email);
    }

    void removeItem(String id, String email) throws Exception {
        for (int i = 0; i < 6; i++){
            hBoxes[i].setVisible(false);
        }
        System.out.println("id " + id + " email " + email);
        DBConnection.getInstance().removeSelectedPrenotazioniItem(id, email);
        initialize();
    }


    void loadPrenotazioni(String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<ArrayList<Prenotazione>> future = ProxyPrenotazione.getInstance().getPrenotazioni();
        ArrayList<Prenotazione> pre = future.get(10, TimeUnit.SECONDS);
        int size = Math.min(pre.size(), 6);
        System.out.println("size " + pre.size());
        for (int i = 0; i < size; i++) {
            String url = "immagini/" + pre.get(i).id_barca() + ".jpg";
            Image image = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(url)));
            imageViews[i].setImage(image);
            titleTextBar[i].setText(DBConnection.getInstance().getBarcaName(pre.get(i).id_barca()));
            dataPrenotazioneText[i].setText("Data Prenotazione:");
            giornoText[i].setText(String.valueOf(pre.get(i).giorno()));
            trattino1Text[i].setText("/");
            meseText[i].setText(String.valueOf(pre.get(i).mese()));
            trattino2Text[i].setText("/");
            annoText[i].setText(String.valueOf(pre.get(i).anno()));
            utentePrenotato[i].setText(pre.get(i).id_utente());
            String id = pre.get(i).id_barca();
            String utente = pre.get(i).id_utente();
            removeButtons[i].setOnMouseClicked(mouseEvent -> {
                try {
                    removeItem(id, utente);
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

