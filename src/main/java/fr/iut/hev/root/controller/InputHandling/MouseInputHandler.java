package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.GlobalView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseInputHandler implements EventHandler<MouseEvent> {

    private TileMap tileMap;
    private GlobalView worldView;
    private Player player;

    public MouseInputHandler(TileMap tileMap,GlobalView worldView) {
        this.tileMap = tileMap;
        this.worldView = worldView;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (true) { //condition à rajouter
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
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
            }
        }
    }

    public void onLeftClickPressed() {
        if (true) { //condition lorsqu'on aura l'inventaire pour vérifier l'objet dans la main

        }
        else if (true) {

        }
    }

    public void onRightClickPressed () {

    }

    public void onLeftClickReleased() {

    }

    public void onRightClickReleased() {

    }
}
