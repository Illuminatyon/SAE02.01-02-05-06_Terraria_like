package fr.iut.hev.root.model.input;

import fr.iut.hev.root.model.enums.InputDevices;

public abstract class Input {
    private final InputDevices device;
    private final Object key;

    public Input(InputDevices device, Object key) {
        this.device = device;
        this.key = key;
    }

    public InputDevices getDevice() {
        return this.device;
    }

    public Object getKey() {
        return this.key;
    }

    public abstract boolean isActive(InputListener listener);
}
