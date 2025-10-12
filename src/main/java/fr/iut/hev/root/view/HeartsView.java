package fr.iut.hev.root.view;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static fr.iut.hev.root.model.land.TileMap.format;

public class HeartsView {

    private final HBox heartsHbox;
    private final List<ImageView> fullHearts;
    private final Image fullHeartImage;
    private final Image halfHeartImage;

    public HeartsView(IntegerProperty healthProperty, HBox heartsHbox) {
        this.heartsHbox = heartsHbox;
        this.fullHearts = new ArrayList<>();

        this.fullHeartImage = new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_full.png").toExternalForm());
        this.halfHeartImage = new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_half.png").toExternalForm());

        // Bind health to UI
        healthProperty.addListener((obs, oldVal, newVal) -> updateHearts(newVal.intValue()));

        // Initial draw (if value already > 0)
        updateHearts(healthProperty.get());
    }

    private void updateHearts(int health) {
        heartsHbox.getChildren().clear();
        int full = health / 2;
        boolean hasHalf = health % 2 == 1;

        for (int i = 0; i < full; i++) {
            heartsHbox.getChildren().add(createHeartImage(fullHeartImage));
        }

        if (hasHalf) {
            heartsHbox.getChildren().add(createHeartImage(halfHeartImage));
        }
    }

    private ImageView createHeartImage(Image img) {
        ImageView view = new ImageView(img);
        view.setFitWidth(format);
        view.setFitHeight(format);
        return view;
    }
}