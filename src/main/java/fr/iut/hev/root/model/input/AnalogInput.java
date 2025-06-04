package fr.iut.hev.root.model.input;

import fr.iut.hev.root.model.enums.ControllerInputs;
import fr.iut.hev.root.model.enums.InputDevices;

public class AnalogInput extends Input {
    private final float threshold;
    private final Direction direction;

    public enum Direction { POSITIVE, NEGATIVE }

    public AnalogInput(InputDevices device, ControllerInputs axis, Direction direction, float threshold) {
        super(device, axis);
        this.direction = direction;
        this.threshold = threshold;
    }

    @Override
    public boolean isActive(InputListener listener) {
        float value = switch (super.getKey()) {
            case ControllerInputs.STICK_LEFT_X -> listener.getStickLeftX();
            case ControllerInputs.STICK_LEFT_Y -> listener.getStickLeftY();
            case ControllerInputs.STICK_RIGHT_X -> listener.getStickRightX();
            case ControllerInputs.STICK_RIGHT_Y -> listener.getStickRightY();
            default -> 0f;
        };

        return switch (direction) {
            case POSITIVE -> value > threshold;
            case NEGATIVE -> value < -threshold;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AnalogInput other)) return false;
        return super.getKey() == other.getKey() && direction == other.direction;
    }

    @Override
    public int hashCode() {
        return super.getKey().hashCode() * 31 + direction.hashCode();
    }
}
