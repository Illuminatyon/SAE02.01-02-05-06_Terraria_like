package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static fr.iut.hev.root.model.TileMap.format;

public class HUDView {

    private Player player;
    private HBox heartsHbox;

    public HUDView(Player player,HBox heartsHbox) {
        this.player = player;
        this.heartsHbox = heartsHbox;
    }

    public void updateHealth() {
        ImageView heart;
        double nbHearts = player.getHealth() / 2.0;
        int loopIteration;

        heartsHbox.getChildren().clear();
        if (player.getIsAlive()) {
            if (nbHearts % 1 > 0)
                loopIteration = (int) nbHearts + 1;
            else
                loopIteration = (int) nbHearts;
            for (int i = 0; i < loopIteration; i++) {
                if (nbHearts % 1 > 0 && i == loopIteration - 1)
                    heart = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_half.png").toExternalForm()));
                else
                    heart = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/heart_full.png").toExternalForm()));
                heart.setFitHeight(format);
                heart.setFitWidth(format);
                heartsHbox.getChildren().add(heart);
            }
        }
    }
}
