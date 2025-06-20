package fr.iut.hev.root.view;

import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ItemFormatCell extends ListCell<RecipesEnum> {

    @Override
    protected void updateItem(RecipesEnum recipesEnum, boolean empty) {
        super.updateItem(recipesEnum, empty);

        setText(null);
        if (empty || recipesEnum == null) {
            setGraphic(null);
        }
        else {
            String path = "/fr/iut/hev/root/img/items/" + recipesEnum.getCraftResult().getName() + ".png";
            ImageView itemImage= new ImageView(new Image(getClass().getResource(path).toExternalForm()));
            itemImage.setFitWidth(60);
            itemImage.setFitHeight(60);
            setGraphic(itemImage);

        }
    }
}
