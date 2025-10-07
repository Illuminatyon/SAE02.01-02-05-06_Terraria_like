package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.RecipesEnum;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.HeartsView;
import fr.iut.hev.root.view.HotbarView;
import fr.iut.hev.root.view.InventoryView;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PlayerView extends ActorView {

    private HeartsView heartsView;
    private CraftView craftView;
    private InventoryView inventoryView;
    private HotbarView hotbarView;

    public PlayerView(Player player, TileMap tileMap, AnchorPane anchorPane, HBox heartsHbox, ListView<RecipesEnum> recipeView, Button craftButton, HBox recipeDisplay, GridPane hotbarInventory, GridPane expandedInventory, AnchorPane hudAnchorPane) {
        super(player,tileMap,anchorPane);

        this.heartsView = new HeartsView(player.healthProperty(),heartsHbox);
        this.craftView = new CraftView(recipeView,,craftButton,recipeDisplay);
        this.inventoryView = new InventoryView(player.getInventory(),hotbarInventory,expandedInventory,hudAnchorPane,this.craftView);
        this.hotbarView = new HotbarView(hotbarInventory);
    }

    public HeartsView getHeartsView() {return this.heartsView;}
}
