package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
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

    public MouseInputHandler(TileMap tileMap,GlobalView worldView,Player player) {
        this.tileMap = tileMap;
        this.worldView = worldView;
        this.player = player;
        this.mouseClickIsPressed = false;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        /*if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED) || mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            x = (int)mouseEvent.getX();
            y = (int)mouseEvent.getY();
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickPressed();
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                onRightClickPressed();
            }
        } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickReleased();
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                onRightClickReleased();
            }
        }*/

        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            mouseClickIsPressed = true;
            System.out.println("entered MOUSE_PRESSED");
        }
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            mouseClickIsPressed = false;
            System.out.println("entered MOUSE_RELEASED");
        }

        while (mouseClickIsPressed) {
            x = (int)mouseEvent.getX();
            y = (int)mouseEvent.getY();

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickPressed();
            }
        }
    }

    public void onLeftClickPressed() {
        if (false) { //condition lorsqu'on aura l'inventaire pour vérifier l'objet dans la main

        }
        else if (true) {
            int xModel = x/format,yModel = y/format;
            player.updateBreaksBlock(xModel,yModel,tileMap);
            worldView.updateTile(xModel,yModel,tileMap.getTile(xModel,yModel));
        }
    }

    public void onRightClickPressed() {

    }

    public void onLeftClickReleased() {

    }

    public void onRightClickReleased() {

    }

    public void setMouseClickIsPressed(boolean mouseClickIsPressed) {this.mouseClickIsPressed = mouseClickIsPressed;}
}
