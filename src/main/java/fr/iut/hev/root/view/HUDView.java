package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HUDView {

    private Player player;
    private HBox heartsHbox;

    public HUDView(Player player,HBox heartsHbox) {
        this.player = player;
        this.heartsHbox = heartsHbox;
    }

    public void updateHealth() {
        double nbHearts = player.getHealth() / 2.0;
        int loopIteration = (int) nbHearts;
        int i = 0;

        for (i = 0; i < loopIteration; i++) {
            heartsHbox.getChildren().get(i).
        }
    }
}
