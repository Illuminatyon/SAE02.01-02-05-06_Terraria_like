package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.HeartsView;
import fr.iut.hev.root.view.HotbarView;
import fr.iut.hev.root.view.InventoryView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PlayerView extends ActorView {

    private HeartsView hudview;
    private CraftView craftView;
    private InventoryView inventoryView;
    private HotbarView hotbarView;

    public PlayerView(Player player, TileMap tileMap, AnchorPane anchorPane, HBox heartsHbox) {
        super(player,tileMap,anchorPane);

        this.hudview = new HeartsView(player.healthProperty(),heartsHbox);
    }

}
