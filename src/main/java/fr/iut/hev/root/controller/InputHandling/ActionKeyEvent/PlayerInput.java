package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.entities.actor.PlayerMouvementsEnum;
import javafx.scene.input.KeyCode;

public class PlayerInput implements KeyCodeInterface {

    private final Player player = Player.getInstance(); // singleton

    @Override
    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case Q -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.addPlayerMouvements(PlayerMouvementsEnum.JUMP);
        }
    }

    @Override
    public void handleKeyReleased(KeyCode code) {
        switch (code) {
            case Q -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.removePlayerMouvements(PlayerMouvementsEnum.JUMP);
        }
    }
}
