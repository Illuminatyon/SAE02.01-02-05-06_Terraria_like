
package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.craft.RecipesEnum;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.items.enums.ItemTypesEnum;
import fr.iut.hev.root.model.utilities.CooldownManager;
import fr.iut.hev.root.view.Camera;
import fr.iut.hev.root.view.GlobalView;
import fr.iut.hev.root.controller.InputHandling.InputHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <h2>Contrôleur global de l'application</h2>
 *
 * <p>La classe {@code GlobalController} est le contrôleur principal de l'application
 * qui coordonne l'interaction entre le modèle (monde du jeu) et la vue (interface graphique).
 * Elle implémente le pattern MVC (Model-View-Controller) pour séparer la logique métier
 * de la présentation.</p>
 *
 * <p><strong>Responsabilités principales :</strong></p>
 * <ul>
 *   <li>Initialisation du monde du jeu et de tous ses composants</li>
 *   <li>Gestion de la boucle de jeu (game loop) à 60 FPS (~0.017s par frame)</li>
 *   <li>Coordination entre les entrées utilisateur, la caméra et le rendu</li>
 *   <li>Mise à jour synchronisée de tous les éléments du jeu</li>
 * </ul>
 *
 * <p><strong>Architecture :</strong></p>
 * <ul>
 *   <li>{@link World} : Gère l'état du monde, les entités et la physique</li>
 *   <li>{@link Camera} : Contrôle la vue de la caméra suivant le joueur</li>
 *   <li>{@link InputHandler} : Traite les entrées clavier et souris</li>
 *   <li>{@link GlobalView} : Coordonne tous les éléments visuels</li>
 *   <li>{@link CooldownManager} : Gère les délais de récupération des actions</li>
 * </ul>
 *
 * <p><strong>Cycle de vie :</strong></p>
 * <ol>
 *   <li>Initialisation via {@link #initialize(URL, ResourceBundle)}</li>
 *   <li>Démarrage de la boucle de jeu</li>
 *   <li>Mise à jour continue via {@link #update()}</li>
 * </ol>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see Initializable
 * @see World
 * @see GlobalView
 * @see InputHandler
 * @since 1.0
 */
public class GlobalController implements Initializable {

    /**
     * Instance du monde du jeu contenant toutes les entités, le terrain et la physique.
     */
    private World world;

    /**
     * Caméra qui suit le joueur et gère le défilement de l'écran.
     */
    private Camera camera;

    /**
     * Gestionnaire des entrées utilisateur (clavier et souris).
     */
    private InputHandler inputHandler;

    /**
     * Gestionnaire des temps de récupération pour les actions du joueur.
     */
    private CooldownManager cooldownManager;

    /**
     * Vue globale coordonnant tous les éléments visuels du jeu.
     */
    private GlobalView globalView;

    /**
     * Panneau contenant les entités visibles du jeu (joueur, mobs, items).
     */
    @FXML private AnchorPane entitiesPane;

    /**
     * Panneau parent principal de l'interface.
     */
    @FXML private AnchorPane parentPane;

    /**
     * Grille affichant les tuiles du terrain (foreground).
     */
    @FXML private TilePane landTileMap;

    /**
     * Grille affichant les tuiles d'arrière-plan (background).
     */
    @FXML private TilePane backgroundTileMap;

    /**
     * Panneau contenant l'interface utilisateur (HUD).
     */
    @FXML private AnchorPane hudAnchorPane;

    /**
     * Container horizontal affichant les cœurs de vie du joueur.
     */
    @FXML private HBox heartsHbox;

    /**
     * Grille représentant la barre d'accès rapide (hotbar) du joueur.
     */
    @FXML private GridPane hotbarInventory;

    /**
     * Grille représentant l'inventaire complet du joueur.
     */
    @FXML private GridPane expandedInventory;

    /**
     * Liste affichant les recettes de craft disponibles.
     */
    @FXML private ListView<RecipesEnum> craftListView;

    /**
     * Bouton pour déclencher le craft d'un objet.
     */
    @FXML private Button craftButton;

    /**
     * Container horizontal affichant les ingrédients de la recette sélectionnée.
     */
    @FXML private HBox recipeDisplay;

    /**
     * <h3>Initialisation du contrôleur</h3>
     *
     * <p>Méthode appelée automatiquement par JavaFX après le chargement du fichier FXML.
     * Elle initialise tous les composants du jeu dans l'ordre suivant :</p>
     *
     * <ol>
     *   <li>Création et initialisation du monde (3840x1440 pixels)</li>
     *   <li>Initialisation des énumérations d'items pour le système de craft</li>
     *   <li>Création de la vue globale avec tous les éléments visuels</li>
     *   <li>Configuration de la caméra suivant le joueur</li>
     *   <li>Configuration du gestionnaire d'entrées</li>
     *   <li>Démarrage de la boucle de jeu</li>
     *   <li>Initialisation des vues des mobs</li>
     * </ol>
     *
     * @param url L'emplacement utilisé pour résoudre les chemins relatifs (non utilisé)
     * @param resourceBundle Les ressources utilisées pour localiser l'interface (non utilisé)
     *
     * @see Initializable#initialize(URL, ResourceBundle)
     * @see World#initWorld(int, int)
     * @see #initItemEnums()
     * @see #initCamera()
     * @see #initInputHandler()
     * @see #initGameLoop()
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        world = World.getInstance();
        world.initWorld(120, 45);
        initItemEnums();
        globalView = new GlobalView(landTileMap,backgroundTileMap,entitiesPane,heartsHbox,craftListView,craftButton,recipeDisplay,hotbarInventory,expandedInventory,hudAnchorPane);
        initCamera();
        initInputHandler();
        initGameLoop();
        globalView.initMobViews(camera,entitiesPane);
    }

    /**
     * <h3>Initialisation de la boucle de jeu</h3>
     *
     * <p>Configure et démarre la boucle principale du jeu (game loop) qui s'exécute
     * indéfiniment à approximativement 60 images par seconde (60 FPS).</p>
     *
     * <p><strong>Fonctionnement :</strong></p>
     * <ul>
     *   <li>Fréquence : 0.017 secondes par frame (~60 FPS)</li>
     *   <li>Répétition : Infinie (jusqu'à l'arrêt du jeu)</li>
     *   <li>Callback : Appelle {@link #update()} à chaque frame</li>
     * </ul>
     *
     * <p>Cette méthode initialise également le {@link CooldownManager} qui gère
     * les temps de récupération des actions du joueur (attaques, utilisation d'objets, etc.).</p>
     *
     * @see Timeline
     * @see KeyFrame
     * @see Duration
     * @see CooldownManager
     * @see #update()
     */
    private void initGameLoop(){
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        cooldownManager = new CooldownManager();

        KeyFrame fk = new KeyFrame(
                Duration.seconds(0.017),
                ev -> update()
        );
        gameLoop.getKeyFrames().add(fk);
        gameLoop.play();
    }

    /**
     * <h3>Mise à jour du jeu</h3>
     *
     * <p>Méthode appelée à chaque frame de la boucle de jeu (~60 fois par seconde).
     * Elle met à jour tous les composants du jeu dans un ordre spécifique pour
     * garantir la cohérence de l'état du jeu.</p>
     *
     * <p><strong>Ordre de mise à jour :</strong></p>
     * <ol>
     *   <li><strong>Monde :</strong> Physique, collisions, IA des mobs, etc.</li>
     *   <li><strong>Caméra :</strong> Position suivant le joueur</li>
     *   <li><strong>Entrées souris :</strong> Actions sur les items et l'inventaire</li>
     *   <li><strong>Cooldowns :</strong> Décrémentation des temps de récupération</li>
     * </ol>
     *
     * <p>Cette méthode est cruciale pour le bon fonctionnement du jeu et doit
     * rester performante pour maintenir un framerate stable.</p>
     *
     * @see World#updateWorld()
     * @see Camera#update()
     * @see InputHandler#getMouseItemActionInputHandler()
     * @see CooldownManager#allCooldownsTick()
     */
    private void update() {
        world.updateWorld();
        camera.update();
        inputHandler.getMouseItemActionInputHandler().checkMouseInput();
        cooldownManager.allCooldownsTick();
    }

    /**
     * <h3>Initialisation de la caméra</h3>
     *
     * <p>Configure la caméra qui suit le joueur et gère l'affichage du monde.
     * La caméra utilise une interpolation douce (lerp) pour créer un effet
     * de suivi fluide du joueur.</p>
     *
     * <p><strong>Paramètres de la caméra :</strong></p>
     * <ul>
     *   <li><strong>Cible :</strong> Instance unique du joueur</li>
     *   <li><strong>Éléments suivis :</strong> Terrain, arrière-plan, loots</li>
     *   <li><strong>Interpolation :</strong> 0.1 (10% de déplacement par frame)</li>
     * </ul>
     *
     * <p>La vue du joueur est liée aux propriétés de la caméra via des bindings
     * JavaFX pour une synchronisation automatique.</p>
     *
     * @see Camera
     * @see Player#getInstance()
     * @see javafx.beans.property.DoubleProperty#bind(javafx.beans.value.ObservableValue)
     */
    private void initCamera() {
        camera = new Camera(Player.getInstance(), landTileMap, backgroundTileMap, parentPane, globalView.getLootView(), 0.1);
        globalView.getPlayerView().camOffsetXProperty().bind(camera.currentCamXProperty());
        globalView.getPlayerView().camOffsetYProperty().bind(camera.currentCamYProperty());
    }

    /**
     * <h3>Initialisation du gestionnaire d'entrées</h3>
     *
     * <p>Configure le gestionnaire qui traite toutes les entrées utilisateur
     * (clavier et souris). Il permet au joueur d'interagir avec le monde,
     * l'inventaire et le système de craft.</p>
     *
     * <p><strong>Éléments gérés :</strong></p>
     * <ul>
     *   <li>Mouvements du joueur (ZQSD/WASD, saut)</li>
     *   <li>Ouverture/fermeture de l'inventaire</li>
     *   <li>Sélection et utilisation d'items</li>
     *   <li>Interactions avec les blocs du monde</li>
     *   <li>Navigation dans l'interface de craft</li>
     * </ul>
     *
     * @see InputHandler
     * @see InputHandler#initInputHandler(TilePane, AnchorPane)
     */
    private void initInputHandler() {
        inputHandler = new InputHandler(globalView.getPlayerView().getInventoryView(),globalView.getPlayerView().getCraftView(),camera, globalView.getTileMapView(),globalView.getPlayerView().getHotbarView());
        inputHandler.initInputHandler(landTileMap,hudAnchorPane);
    }

    /**
     * <h3>Initialisation des énumérations d'items</h3>
     *
     * <p>Parcourt toutes les énumérations d'items et initialise ceux qui sont
     * de type BLOCK ou UTILITY. Cette initialisation est nécessaire pour que
     * les items puissent être correctement utilisés dans le système de craft
     * et de placement de blocs.</p>
     *
     * <p><strong>Types d'items initialisés :</strong></p>
     * <ul>
     *   <li><strong>BLOCK :</strong> Blocs constructibles (terre, pierre, bois, etc.)</li>
     *   <li><strong>UTILITY :</strong> Objets utilitaires (torches, crafting table, etc.)</li>
     * </ul>
     *
     * @see ItemsEnum
     * @see ItemTypesEnum
     * @see ItemsEnum#itemEnumInit()
     */
    private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }
}