package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.layout.AnchorPane;

public class PlayerView extends ActorView {

    public PlayerView(Player player, TileMap tileMap,AnchorPane anchorPane) {
        super(player,tileMap,anchorPane);
    }


}
