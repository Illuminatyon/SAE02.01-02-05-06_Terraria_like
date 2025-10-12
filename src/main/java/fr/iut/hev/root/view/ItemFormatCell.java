package fr.iut.hev.root.view;

import fr.iut.hev.root.model.craft.RecipesEnum;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
