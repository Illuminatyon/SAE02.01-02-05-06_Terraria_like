package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Loot;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class LootView {
    public LootView(Pane pane) {
        Loot.lootOnMapProperty.get().addListener((SetChangeListener<Loot>) change -> {
            if (change.wasAdded()) {
                System.out.println("Loot ajouté sur la map");
                String path = "/fr/iut/hev/root/img/items/dirt.png";
                Image img = new Image(getClass().getResource(path).toExternalForm());
                ImageView imgv = new ImageView(img);
                imgv.translateXProperty().bind(change.getElementAdded().posXProperty());
                imgv.translateYProperty().bind(change.getElementAdded().posYProperty());
                pane.getChildren().add(imgv);
            } else if (change.wasRemoved()) {
                System.out.println("Loot retiré de la map");
            }
        });
    }
}
