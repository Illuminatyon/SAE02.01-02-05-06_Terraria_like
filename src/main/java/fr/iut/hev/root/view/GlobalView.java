package fr.iut.hev.root.view;

import fr.iut.hev.root.model.craft.RecipesEnum;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.view.actor.PlayerView;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

public class GlobalView {

    private TileMapView tileMapView;
    private PlayerView playerView;
    private LootView lootView;

    public GlobalView(TilePane landTileMap, TilePane backgroundTileMap, AnchorPane entitiesPane, HBox heartsBox, ListView<RecipesEnum> recipeView, Button craftButton, HBox recipeDisplay, GridPane hotbarInventory, GridPane expandedInventory, AnchorPane hudAnchorPane) {
        this.tileMapView = new TileMapView(TileMap.getInstance(),landTileMap,backgroundTileMap);
        this.playerView = new PlayerView(TileMap.getInstance(),entitiesPane,heartsBox,recipeView,craftButton,recipeDisplay,hotbarInventory,expandedInventory,hudAnchorPane);
        this.lootView = new LootView(entitiesPane);
    }

    public PlayerView getPlayerView() {return this.playerView;}
    public LootView getLootView() {return this.lootView;}
    public TileMapView getTileMapView() {return this.tileMapView;}
}
