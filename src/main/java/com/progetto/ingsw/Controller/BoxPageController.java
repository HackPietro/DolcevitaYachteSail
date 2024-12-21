package com.progetto.ingsw.Controller;

import com.progetto.ingsw.Controller.Handler.BarcaHandler;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.View.SceneHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BoxPageController {

    @FXML
    private HBox buttomBoxPage;
    @FXML
    private Text priceText1, priceText2, priceText3, priceText4, priceText5, priceText6, priceText7, priceText8, priceText9, priceText10, priceText11, priceText12, priceText13, priceText14, priceText15;
    @FXML
    private ImageView barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5, barcaImage6, barcaImage7, barcaImage8, barcaImage9, barcaImage10, barcaImage11, barcaImage12, barcaImage13, barcaImage14, barcaImage15;
    @FXML
    private Text titleText1, titleText2, titleText3, titleText4, titleText5, titleText6, titleText7, titleText8, titleText9, titleText10, titleText11, titleText12, titleText13, titleText14, titleText15;

    ImageView[] barcaImages;
    Text[] titleTextArray;
    Text[] priceArray;
    VBox[] vBoxes;

    @FXML
    private VBox totalVbox, vBoxBarca1, vBoxBarca2, vBoxBarca3, vBoxBarca4, vBoxBarca5, vBoxBarca6, vBoxBarca7, vBoxBarca8, vBoxBarca9, vBoxBarca10, vBoxBarca11, vBoxBarca12, vBoxBarca13, vBoxBarca14, vBoxBarca15, totalBox;
    @FXML
    private HBox hBox1, hBox2, hBox3;
    @FXML
    private ScrollPane scrollPaneBoxPage;

    private List<CloneableBarca> barcaItems;

    void arrayInitialize(){
        barcaImages = new ImageView[]{barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5, barcaImage6, barcaImage7, barcaImage8, barcaImage9, barcaImage10, barcaImage11, barcaImage12, barcaImage13, barcaImage14, barcaImage15};
        titleTextArray = new Text[]{titleText1, titleText2, titleText3, titleText4, titleText5, titleText6, titleText7, titleText8, titleText9, titleText10, titleText11, titleText12, titleText13, titleText14, titleText15};
        priceArray = new Text[]{priceText1, priceText2, priceText3, priceText4, priceText5, priceText6, priceText7, priceText8, priceText9, priceText10, priceText11, priceText12, priceText13, priceText14, priceText15};
        vBoxes = new VBox[]{vBoxBarca1, vBoxBarca2, vBoxBarca3, vBoxBarca4, vBoxBarca5, vBoxBarca6, vBoxBarca7, vBoxBarca8, vBoxBarca9, vBoxBarca10, vBoxBarca11, vBoxBarca12, vBoxBarca13, vBoxBarca14, vBoxBarca15, totalBox};

        // Lista che conterrà i clone degli oggetti BarcaItem
        barcaItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            // Cloniamo l'oggetto BarcaItem per ogni elemento
            BarcaItem clonedBarcaItem = (BarcaItem) new BarcaItem(barcaImages[i], titleTextArray[i], priceArray[i], vBoxes[i]).clone();
            barcaItems.add(clonedBarcaItem);
        }
    }

    private void loadBarcaViewPage(String id, String category) {
        try {
            CompletableFuture<Barca> future = DBConnection.getInstance().getBarca(id);
            Barca b = future.get(10, TimeUnit.SECONDS);
            BarcaHandler.getInstance().setBarca(b);
            DBConnection.getInstance().addSimilarBarca(id, category);
            Platform.runLater(() -> SceneHandler.getInstance().setHomeScene());
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("Error", Message.load_barca_view_error,0);
        }
    }

    private void loadHomePageImage(){
        try{
            CompletableFuture<ArrayList<Barca>> future = DBConnection.getInstance().addHomePageBarche();
            ArrayList<Barca> barche = future.get(10, TimeUnit.SECONDS);
            List<Integer> randomNumber = new ArrayList<>();
            for (int i = 0; i < 48; i++){
                randomNumber.add(i);
            }
            Collections.shuffle(randomNumber);
            for (int i = 0; i < 15; i++) {
                BarcaItem barcaItem = (BarcaItem) barcaItems.get(i); // Otteniamo il clone
                Barca barca = barche.get(randomNumber.get(i));

                barcaItem.updateDetails(barca);

                String id = barca.id();
                String category = barca.category();
                barcaItem.getVBox().setOnMouseClicked(event -> loadBarcaViewPage(id, category));
            }
        } catch (Exception e){
            SceneHandler.getInstance().showAlert("", Message.add_home_page_barche_error, 0);
        }
    }

    @FXML
    void initialize(){
        arrayInitialize();
        loadHomePageImage();
    }
}
