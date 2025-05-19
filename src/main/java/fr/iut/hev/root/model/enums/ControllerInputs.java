package fr.iut.hev.root.model.enums;

public enum ControllerInputs {
    START(InputTypes.BUTTON),
    SELECT(InputTypes.BUTTON),

    BUTTON_RIGHT(InputTypes.BUTTON),
    BUTTON_LEFT(InputTypes.BUTTON),
    BUTTON_TOP(InputTypes.BUTTON),
    BUTTON_BOTTOM(InputTypes.BUTTON),

    DPAD_RIGHT(InputTypes.BUTTON),
    DPAD_LEFT(InputTypes.BUTTON),
    DPAD_UP(InputTypes.BUTTON),
    DPAD_DOWN(InputTypes.BUTTON),

    BUMPER_RIGHT(InputTypes.BUTTON),
    BUMPER_LEFT(InputTypes.BUTTON),

    TRIGGER_RIGHT(InputTypes.TRIGGER),
    TRIGGER_LEFT(InputTypes.TRIGGER),

    STICK_RIGHT_X(InputTypes.AXIS), // plutot RIGHT_STICK_X ?
    STICK_RIGHT_Y(InputTypes.AXIS),

    STICK_LEFT_X(InputTypes.AXIS),
    STICK_LEFT_Y(InputTypes.AXIS);

    private final InputTypes inputType;

    ControllerInputs(InputTypes inputType) {
        this.inputType = inputType;
    }
}
