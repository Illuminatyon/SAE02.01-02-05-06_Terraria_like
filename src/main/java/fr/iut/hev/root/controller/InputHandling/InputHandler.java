
package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.view.*;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

/**
 * <h2>Gestionnaire central des entrées utilisateur</h2>
 *
 * <p>Cette classe coordonne tous les types d'entrées utilisateur dans le jeu :
 * clavier, souris et molette. Elle sert de <strong>façade</strong> pour unifier
 * la gestion des différents gestionnaires d'entrées spécialisés.</p>
 *
 * <p><strong>Architecture :</strong></p>
 * <pre>
 * InputHandler (Façade)
 *    ├── KeyInputHandler          → Clavier (mouvements, inventaire)
 *    ├── MouseInventoryInputHandler → Souris dans l'inventaire
 *    ├── MouseItemActionInputHandler → Souris dans le monde (usage d'item)
 *    └── ScrollInputHandler       → Molette (sélection hotbar)
 * </pre>
 *
 * <p><strong>Responsabilités :</strong></p>
 * <ul>
 *   <li>Création et configuration de tous les gestionnaires d'entrées</li>
 *   <li>Enregistrement des event handlers sur les composants JavaFX</li>
 *   <li>Configuration des bindings entre le modèle et la vue</li>
 *   <li>Coordination des interactions entre les différents handlers</li>
 * </ul>
 *
 * <p><strong>Pattern utilisé :</strong> Façade - Simplifie l'accès à un sous-système
 * complexe de gestion des entrées via une interface unifiée.</p>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see KeyInputHandler
 * @see MouseInventoryInputHandler
 * @see MouseItemActionInputHandler
 * @see ScrollInputHandler
 * @since 1.0
 */
public class InputHandler {

    /**
     * Gestionnaire des entrées clavier (mouvements, inventaire, craft).
     */
    private KeyInputHandler keyInputHandler;

    /**
     * Gestionnaire des clics souris dans l'inventaire (déplacer items).
     */
    private MouseInventoryInputHandler mouseInventoryInputHandler;

    /**
     * Gestionnaire des clics souris dans le monde (casser/placer blocs, attaquer).
     */
    private MouseItemActionInputHandler mouseItemActionInputHandler;

    /**
     * Gestionnaire de la molette de la souris (sélection hotbar).
     */
    private ScrollInputHandler scrollInputHandler;

    /**
     * Constructeur du gestionnaire d'entrées.
     *
     * <p>Initialise tous les gestionnaires spécialisés avec leurs dépendances.
     * Chaque gestionnaire reçoit les vues et modèles dont il a besoin pour
     * fonctionner.</p>
     *
     * @param inventoryView Vue de l'inventaire
     * @param craftView Vue de l'interface de craft
     * @param camera Caméra du jeu (pour les coordonnées monde/écran)
     * @param tileMapView Vue de la carte de tuiles
     * @param hotbarView Vue de la barre d'accès rapide
     * @throws NullPointerException si un des paramètres est null
     */
    public InputHandler(InventoryView inventoryView, CraftView craftView, Camera camera, TileMapView tileMapView, HotbarView hotbarView) {
        this.keyInputHandler = new KeyInputHandler(inventoryView,craftView);
        this.mouseInventoryInputHandler = new MouseInventoryInputHandler(Player.getInstance().getInventory(), inventoryView);
        this.mouseItemActionInputHandler = new MouseItemActionInputHandler(camera,inventoryView, tileMapView);
        this.scrollInputHandler = new ScrollInputHandler(Player.getInstance().getInventory(),hotbarView,inventoryView);
    }

