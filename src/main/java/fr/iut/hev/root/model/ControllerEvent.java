package fr.iut.hev.root.model;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;

public final class ControllerEvent extends InputEvent {
    public static final javafx.event.EventType<ControllerEvent> ANY = new EventType<ControllerEvent>(InputEvent.ANY);
    public static final javafx.event.EventType<ControllerEvent> BUTTON_PRESSED = new EventType<ControllerEvent>(InputEvent.ANY, "BUTTON_PRESSED");
    public static final javafx.event.EventType<ControllerEvent> BUTTON_RELEASED = new EventType<ControllerEvent>(InputEvent.ANY, "BUTTON_RELEASED");
    public static final javafx.event.EventType<ControllerEvent> AXIS_USED = new EventType<ControllerEvent>(InputEvent.ANY, "AXIS_USED");
    private final ControllerButton button;
    private final ControllerAxis axis;

    public ControllerEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<ControllerEvent> eventType, @NamedArg("button") ControllerButton button, @NamedArg("axis") ControllerAxis axis) {
        super(source, target, eventType);
        boolean isButtonPressed = eventType == BUTTON_PRESSED;
        boolean isAxisUsed = eventType == AXIS_USED;

        this.button = isButtonPressed ? null : button;
        this.axis = isAxisUsed ? null : axis;
    }

    public final ControllerButton getButton() {
        return button;
    }

    public final ControllerAxis getAxis() {
        return axis;
    }

    @Override
    public java.lang.String toString() {
        final StringBuilder sb = new StringBuilder("KeyEvent [");

        sb.append("source = ").append(getSource());
        sb.append(", target = ").append(getTarget());
        sb.append(", eventType = ").append(getEventType());
        sb.append(", consumed = ").append(isConsumed());

        sb.append(", button = ").append(getButton());
        sb.append(", axis = ").append(getAxis());

        return sb.append("]").toString();
    }

    @Override
    public ControllerEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ControllerEvent) super.copyFor(newSource, newTarget);
    }

    public ControllerEvent copyFor(Object source, EventTarget target, EventType<ControllerEvent> type) {
        ControllerEvent e = copyFor(source, target);
        e.eventType = type;
        return e;
    }

    @Override
    public EventType<ControllerEvent> getEventType() {
        return (EventType<ControllerEvent>) super.getEventType();
    }
}
