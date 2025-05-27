package fr.iut.hev.root.controller.InputHandling;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseClickReleasedHandler implements EventHandler<MouseEvent> {

    private MouseInputHandler mouseInputHandler;

    public MouseClickReleasedHandler(MouseInputHandler mouseInputHandler) {
        this.mouseInputHandler = mouseInputHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            mouseInputHandler.setMouseClickIsPressed(false);
        }
    }
}
