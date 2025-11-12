
package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.InventoryInput;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.KeyCodeInterface;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.PlayerInput;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * <h2>Gestionnaire principal des entrées clavier</h2>
 *
 * <p>Cette classe implémente le pattern <strong>Strategy</strong> pour gérer dynamiquement
 * les entrées clavier selon le contexte du jeu. Elle bascule automatiquement entre
 * différentes stratégies en fonction de l'état de l'interface (inventaire ouvert/fermé).</p>
 *
 * <p><strong>Fonctionnement :</strong></p>
 * <ul>
 *   <li><strong>Inventaire fermé :</strong> Utilise {@link PlayerInput} pour les mouvements</li>
 *   <li><strong>Inventaire ouvert :</strong> Utilise {@link InventoryInput} pour la navigation</li>
 * </ul>
 *
 * <p><strong>Architecture :</strong></p>
 * <pre>
 * KeyInputHandler (Context)
 *    ├── playerStrategy (PlayerInput)
 *    └── inventoryStrategy (InventoryInput)
 *         └── currentStrategy → Change dynamiquement
 * </pre>
 *
 * <p><strong>Avantages :</strong></p>
 * <ul>
 *   <li>Pas de conflit entre les actions du joueur et celles de l'inventaire</li>
 *   <li>Code modulaire et facilement extensible</li>
 *   <li>Changement de contexte transparent pour l'utilisateur</li>
 * </ul>
 *
 * <p><strong>Exemple d'utilisation :</strong></p>
 * <pre>{@code
 * KeyInputHandler handler = new KeyInputHandler(inventoryView, craftView);
 * scene.addEventHandler(KeyEvent.ANY, handler);
 * // Les événements clavier seront automatiquement routés vers la bonne stratégie
 * }</pre>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see KeyCodeInterface
 * @see PlayerInput
 * @see InventoryInput
 * @see EventHandler
 * @since 1.0
 */
public class KeyInputHandler implements EventHandler<KeyEvent> {

    /**
     * Instance singleton du joueur.
     */
    private final Player player = Player.getInstance();

    /**
     * Vue de l'inventaire pour vérifier son état (ouvert/fermé).
     */
    private final InventoryView inventoryView;

    /**
     * Vue de l'interface de craft.
     */
    private final CraftView craftView;

    /**
     * Stratégie utilisée quand le joueur est en mode jeu normal.
     */
    private final KeyCodeInterface playerStrategy;

    /**
     * Stratégie utilisée quand l'inventaire est ouvert.
     */
    private final KeyCodeInterface inventoryStrategy;

    /**
     * Stratégie actuellement active (référence vers playerStrategy ou inventoryStrategy).
     */
    private KeyCodeInterface currentStrategy;

    /**
     * Constructeur du gestionnaire d'entrées clavier.
     *
     * <p>Initialise les deux stratégies (joueur et inventaire) et définit
     * la stratégie joueur comme stratégie par défaut au démarrage.</p>
     *
     * @param inventoryView La vue de l'inventaire pour vérifier son état
     * @param craftView La vue de l'interface de craft
     * @throws NullPointerException si un des paramètres est null
     */
    public KeyInputHandler(InventoryView inventoryView, CraftView craftView) {
        this.inventoryView = inventoryView;
        this.craftView = craftView;
        this.playerStrategy = new PlayerInput(inventoryView);
        this.inventoryStrategy = new InventoryInput(inventoryView, craftView);
        this.currentStrategy = playerStrategy;
    }

    /**
     * <h3>Gestion des événements clavier</h3>
     *
     * <p>Point d'entrée principal pour tous les événements clavier du jeu.
     * Cette méthode effectue les opérations suivantes :</p>
     *
     * <ol>
     *   <li><strong>Sélection de la stratégie :</strong> Choisit entre playerStrategy
     *       et inventoryStrategy selon l'état de l'inventaire</li>
     *   <li><strong>Dispatching :</strong> Route l'événement vers la bonne méthode
     *       (handleKeyPressed ou handleKeyReleased) de la stratégie active</li>
     * </ol>
     *
     * <p><strong>Types d'événements traités :</strong></p>
     * <ul>
     *   <li>{@link KeyEvent#KEY_PRESSED} : Touche pressée</li>
     *   <li>{@link KeyEvent#KEY_RELEASED} : Touche relâchée</li>
     * </ul>
     *
     * <p><strong>Note importante :</strong> Le changement de stratégie s'effectue
     * à chaque événement pour garantir une réactivité immédiate lors de l'ouverture
     * ou fermeture de l'inventaire.</p>
     *
     * @param event L'événement clavier à traiter
     * @see KeyEvent
     * @see KeyCodeInterface#handleKeyPressed(javafx.scene.input.KeyCode)
     * @see KeyCodeInterface#handleKeyReleased(javafx.scene.input.KeyCode)
     * @see InventoryView#getInventoryOpened()
     */
    @Override
    public void handle(KeyEvent event) {
        // Choix de la stratégie selon l'état de l'inventaire
        currentStrategy = inventoryView.getInventoryOpened() ? inventoryStrategy : playerStrategy;
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            currentStrategy.handleKeyPressed(event.getCode());
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            currentStrategy.handleKeyReleased(event.getCode());
        }
    }
}