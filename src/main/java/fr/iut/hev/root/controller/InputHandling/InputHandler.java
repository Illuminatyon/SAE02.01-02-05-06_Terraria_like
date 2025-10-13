package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.view.*;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

public class InputHandler {

    private KeyInputHandler keyInputHandler;
    private MouseInventoryInputHandler mouseInventoryInputHandler;
    private MouseItemActionInputHandler mouseItemActionInputHandler;
    private ScrollInputHandler scrollInputHandler;

    public InputHandler(InventoryView inventoryView, CraftView craftView, Camera camera, GlobalView globalView, HotbarView hotbarView) {
        this.keyInputHandler = new KeyInputHandler(inventoryView,craftView);
        this.mouseInventoryInputHandler = new MouseInventoryInputHandler(Player.getInstance().getInventory(), inventoryView);
        this.mouseItemActionInputHandler = new MouseItemActionInputHandler(camera,inventoryView,globalView);
        this.scrollInputHandler = new ScrollInputHandler(Player.getInstance().getInventory(),hotbarView,inventoryView);
    }

    private void initScrollInputHandler() {
        Player player = Player.getInstance();
        player.itemInHandProperty().bindBidirectional(scrollInputHandler.onHandItemProperty());
        player.quantityOfItemInHandProperty().bindBidirectional(scrollInputHandler.quantityProperty());
        player.itemInHandProperty().addListener((observableValue, item, t1) -> mouseItemActionInputHandler.updateCooldown());

        scrollInputHandler.directionProperty().addListener((observableValue, number, t1) -> {
            if (scrollInputHandler.getDirection() != 0)
                scrollInputHandler.updateHotbar();
        });
    }

    public void initInputHandler(TilePane landTileMap, AnchorPane hudAnchorPane) {
        initScrollInputHandler();
        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED,mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseItemActionInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryInputHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollInputHandler);
        });
    }

    public KeyInputHandler getKeyInputHandler() {return this.keyInputHandler;}
    public MouseInventoryInputHandler getMouseInventoryInputHandler() {return this.mouseInventoryInputHandler;}
    public MouseItemActionInputHandler getMouseItemActionInputHandler() {return this.mouseItemActionInputHandler;}
    public ScrollInputHandler getScrollInputHandler() {return this.scrollInputHandler;}
}
