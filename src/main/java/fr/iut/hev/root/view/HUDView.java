package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

import static fr.iut.hev.root.model.TileMap.format;

public class HUDView {

    private HBox heartsHbox;
    private ArrayList<ImageView> fullHearts;
    private ImageView halfHeart;
    private Player player;

    public HUDView(int health, HBox heartsHbox) {
        this.heartsHbox = heartsHbox;
        this.fullHearts = new ArrayList<>();
        initFullHearts(health);
        this.halfHeart = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_half.png").toExternalForm()));
        setFit();
    }

    public HUDView(Player player, HBox heartsHbox) {
        this.player = player;
        this.heartsHbox = heartsHbox;
        this.fullHearts = new ArrayList<>();
        initFullHearts(player.getMaxHealth());
        this.halfHeart = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_half.png").toExternalForm()));
        setFit();
    }

    public void updateHealth(Number playerHealth) {
        ImageView heart;
        int health = playerHealth.intValue();
        double nbHearts = health / 2.0;
        int loopIteration;

        // If player is set, check if max health has changed and update fullHearts if needed
        if (player != null) {
            int maxHealth = player.getMaxHealth();
            if (fullHearts.size() < maxHealth / 2) {
                updateMaxHealth(maxHealth);
            }
        }

        heartsHbox.getChildren().clear();
        if (health > 0) {
            loopIteration = (int) nbHearts;
            if (nbHearts % 1 > 0)
                loopIteration += 1;
            for (int i = 0; i < loopIteration; i++) {
                if (nbHearts % 1 > 0 && i == loopIteration - 1)
                    heart = halfHeart;
                else
                    heart = fullHearts.get(i);
                heartsHbox.getChildren().add(heart);
            }
        }
    }

    public void updateMaxHealth(int maxHealth) {
        // Clear existing hearts
        fullHearts.clear();

        // Reinitialize with new max health
        initFullHearts(maxHealth);
    }

    public void setFit() {
        halfHeart.setFitHeight(format);
        halfHeart.setFitWidth(format);
    }

    public void initFullHearts(int health) {
        ImageView heart;
        for (int i = 0 ; i < health/2 ; i++) {
            heart = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_full.png").toExternalForm()));
            heart.setFitHeight(format);
            heart.setFitWidth(format);
            fullHearts.add(heart);
        }
    }
}
