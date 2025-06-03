package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.ImageView;

public class PlayerView {
    private Player player;
    private ImageView playerSprite;
    private TileMap tileMap;


    public PlayerView(Player player,ImageView playerSprite,TileMap tileMap) {
        this.player = player;
        this.playerSprite = playerSprite;
        this.tileMap = tileMap;
    }

    public void load() {
        playerSprite.setLayoutX((double) (tileMap.getWidth() * TileMap.format) / 2);
        playerSprite.setLayoutY((double) (tileMap.getHeight() * TileMap.format) / 2);
        playerSprite.translateXProperty().bind(player.posXProperty());
        playerSprite.translateYProperty().bind(player.posYProperty());
        playerSprite.scaleXProperty().bind(player.lookDirectionProperty());
    }

    public void deletePlayerSprite() {
        playerSprite.setVisible(false);
    }
}
