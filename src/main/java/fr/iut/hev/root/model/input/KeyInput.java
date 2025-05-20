package fr.iut.hev.root.model.input;

import com.studiohartman.jamepad.ControllerButton;
import fr.iut.hev.root.model.enums.InputDevices;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.Objects;

public class KeyInput extends Input {
    public KeyInput(InputDevices device, Object key) {
        super(device, key);
    }

    @Override
    public boolean isActive(InputListener listener) {
        try {
            return switch (super.getDevice()) {
                case KEYBOARD -> listener.isKeyPressed((KeyCode) super.getKey());
                case MOUSE -> listener.isMouseButtonPressed((MouseButton) super.getKey());
                //case CONTROLLER -> listener.isControllerInputActive((ControllerInput) super.getKey());
                case CONTROLLER -> listener.isControllerButtonPressed((ControllerButton) super.getKey());
            };
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KeyInput other)) return false;
        return super.getDevice() == other.getDevice() && super.getKey().equals(other.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getDevice(), super.getKey());
    }
}