    /**
     * <h3>Initialisation du gestionnaire de molette</h3>
     *
     * <p>Configure les bindings bidirectionnels entre le modèle du joueur et le
     * gestionnaire de molette. Ces bindings garantissent la synchronisation
     * automatique entre l'item en main et la sélection du hotbar.</p>
     *
     * <p><strong>Bindings configurés :</strong></p>
     * <ul>
     *   <li><strong>itemInHand ↔ onHandItem :</strong> Item actuellement équipé</li>
     *   <li><strong>quantityOfItemInHand ↔ quantity :</strong> Quantité de l'item équipé</li>
     * </ul>
     *
     * <p><strong>Listeners ajoutés :</strong></p>
     * <ul>
     *   <li><strong>itemInHand :</strong> Met à jour le cooldown quand l'item change</li>
     *   <li><strong>direction :</strong> Déclenche la mise à jour du hotbar lors du scroll</li>
     * </ul>
     *
     * @see Player#itemInHandProperty()
     * @see Player#quantityOfItemInHandProperty()
     * @see ScrollInputHandler#onHandItemProperty()
     * @see ScrollInputHandler#updateHotbar()
     */
    private void initScrollInputHandler() {
        Player player = Player.getInstance();
        player.itemInHandProperty().bindBidirectional(scrollInputHandler.onHandItemProperty());
        player.quantityOfItemInHandProperty().bindBidirectional(scrollInputHandler.quantityProperty());
        player.itemInHandProperty().addListener((observableValue, item, t1) -> mouseItemActionInputHandler.updateCooldown());

        scrollInputHandler.directionProperty().addListener((observableValue, number, t1) -> {
            if (scrollInputHandler.getDirection() != 0)
                scrollInputHandler.updateHotbar();
        });
    }

    /**
     * <h3>Initialisation complète du gestionnaire d'entrées</h3>
     *
     * <p>Point d'entrée principal pour configurer tous les gestionnaires d'entrées.
     * Cette méthode doit être appelée après la création de la scène JavaFX.</p>
     *
     * <p><strong>Étapes d'initialisation :</strong></p>
     * <ol>
     *   <li>Initialisation du gestionnaire de molette et ses bindings</li>
     *   <li>Enregistrement des event handlers sur la scène (via Platform.runLater)</li>
     * </ol>
     *
     * <p><strong>Event handlers enregistrés :</strong></p>
     * <ul>
     *   <li><strong>Scene → KeyEvent :</strong> Tous les événements clavier</li>
     *   <li><strong>Scene → MouseEvent :</strong> Clics et drags dans le monde</li>
     *   <li><strong>HUD → MouseEvent :</strong> Clics et mouvements dans l'inventaire</li>
     *   <li><strong>Scene → ScrollEvent :</strong> Molette de la souris</li>
     * </ul>
     *
     * <p><strong>Note importante :</strong> L'utilisation de {@link Platform#runLater(Runnable)}
     * garantit que les handlers sont enregistrés après la construction complète
     * de la scène JavaFX.</p>
     *
     * @param landTileMap La grille de tuiles du terrain (pour accéder à la scene)
     * @param hudAnchorPane Le panneau HUD (pour les événements d'inventaire)
     * @throws NullPointerException si un des paramètres est null
     * @see Platform#runLater(Runnable)
     * @see javafx.scene.Node#getScene()
     * @see javafx.scene.Node#addEventHandler(javafx.event.EventType, javafx.event.EventHandler)
     */
    public void initInputHandler(TilePane landTileMap, AnchorPane hudAnchorPane) {
        initScrollInputHandler();
        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED,mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseItemActionInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryInputHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollInputHandler);
        });
    }

    /**
     * <h3>Obtenir le gestionnaire d'actions souris</h3>
     *
     * <p>Donne accès au gestionnaire des clics souris dans le monde.
     * Utilisé par la boucle de jeu pour vérifier les entrées souris
     * maintenues (ex: casser plusieurs blocs en gardant le bouton enfoncé).</p>
     *
     * @return Le gestionnaire d'actions souris dans le monde
     * @see MouseItemActionInputHandler
     * @see MouseItemActionInputHandler#checkMouseInput()
     */
    public MouseItemActionInputHandler getMouseItemActionInputHandler() {return this.mouseItemActionInputHandler;}
}