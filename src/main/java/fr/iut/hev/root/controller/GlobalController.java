package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.FixedAnimationTimer;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.GlobalView;
import fr.iut.hev.root.view.InventoryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.Node;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {

    private Player player;
    private Inventory inventory;

    @FXML private TilePane backgroundTileMap;
    @FXML private TilePane tileMap;
    @FXML private ImageView player_imageview;
    @FXML private GridPane hotbarInventory;
    @FXML private GridPane expandedInventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TileMap tilemap = new TileMap(1920,1056);
        GlobalView vue = new GlobalView(tilemap,tileMap,backgroundTileMap);
        player_imageview.setLayoutX((double) (tilemap.getWidth() * TileMap.format) / 2);
        player_imageview.setLayoutY((double) (tilemap.getHeight() * TileMap.format) / 2);
        vue.loadWorld();
        player_imageview.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                player = new Player(newScene, tilemap, 0, -25, 32, 64, 2, 10);
                player_imageview.translateXProperty().bind(player.posXProperty());
                player_imageview.translateYProperty().bind(player.posYProperty());
                player_imageview.scaleXProperty().bind(player.lookDirectionProperty());

                new FixedAnimationTimer() {
                    @Override
                    protected void update() {
                        player.updateMovements();
                    }
                }.start();
            }
        });

    }

    /*@FXML
    void openInventory(KeyEvent event) {
        System.out.println("event");
        if (Objects.equals(event.getCharacter(), "e") || Objects.equals(event.getCharacter(), "E")) {
            inventoryGridPane.setVisible(!inventoryGridPane.isVisible());
        }
    }*/

    private void manageInventory() {
        inventory = new Inventory();
        InventoryView inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory);
        for (Node cell : hotbarInventory.getChildren()) {
            int colIndex = GridPane.getColumnIndex(cell);
            int rowIndex = GridPane.getRowIndex(cell);
            ((ImageView) cell).imageProperty().bind();
        }
    }
}
