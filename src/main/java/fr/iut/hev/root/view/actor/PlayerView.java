package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.controller.InputHandling.InputHandler;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.craft.RecipesEnum;
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

    public PlayerView(TileMap tileMap, AnchorPane anchorPane, HBox heartsHbox, ListView<RecipesEnum> recipeView, Button craftButton, HBox recipeDisplay, GridPane hotbarInventory, GridPane expandedInventory, AnchorPane hudAnchorPane, InputHandler inputHandler) {
        super(Player.getInstance(),tileMap,anchorPane);

        this.heartsView = new HeartsView(Player.getInstance().healthProperty(),heartsHbox);
        this.craftView = new CraftView(recipeView,Player.getInstance().getCraftingManager().getRecipesAvailable(),craftButton,recipeDisplay);
        this.inventoryView = new InventoryView(Player.getInstance().getInventory(),hotbarInventory,expandedInventory,hudAnchorPane,this.craftView,inputHandler);
        this.hotbarView = new HotbarView(hotbarInventory);
    }

    public HeartsView getHeartsView() {return this.heartsView;}
    public CraftView getCraftView() {return this.craftView;}
    public InventoryView getInventoryView() {return this.inventoryView;}
}
