package fr.iut.hev.root.model;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SceneWrapper extends Scene {
    public SceneWrapper(@NamedArg("root") Parent root) {
        super(root);
    }

    public SceneWrapper(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root, width, height);
    }

    public SceneWrapper(@NamedArg("root") Parent root, @NamedArg(value="fill", defaultValue="WHITE") Paint fill) {
        super(root, fill);
    }

    public SceneWrapper(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height,
                 @NamedArg(value="fill", defaultValue="WHITE") Paint fill) {
        super(root, width, height, fill);
    }

    public SceneWrapper(@NamedArg("root") Parent root, @NamedArg(value="width", defaultValue="-1") double width, @NamedArg(value="height", defaultValue="-1") double height, @NamedArg("depthBuffer") boolean depthBuffer) {
        super(root, width, height, depthBuffer);
    }

    public SceneWrapper(@NamedArg("root") Parent root, @NamedArg(value="width", defaultValue="-1") double width, @NamedArg(value="height", defaultValue="-1") double height,
                        @NamedArg("depthBuffer") boolean depthBuffer,
                        @NamedArg(value="antiAliasing", defaultValue="DISABLED") SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
    }

    /* *************************************************************************
     *                                                                         *
     *                          Controller Handling                            *
     *                                                                         *
     **************************************************************************/

    private ObjectProperty<EventHandler<? super ControllerEvent>> onControllerButtonPressed;

    public final void setOnControllerButtonPressed(EventHandler<? super ControllerEvent> value) {
        onControllerButtonPressedProperty().set(value);
    }

    public final EventHandler<? super ControllerEvent> getOnControllerButtonPressed() {
        return onControllerButtonPressed == null ? null : onControllerButtonPressed.get();
    }

    public final ObjectProperty<EventHandler<? super ControllerEvent>> onControllerButtonPressedProperty() {
        if (onControllerButtonPressed == null) {
            onControllerButtonPressed = new ObjectPropertyBase<EventHandler<? super ControllerEvent>>() {
                @Override
                protected void invalidated() {
                    setEventHandler(ControllerEvent.BUTTON_PRESSED, get());
                }

                @Override
                public Object getBean() {
                    return super.get(); // this
                }

                @Override
                public String getName() {
                    return "onControllerButtonPressed";
                }
            };
        }
        return onControllerButtonPressed;
    }

    private ObjectProperty<EventHandler<? super ControllerEvent>> onControllerButtonReleased;

    public final void setOnControllerButtonReleased(EventHandler<? super ControllerEvent> value) {
        onControllerButtonReleasedProperty().set(value);
    }

    public final EventHandler<? super ControllerEvent> getOnControllerButtonReleased() {
        return onControllerButtonReleased == null ? null : onControllerButtonReleased.get();
    }

    public final ObjectProperty<EventHandler<? super ControllerEvent>> onControllerButtonReleasedProperty() {
        if (onControllerButtonReleased == null) {
            onControllerButtonReleased = new ObjectPropertyBase<EventHandler<? super ControllerEvent>>() {
                @Override
                protected void invalidated() {
                    setEventHandler(ControllerEvent.BUTTON_RELEASED, get());
                }

                @Override
                public Object getBean() {
                    return super.get(); // this
                }

                @Override
                public String getName() {
                    return "onControllerButtonReleased";
                }
            };
        }
        return onControllerButtonReleased;
    }
}
