package fr.iut.hev.root.model.input;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import fr.iut.hev.root.model.enums.ControllerInputs;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class InputListener {
    private Set<KeyCode> activeKeys = new HashSet<>();
    private Set<MouseButton> activeMouseButtons = new HashSet<>();
    private EnumMap<ControllerInputs, Boolean> controllerStates = new EnumMap<>(ControllerInputs.class);

    private ControllerManager controllerManager;

    private float stickRightX;
    private float stickRightY;
    private float stickLeftX;
    private float stickLeftY;

    public InputListener(Scene scene) {
        handleKeyPress(scene);
        handleMousePress(scene);
        handleControllerInput();
    }

    private void handleKeyPress(Scene scene) {
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    private void handleMousePress(Scene scene) {
        scene.setOnMousePressed(e -> activeMouseButtons.add(e.getButton()));
        scene.setOnMouseReleased(e -> activeMouseButtons.remove(e.getButton()));
    }

    private void handleControllerInput() {
        controllerManager = new ControllerManager();
        controllerManager.initSDLGamepad();

        Thread controllerThread = new Thread(() -> {
            while (true) {
                ControllerState state = controllerManager.getState(0); // Controller 0
                if (state.isConnected) {
                    for (ControllerInputs input : ControllerInputs.values()) {
                        boolean isActive = checkControllerInput(state, input);
                        controllerStates.put(input, isActive);
                    }

                    stickRightX = state.rightStickX;
                    stickRightY = state.rightStickY;
                    stickLeftX = state.leftStickX;
                    stickLeftY = state.leftStickY;
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ignored) {}
            }
        });

        controllerThread.setDaemon(true);
        controllerThread.start();
    }

    private boolean checkControllerInput(ControllerState state, ControllerInputs input) {
        return switch (input) {
            case START -> state.start;
            case SELECT -> state.back;
            case BUTTON_BOTTOM -> state.a;
            case BUTTON_RIGHT -> state.b;
            case BUTTON_LEFT -> state.x;
            case BUTTON_TOP -> state.y;
            case BUMPER_LEFT -> state.lb;
            case BUMPER_RIGHT -> state.rb;
            case DPAD_UP -> state.dpadUp;
            case DPAD_DOWN -> state.dpadDown;
            case DPAD_LEFT -> state.dpadLeft;
            case DPAD_RIGHT -> state.dpadRight;
            case TRIGGER_LEFT -> state.leftTrigger > 0.5f; // Trigger deadzone
            case TRIGGER_RIGHT -> state.rightTrigger > 0.5f;
            case STICK_LEFT_X -> Math.abs(state.leftStickX) > 0.3f; // Joystick deadzone
            case STICK_LEFT_Y -> Math.abs(state.leftStickY) > 0.3f;
            case STICK_RIGHT_X -> Math.abs(state.rightStickX) > 0.3f;
            case STICK_RIGHT_Y -> Math.abs(state.rightStickY) > 0.3f;
        };
    }

    public boolean isKeyPressed(KeyCode key) {
        return activeKeys.contains(key);
    }

    public boolean isMouseButtonPressed(MouseButton button) {
        return activeMouseButtons.contains(button);
    }

    public boolean isControllerInputActive(ControllerInputs input) {
        return controllerStates.getOrDefault(input, false);
    }

    public float getStickRightX() {
        return stickRightX;
    }

    public float getStickRightY() {
        return stickRightY;
    }

    public float getStickLeftX() {
        return stickLeftX;
    }

    public float getStickLeftY() {
        return stickLeftY;
    }

    public void stop() {
        controllerManager.quitSDLGamepad();
    }
}
