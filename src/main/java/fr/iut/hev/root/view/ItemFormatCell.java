package fr.iut.hev.root.view;

import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ItemFormatCell extends ListCell<RecipesEnum> {

    @Override
    protected void updateItem(RecipesEnum recipesEnum, boolean b) {
        super.updateItem(recipesEnum, b);

        setText(null);
        if (b || recipesEnum == null) {
            setGraphic(null);
        }
        else {
            System.out.println(recipesEnum.getCraftResult().getName());
            String path = "/fr/iut/hev/root/img/items/" + recipesEnum.getCraftResult().getName() + ".png";
            System.out.println(path);
            ImageView itemImage= new ImageView(new Image(getClass().getResource(path).toExternalForm()));
            itemImage.setFitWidth(60);
            itemImage.setFitHeight(60);
            Pane cellPane = new Pane(itemImage);
            cellPane.setBackground(Background.fill(Color.rgb(0, 0, 0, 0.25)));
            cellPane.setPrefHeight(60);
            cellPane.setPrefWidth(60);
            setGraphic(cellPane);
        }
    }
}
