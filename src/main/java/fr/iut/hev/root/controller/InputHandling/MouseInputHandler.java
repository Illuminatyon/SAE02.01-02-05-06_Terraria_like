package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import fr.iut.hev.root.view.GlobalView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.iut.hev.root.model.TileMap.format;

public class MouseInputHandler implements EventHandler<MouseEvent> {

    private TileMap tileMap;
    private GlobalView worldView;
    private Player player;
    private int x;
    private int y;
    private boolean mouseClickIsPressed;
    private MouseEvent mouseEvent;

    public MouseInputHandler(TileMap tileMap,GlobalView worldView,Player player) {
        this.tileMap = tileMap;
        this.worldView = worldView;
        this.player = player;
        this.mouseClickIsPressed = false;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            this.mouseClickIsPressed = true;
            this.mouseEvent = mouseEvent;
        }
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            mouseClickIsPressed = false;
            this.mouseEvent = null;
        }
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
            this.mouseEvent = mouseEvent;
        }
    }

    public void clickPressedHandler() {
        x = (int)mouseEvent.getX() / format;
        y = (int)mouseEvent.getY() / format;
        if (checkClickRelevent(x,y)) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickPressed();
            }
            else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                onRightClickPressed();
            }
        }
    }

    public void onLeftClickPressed() {
        if (false) { //condition lorsqu'on aura l'inventaire pour vérifier l'objet dans la main

        }
        else if (true) {
            player.updateBreaksBlock(x,y,tileMap);
            worldView.updateTile(x,y,tileMap.getTile(x,y));
        }
    }

    public void onRightClickPressed() {
        System.out.println("right click triggered");
    }

    public boolean checkClickRelevent(int x,int y) {
        return tileMap.getTile(x,y).getTile().getType() != TileTypes.AIR;
    }

    public boolean getMouseClickIsPressed() {return this.mouseClickIsPressed;}
}
