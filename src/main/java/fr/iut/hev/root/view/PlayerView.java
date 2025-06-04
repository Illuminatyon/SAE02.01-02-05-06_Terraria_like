package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Actor;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.time.chrono.AbstractChronology;

public class PlayerView extends ActorView{

    public PlayerView(Player player, TileMap tileMap,AnchorPane anchorPane) {
        System.out.println(player);
        super(player,tileMap,anchorPane);
    }

    /*@Override


    public void deletePlayerSprite() {
        playerSprite.setVisible(false);
    }*/
}
