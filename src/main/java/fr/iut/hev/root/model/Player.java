package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ControllerInputs;
import fr.iut.hev.root.model.enums.InputDevices;
import fr.iut.hev.root.model.enums.PlayerActions;
import fr.iut.hev.root.model.input.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.Set;

public class Player extends Actor {
    private InputListener inputListener;
    private InputManager inputManager;
    private Set<PlayerActions> activeActions;

    public Player(Scene scene, int posX, int posY, int moveSpeed, int jumpForce) {
        super(posX, posY, moveSpeed, jumpForce);
        inputListener = new InputListener(scene);
        inputManager = new InputManager(inputListener);

        Input input1 = new KeyInput(InputDevices.KEYBOARD, KeyCode.D);
        Input input2 = new KeyInput(InputDevices.KEYBOARD, KeyCode.Q);
        Input input3 = new AnalogInput(InputDevices.CONTROLLER, ControllerInputs.STICK_LEFT_X, AnalogInput.Direction.POSITIVE, 0.3f);
        Input input4 = new AnalogInput(InputDevices.CONTROLLER, ControllerInputs.STICK_LEFT_X, AnalogInput.Direction.NEGATIVE, 0.3f);
        inputManager.bind(input1, PlayerActions.MOVE_RIGHT);
        inputManager.bind(input2, PlayerActions.MOVE_LEFT);
        inputManager.bind(input3, PlayerActions.MOVE_RIGHT);
        inputManager.bind(input4, PlayerActions.MOVE_LEFT);

        AnimationTimer inputTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                activeActions = inputManager.getActiveActions();
            }
        };
        inputTimer.start();
    }

    public void updateMovements() {
        //super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());

        super.posXProperty().set(super.posXProperty().get() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().get() + super.getVelocityY() * super.getMoveSpeed());

        //playerCollision.checkCollision() // A rename
        /*
        If is on ground then
            velocityY = 0
         */

        updateHorizontalMovements();
        //System.out.println("x : " + super.posXProperty().get());
    }

    private void updateHorizontalMovements() {
        if (inputManager.getActiveActions().size() > 1) {
            super.setVelocityX(0);
        } else if (inputManager.getActiveActions().contains(PlayerActions.MOVE_RIGHT)) {
            super.setVelocityX(super.getMoveSpeed());
        } else if (inputManager.getActiveActions().contains(PlayerActions.MOVE_LEFT)) {
            super.setVelocityX(-super.getMoveSpeed());
        } else {
            super.setVelocityX(0);
        }
    }
}
