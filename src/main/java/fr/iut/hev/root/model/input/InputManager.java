package fr.iut.hev.root.model.input;

import fr.iut.hev.root.model.enums.PlayerActions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InputManager {
    private final InputListener listener;
    private final Map<PlayerActions, Set<Input>> bindings = new HashMap<>();

    public InputManager(InputListener listener) {
        this.listener = listener;
    }

    public void bind(Input input, PlayerActions action) {
        bindings.computeIfAbsent(action, a -> new HashSet<>()).add(input);
    }

    public void unbind(PlayerActions action) {
        //
    }

    public Set<PlayerActions> getActiveActions() {
        Set<PlayerActions> activeActions = new HashSet<>();

        for (Map.Entry<PlayerActions, Set<Input>> entry : bindings.entrySet()) {
            PlayerActions action = entry.getKey();
            Set<Input> inputs = entry.getValue();

            boolean isActive = false;

            if (isActive) {
                activeActions.add(action);
            }
        }

        return activeActions;
    }
}
