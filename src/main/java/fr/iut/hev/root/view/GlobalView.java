package fr.iut.hev.root.view;

import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.craft.RecipesEnum;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.view.actor.MobView;
import fr.iut.hev.root.view.actor.PlayerView;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;

public class GlobalView {

    private TileMapView tileMapView;
    private PlayerView playerView;
    private LootView lootView;
    private ArrayList<MobView> mobViews;

    public GlobalView(TilePane landTileMap, TilePane backgroundTileMap, AnchorPane entitiesPane, HBox heartsBox, ListView<RecipesEnum> recipeView, Button craftButton, HBox recipeDisplay, GridPane hotbarInventory, GridPane expandedInventory, AnchorPane hudAnchorPane) {
        this.tileMapView = new TileMapView(TileMap.getInstance(),landTileMap,backgroundTileMap);
        this.playerView = new PlayerView(entitiesPane,heartsBox,recipeView,craftButton,recipeDisplay,hotbarInventory,expandedInventory,hudAnchorPane);
        this.lootView = new LootView(entitiesPane);
        this.mobViews = new ArrayList<>();
    }

    public void initMobViews(Camera camera, AnchorPane entitiesPane) {
        World world = World.getInstance();
        for (int i = 0; i < mobViews.size(); i++){
            createNPCView(world.getAliveMobs().get(i),camera,entitiesPane);

        }
    }

    private void createNPCView(Mob mob, Camera camera, AnchorPane entitiesPane) {

        mobViews.add(new MobView(mob,entitiesPane));
        mobViews.getLast().camOffsetXProperty().bind(camera.currentCamXProperty());
        mobViews.getLast().camOffsetYProperty().bind(camera.currentCamYProperty());
        //mob.healthProperty().addListener(new DeathListener(mob, mobView.getLast(), world.getAliveMobs(), lootManager));
    }

    public PlayerView getPlayerView() {return this.playerView;}
    public LootView getLootView() {return this.lootView;}
    public TileMapView getTileMapView() {return this.tileMapView;}
}
