package com.progetto.ingsw.Controller;

import com.progetto.ingsw.Controller.Handler.BarcaHandler;
import com.progetto.ingsw.DataBase.DBConnection;
import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Message;
import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.View.SceneHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CategoryBoxController {

    @FXML
    private VBox vBoxBarca1, vBoxBarca2, vBoxBarca3, vBoxBarca4, vBoxBarca5, vBoxBarca6, vBoxBarca7, vBoxBarca8, vBoxBarca9, vBoxBarca10, vBoxBarca11, vBoxBarca12, vBoxBarca13, vBoxBarca14, vBoxBarca15;
    @FXML
    private Text titleText1, titleText2, titleText3, titleText4, titleText5, titleText6, titleText7, titleText8, titleText9, titleText10, titleText11, titleText12, titleText13, titleText14, titleText15;
    @FXML
    private Text priceText1, priceText2, priceText3, priceText4, priceText5, priceText6, priceText7, priceText8, priceText9, priceText10, priceText11, priceText12, priceText13, priceText14, priceText15;
    @FXML
    private ImageView barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5, barcaImage6, barcaImage7, barcaImage8, barcaImage9, barcaImage10, barcaImage11, barcaImage12, barcaImage13, barcaImage14, barcaImage15;
    @FXML
    private HBox hBox1, hBox2;
    ImageView[] barcaImagesArray;
    Text[] titleTextArray;
    Text[] priceArray;
    VBox[] vBoxArray;

    void arrayInitialize(){
        vBoxArray = new VBox[]{vBoxBarca1, vBoxBarca2, vBoxBarca3, vBoxBarca4, vBoxBarca5, vBoxBarca6, vBoxBarca7, vBoxBarca8, vBoxBarca9, vBoxBarca10, vBoxBarca11,  vBoxBarca12, vBoxBarca13, vBoxBarca14, vBoxBarca15};
        barcaImagesArray = new ImageView[]{barcaImage1, barcaImage2, barcaImage3, barcaImage4, barcaImage5, barcaImage6, barcaImage7, barcaImage8, barcaImage9, barcaImage10, barcaImage11, barcaImage12, barcaImage13, barcaImage14, barcaImage15};
        titleTextArray = new Text[]{titleText1, titleText2, titleText3, titleText4, titleText5, titleText6, titleText7, titleText8, titleText9, titleText10, titleText11, titleText12, titleText13, titleText14, titleText15};
        priceArray = new Text[]{priceText1, priceText2, priceText3, priceText4, priceText5, priceText6, priceText7, priceText8, priceText9, priceText10, priceText11, priceText12, priceText13, priceText14, priceText15};
    }

    void removeExcessBox(int size){
        for (int i = size; i < 15; i++){
            vBoxArray[i].setVisible(false);
        }
    }

    private void loadBarcaViewPage(String id, String category) {
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

    void addBarca(ArrayList<Barca> barche){
        for (int i = 0; i < barche.size(); i++){
            String imagePath = "src/main/resources/com/progetto/ingsw/immagini/" + barche.get(i).id() + ".jpg";
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString()); // Percorso assoluto
                barcaImagesArray[i].setImage(image);
            } else {
                // Immagine di default in caso non esista ancora
                Image defaultImage = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("immagini/default.jpg")));
                barcaImagesArray[i].setImage(defaultImage);
            }

            titleTextArray[i].setText(barche.get(i).name());
            priceArray[i].setText(barche.get(i).price() + "€");

            String id = barche.get(i).id();
            String category = barche.get(i).category();
            vBoxArray[i].setOnMouseClicked(event -> loadBarcaViewPage(id, category));
        }
    }



    @FXML
    void initialize(){
        arrayInitialize();
        ArrayList<Barca> barche = new ArrayList<>();
        if (DBConnection.getInstance().getSearchedBarche().isEmpty()){
            barche = DBConnection.getInstance().getCategoryBarche();
        }else{
            barche = DBConnection.getInstance().getSearchedBarche();
        }
        if(barche != null){
            removeExcessBox(barche.size());
            addBarca(barche);
            DBConnection.getInstance().clearSearchedList();
            DBConnection.getInstance().clearCategoryBarcaList();
        }else{
            removeExcessBox(0);
        }
    }


}




