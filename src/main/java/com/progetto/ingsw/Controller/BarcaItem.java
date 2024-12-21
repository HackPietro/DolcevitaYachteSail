package com.progetto.ingsw.Controller;

import com.progetto.ingsw.MainApplication;
import com.progetto.ingsw.Model.Barca;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.Objects;

class BarcaItem implements CloneableBarca {
    private ImageView imageView;
    private Text titleText;
    private Text priceText;
    private VBox vBox;

    public BarcaItem(ImageView imageView, Text titleText, Text priceText, VBox vBox) {
        this.imageView = imageView;
        this.titleText = titleText;
        this.priceText = priceText;
        this.vBox = vBox;
    }

    @Override
    public CloneableBarca clone() {
        return new BarcaItem(imageView, titleText, priceText, vBox);
    }

    @Override
    public void updateDetails(Barca barca) {
        String url = "immagini/" + barca.id() + ".jpg";
        Image image = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(url)));
        imageView.setImage(image);
        titleText.setText(barca.name());
        priceText.setText(barca.price() + "€");
    }

    public VBox getVBox() {
        return vBox;
    }
}
