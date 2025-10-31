package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.entities.actor.PlayerMouvementsEnum;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Gestionnaire des entrées clavier pour le jeu.
 * Cette classe est responsable de la capture et du traitement des événements clavier,
 * permettant au joueur d'interagir avec le jeu via le clavier.
 */
public class KeyInputHandler implements EventHandler<KeyEvent> {
    private Player player;
    private InventoryView inventoryView;
    private CraftView craftView;

    /**
     * Constructeur du gestionnaire d'entrées clavier.
     *
     *@param inventoryView Vue de l'inventaire du joueur
     * @param craftView Vue de l'interface de fabrication
     */
    public KeyInputHandler(InventoryView inventoryView, CraftView craftView) {
        this.player = Player.getInstance();
        this.inventoryView = inventoryView;
        this.craftView = craftView;
    }

    /**
     * Gère les événements clavier.
     * Cette méthode traite les événements de pression et de relâchement des touches,
     * et déclenche les actions correspondantes dans le jeu.
     *
     * @param keyEvent L'événement clavier à traiter
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            switch (keyEvent.getCode()) {
                // Traitement des touches enfoncées
                case KeyCode.Q -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
                case KeyCode.D -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
                case KeyCode.E -> inventoryView.setInventoryVisible();
                case KeyCode.R -> {
                    if (inventoryView.getInventoryOpened())
                        craftView.setCraftGUIVisible();
                }
                case KeyCode.SPACE -> player.addPlayerMouvements(PlayerMouvementsEnum.JUMP);
                //case KeyCode.TAB -> System.out.println("map opened"); // TODO: Finish this feature (open and close map)
                /*case KeyCode.Z -> {
                    player.receiveDamage(1);
                    System.out.println("pv = " + player.getHealth());
                }*/ // DEBUG feature: Remove health from player
            }
        } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
                case KeyCode.D -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
                case KeyCode.SPACE -> player.removePlayerMouvements(PlayerMouvementsEnum.JUMP);
            }
        }
    }
}
