package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Loot;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class LootView {
    private HashMap<Loot, ImageView> lootImageViews;
    private Pane landPane;

    public LootView(Pane landPane) {
        this.landPane = landPane;
        lootImageViews = new HashMap<Loot, ImageView>();

        Loot.lootOnMapProperty.get().addListener((SetChangeListener<Loot>) change -> {
            if (change.wasAdded()) {
                System.out.println("Loot ajouté sur la map");
                String path = "/fr/iut/hev/root/img/items/" + change.getElementAdded().getItem().getItemEnum().getName() + ".png";
                try {
                    java.net.URL resourceUrl = getClass().getResource(path);
                    if (resourceUrl == null) {
                        System.err.println("Resource not found: " + path);
                        // Use a default image or create a colored rectangle as a placeholder
                        ImageView imgv = new ImageView();
                        imgv.setFitWidth(32);
                        imgv.setFitHeight(32);
                        // Set initial position (will be updated with camera offset)
                        imgv.setLayoutX(change.getElementAdded().getPosX());
                        imgv.setLayoutY(change.getElementAdded().getPosY());
                        lootImageViews.put(change.getElementAdded(), imgv);
                        landPane.getChildren().add(imgv);
                    } else {
                        Image img = new Image(resourceUrl.toExternalForm());
                        ImageView imgv = new ImageView(img);
                        // Set initial position (will be updated with camera offset)
                        imgv.setLayoutX(change.getElementAdded().getPosX());
                        imgv.setLayoutY(change.getElementAdded().getPosY());
                        lootImageViews.put(change.getElementAdded(), imgv);
                        landPane.getChildren().add(imgv);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image for loot: " + e.getMessage());
                }
            } else if (change.wasRemoved()) {
                try {
                    ImageView imgView = lootImageViews.get(change.getElementRemoved());
                    if (imgView != null) {
                        imgView.setImage(null);
                        landPane.getChildren().remove(imgView);
                    }
                    lootImageViews.remove(change.getElementRemoved());
                    System.out.println("Loot retiré de la map");
                } catch (Exception e) {
                    System.err.println("Error removing loot image: " + e.getMessage());
                }
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
