package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import fr.iut.hev.root.view.GlobalView;
import fr.iut.hev.root.view.InventoryView;
import fr.iut.hev.root.view.MouseCursorCircleView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.iut.hev.root.model.TileMap.format;

public class MouseGameInputHandler implements EventHandler<MouseEvent> {

    private TileMap tileMap;
    private GlobalView worldView;
    private Player player;
    private int x;
    private int y;
    private boolean mouseClickIsPressed;
    private boolean mouseClickIsReleased;
    private MouseEvent mouseEvent;
    private MouseCursorCircleView mouseCursor;
    private InventoryView inventoryView;

    public MouseGameInputHandler(TileMap tileMap, GlobalView worldView, Player player, MouseCursorCircleView mouseCursor,InventoryView inventoryView) {
        this.tileMap = tileMap;
        this.worldView = worldView;
        this.player = player;
        this.mouseClickIsPressed = false;
        this.mouseClickIsReleased = false;
        this.mouseCursor = mouseCursor;
        this.inventoryView = inventoryView;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!inventoryView.getInventoryOpened()) {
            mouseCursor.handleMouseMove(mouseEvent);
            x = (int) mouseCursor.getCursorX() / format;
            y = (int) mouseCursor.getCursorY() / format;
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                this.mouseClickIsPressed = true;
                this.mouseEvent = mouseEvent;
            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                mouseClickIsPressed = false;
                mouseClickIsReleased = true;
            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                this.mouseEvent = mouseEvent;
            }
        }
    }

    public void clickPressedHandler() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickPressed();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickPressed();
        }
    }

    public void clickReleasedHandler() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickReleased();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickReleased();
        }
    }

    public void onLeftClickPressed() {
        if (false) {

        }
        else if (checkTileNotEmpty()) {
            player.updateBreaksBlock(x,y,tileMap);
            worldView.updateTile(x,y,tileMap.getTile(x,y));
        }
    }

    public void onRightClickPressed() {
        System.out.println("right click triggered");
    }

    public void onLeftClickReleased() {
        if (checkTileNotEmpty()) {
            tileMap.getTile(x,y).resetHealth();
            worldView.updateTile(x,y,tileMap.getTile(x,y));
        }
        mouseClickIsReleased = false;
    }

    public void onRightClickReleased() {
        System.out.println("right click released");
    }

    public boolean checkTileNotEmpty() {
        return tileMap.getTile(x,y).getTile().getType() != TileTypes.AIR;
    }

    public boolean getMouseClickIsPressed() {return this.mouseClickIsPressed;}

    public boolean getMouseClickIsReleased() {return this.mouseClickIsReleased;}

}
