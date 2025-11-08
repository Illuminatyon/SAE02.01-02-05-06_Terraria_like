
package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import javafx.scene.input.KeyCode;

/**
 * <h2>Interface de stratégie pour la gestion des entrées clavier</h2>
 *
 * <p>Cette interface définit le contrat pour les gestionnaires d'entrées clavier
 * en utilisant le <strong>Strategy Pattern</strong>. Elle permet de basculer dynamiquement
 * entre différents comportements selon le contexte du jeu (menu, jeu, inventaire, etc.).</p>
 *
 * <p><strong>Avantages du pattern :</strong></p>
 * <ul>
 *   <li>Séparation des responsabilités : chaque contexte a sa propre classe</li>
 *   <li>Facilité d'extension : ajout de nouveaux comportements sans modifier le code existant</li>
 *   <li>Changement dynamique : le comportement peut changer pendant l'exécution</li>
 *   <li>Testabilité : chaque stratégie peut être testée indépendamment</li>
 * </ul>
 *
 * <p><strong>Implémentations existantes :</strong></p>
 * <ul>
 *   <li>{@link PlayerInput} : Gestion des mouvements et actions du joueur</li>
 *   <li>{@link InventoryInput} : Gestion de l'inventaire et du craft</li>
 * </ul>
 *
 * <p><strong>Exemple d'utilisation :</strong></p>
 * <pre>{@code
 * KeyCodeInterface currentStrategy;
 * if (inventoryOpen) {
 *     currentStrategy = new InventoryInput(inventoryView, craftView);
 * } else {
 *     currentStrategy = new PlayerInput();
 * }
 * currentStrategy.handleKeyPressed(KeyCode.E);
 * }</pre>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see PlayerInput
 * @see InventoryInput
 * @see KeyInputHandler
 * @since 1.0
 */
public interface KeyCodeInterface {

    /**
     * <h3>Gestion des touches pressées</h3>
     *
     * <p>Méthode appelée lorsqu'une touche du clavier est pressée.
     * Chaque implémentation définit son propre comportement pour
     * traiter les différentes touches.</p>
     *
     * @param code Le code de la touche pressée (ex: KeyCode.W, KeyCode.SPACE, etc.)
     * @see javafx.scene.input.KeyCode
     * @see javafx.scene.input.KeyEvent#KEY_PRESSED
     */
    void handleKeyPressed(KeyCode code);

    /**
     * <h3>Gestion des touches relâchées</h3>
     *
     * <p>Méthode appelée lorsqu'une touche du clavier est relâchée.
     * Utile pour gérer les mouvements continus (arrêter le déplacement
     * quand la touche est relâchée) ou les actions qui durent.</p>
     *
     * @param code Le code de la touche relâchée
     * @see javafx.scene.input.KeyCode
     * @see javafx.scene.input.KeyEvent#KEY_RELEASED
     */
    void handleKeyReleased(KeyCode code);
}