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
    private boolean isJumping;
    private int jumpingTestDecay;

    public Player(Scene scene, TileMap tileMap, int posX, int posY, int width, int height, int moveSpeed, int jumpForce) {
        super(tileMap, posX, posY, width, height, moveSpeed, jumpForce);
        inputListener = new InputListener(scene);
        inputManager = new InputManager(inputListener);

        Input input1 = new KeyInput(InputDevices.KEYBOARD, KeyCode.D);
        Input input2 = new KeyInput(InputDevices.KEYBOARD, KeyCode.Q);
        Input input3 = new AnalogInput(InputDevices.CONTROLLER, ControllerInputs.STICK_LEFT_X, AnalogInput.Direction.POSITIVE, 0.3f);
        Input input4 = new AnalogInput(InputDevices.CONTROLLER, ControllerInputs.STICK_LEFT_X, AnalogInput.Direction.NEGATIVE, 0.3f);
        Input input5 = new KeyInput(InputDevices.KEYBOARD, KeyCode.SPACE);
        Input input6 = new KeyInput(InputDevices.CONTROLLER, ControllerInputs.BUTTON_BOTTOM);
        inputManager.bind(input1, PlayerActions.MOVE_RIGHT);
        inputManager.bind(input2, PlayerActions.MOVE_LEFT);
        inputManager.bind(input3, PlayerActions.MOVE_RIGHT);
        inputManager.bind(input4, PlayerActions.MOVE_LEFT);
        inputManager.bind(input5, PlayerActions.JUMP);
        inputManager.bind(input6, PlayerActions.JUMP);

        AnimationTimer inputTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                activeActions = inputManager.getActiveActions();
            }
        };
        inputTimer.start();
    }

    public void updateMovements() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !isJumping) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }

        updateHorizontalMovements();
        updateVerticalMovements();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }

    private void updateHorizontalMovements() {
        // Code pas propre a nettoyer
        if (inputManager.getActiveActions().contains(PlayerActions.MOVE_RIGHT)
                && inputManager.getActiveActions().contains(PlayerActions.MOVE_LEFT)) {
            super.setVelocityX(0);
        } else if (inputManager.getActiveActions().contains(PlayerActions.MOVE_RIGHT)) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else if (inputManager.getActiveActions().contains(PlayerActions.MOVE_LEFT)) {
            super.setLookDirection(LookDirections.LEFT); // IL FAUT JUSTE FIX LE LEFT COLLIDER
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else {
            super.setVelocityX(0);
        }
    }

    private void updateVerticalMovements() {
        if (activeActions.contains(PlayerActions.JUMP) && super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !isJumping) {
            isJumping = true;
            jumpingTestDecay = 0;
        } else if (isJumping) {
            if (jumpingTestDecay == super.getJumpForce()) {
                super.setVelocityY(0);
                isJumping = false;
            } else {
                super.setVelocityY(-super.getJumpForce() + jumpingTestDecay);
                jumpingTestDecay += 1;
            }
        }
    }
}
