
package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.entities.actor.PlayerMouvementsEnum;
import fr.iut.hev.root.view.InventoryView;
import javafx.scene.input.KeyCode;


/**
 * Gestionnaire des entrées clavier pour les mouvements et actions du joueur.
 * Implémente KeyCodeInterface pour le contexte de jeu (inventaire fermé).
 *
 * Contrôles :
 * - Q : Déplacement gauche
 * - D : Déplacement droite
 * - ESPACE : Saut
 * - E : Ouvrir l'inventaire
 */
public class PlayerInput implements KeyCodeInterface {
    private final Player player = Player.getInstance();
    private final InventoryView inventoryView;

    public PlayerInput(InventoryView inventoryView) {
        this.inventoryView = inventoryView;
    }

    /**
     * Ajoute le mouvement correspondant à la touche pressée.
     * Gère les déplacements continus et les actions uniques (saut).
     *
     * @param code Code de la touche pressée
     */
    @Override
    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case Q -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.addPlayerMouvements(PlayerMouvementsEnum.JUMP);
            case E -> {
                if(!inventoryView.getInventoryOpened())
                    inventoryView.setInventoryVisible();
            }
        }
    }

    /**
     * Retire le mouvement correspondant à la touche relâchée.
     *
     * @param code Code de la touche relâchée
     */
    @Override
    public void handleKeyReleased(KeyCode code) {
        switch (code) {
            case Q -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.removePlayerMouvements(PlayerMouvementsEnum.JUMP);
        }
    }
}