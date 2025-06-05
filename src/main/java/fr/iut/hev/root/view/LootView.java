package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Loot;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class LootView {
    private HashMap<Loot, ImageView> lootImageViews;

    public LootView(Pane pane) {
        lootImageViews = new HashMap<Loot, ImageView>();

        Loot.lootOnMapProperty.get().addListener((SetChangeListener<Loot>) change -> {
            if (change.wasAdded()) {
                System.out.println("Loot ajouté sur la map");
                String path = "/fr/iut/hev/root/img/items/dirt.png";
                Image img = new Image(getClass().getResource(path).toExternalForm());
                ImageView imgv = new ImageView(img);
                imgv.translateXProperty().bind(change.getElementAdded().posXProperty());
                imgv.translateYProperty().bind(change.getElementAdded().posYProperty());
                lootImageViews.put(change.getElementAdded(), imgv);
                pane.getChildren().add(imgv);
            } else if (change.wasRemoved()) {
                lootImageViews.get(change.getElementRemoved()).setImage(null);
                lootImageViews.remove(change.getElementRemoved());
                System.out.println("Loot retiré de la map");
            }
        });
    }
}
