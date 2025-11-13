
package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import javafx.scene.input.KeyCode;

/**
 * Interface de stratégie pour la gestion des entrées clavier.
 *
 * Utilise le pattern Strategy pour permettre un changement dynamique de comportement
 * selon le contexte (menu, jeu, inventaire, etc.).
 *
 * @see PlayerInput
 * @see InventoryInput
 * @see KeyInputHandler
 */
public interface KeyCodeInterface {
    /**
     * Gère une touche pressée.
     *
     * @param code Code de la touche pressée (ex: KeyCode.W, KeyCode.SPACE)
     */
    void handleKeyPressed(KeyCode code);

    /**
     * Gère une touche relâchée.
     *
     * @param code Code de la touche relâchée
     */
    void handleKeyReleased(KeyCode code);
}