package fr.iut.hev.root.controller;

import com.studiohartman.jamepad.ControllerButton;
import fr.iut.hev.root.model.ControllerEvent;
import fr.iut.hev.root.model.SceneWrapper;
import fr.iut.hev.root.model.enums.InputDevices;
import fr.iut.hev.root.model.enums.PlayerActions;
import fr.iut.hev.root.model.input.Input;
import fr.iut.hev.root.model.input.InputListener;
import fr.iut.hev.root.model.input.InputManager;
import fr.iut.hev.root.model.input.KeyInput;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable {
    @FXML
    private AnchorPane root;

    private Set<PlayerActions> activeActions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                InputListener inputListener = new InputListener((SceneWrapper) newScene);
                InputManager inputManager = new InputManager(inputListener);

                Input input1 = new KeyInput(InputDevices.KEYBOARD, KeyCode.D);
                Input input2 = new KeyInput(InputDevices.KEYBOARD, KeyCode.Q);
                Input input5 = new KeyInput(InputDevices.KEYBOARD, KeyCode.SPACE);
                Input input6 = new KeyInput(InputDevices.CONTROLLER, ControllerButton.DPAD_DOWN);
                inputManager.bind(input1, PlayerActions.MOVE_RIGHT);
                inputManager.bind(input2, PlayerActions.MOVE_LEFT);
                inputManager.bind(input5, PlayerActions.JUMP);
                inputManager.bind(input6, PlayerActions.JUMP);
                new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        activeActions = inputManager.getActiveActions();
                        System.out.println(activeActions);
                    }
                }.start();
            }
        });
    }
}
