package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import javafx.scene.input.KeyCode;

public interface KeyCodeInterface {

    void handleKeyPressed(KeyCode code);
    void handleKeyReleased(KeyCode code);

}
