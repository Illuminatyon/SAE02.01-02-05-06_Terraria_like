package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Loot;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class LootView {
    private HashMap<Loot, ImageView> lootImageViews;
    private Pane pane;

    public LootView(Pane pane) {
        this.pane = pane;
        lootImageViews = new HashMap<Loot, ImageView>();

        Loot.lootOnMapProperty.get().addListener((SetChangeListener<Loot>) change -> {
            if (change.wasAdded()) {
                System.out.println("Loot ajouté sur la map");
                String path = "/fr/iut/hev/root/img/items/" + change.getElementAdded().getItem().getItemEnum().getName() + ".png";
                Image img = new Image(getClass().getResource(path).toExternalForm());
                ImageView imgv = new ImageView(img);
                // Set initial position (will be updated with camera offset)
                imgv.setLayoutX(change.getElementAdded().getPosX());
                imgv.setLayoutY(change.getElementAdded().getPosY());
                lootImageViews.put(change.getElementAdded(), imgv);
                pane.getChildren().add(imgv);
            } else if (change.wasRemoved()) {
                lootImageViews.get(change.getElementRemoved()).setImage(null);
                lootImageViews.remove(change.getElementRemoved());
                System.out.println("Loot retiré de la map");
            }
        });
    }

    /**
     * Updates the positions of all loot items with the camera offset
     * @param cameraOffsetX the camera offset X
     * @param cameraOffsetY the camera offset Y
     */
    public void updateLootPositions(double cameraOffsetX, double cameraOffsetY) {
        for (Loot loot : lootImageViews.keySet()) {
            ImageView imgv = lootImageViews.get(loot);
            imgv.setLayoutX(loot.getPosX() + cameraOffsetX);
            imgv.setLayoutY(loot.getPosY() + cameraOffsetY);
        }
    }
}
