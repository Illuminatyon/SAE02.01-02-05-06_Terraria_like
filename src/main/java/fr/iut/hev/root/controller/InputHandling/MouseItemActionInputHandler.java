package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.items.enums.ItemTypesEnum;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.view.Camera;
import fr.iut.hev.root.view.TileMapView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Gestionnaire des actions de la souris liées aux objets dans le jeu.
 * Traite les clics pour utiliser les objets tenus par le joueur (outils, blocs, armes, etc.).
 */
public class MouseItemActionInputHandler implements EventHandler<MouseEvent> {
    private final InventoryView inventoryView;
    private final Player player;
    private final Camera camera;
    private final Cooldown itemUseCooldown;
    private final TileMapView worldView;
    private final TileMap tileMap;

    // État des clics souris
    private boolean mouseClickIsPressed;
    private boolean mouseClickIsReleased;
    private MouseEvent mouseEvent;
    private double x;
    private double y;

    /**
     * Initialise le gestionnaire avec les références nécessaires.
     */
    public MouseItemActionInputHandler(Camera camera, InventoryView inventoryView, TileMapView worldView) {
        this.inventoryView = inventoryView;
        this.player = Player.getInstance();
        this.camera = camera;
        this.worldView = worldView;
        this.tileMap = TileMap.getInstance();
        this.x = 0;
        this.y = 0;
        this.mouseClickIsPressed = false;
        this.mouseClickIsReleased = false;
        this.itemUseCooldown = new Cooldown(0);
        updateCooldown();
    }

    /**
     * Gère les événements souris en fonction du type d'item tenu.
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        if (inventoryView.getInventoryOpened() || itemUseCooldown.getOnGoing()) {
            return;
        }

        if (player.getItemInHand() == null) {
            return;
        }

        updateMousePosition(mouseEvent);

        ItemTypesEnum itemType = player.getItemInHand().getItemEnum().getItemType();
        if (isToolBlockOrUtilityItem(itemType)) {
            handleToolBlockOrUtilityEvent(mouseEvent);
        } else if (isArmorConsumableOrResourceItem(itemType)) {
            handleArmorConsumableOrResourceEvent(mouseEvent);
        }
    }

    /**
     * Met à jour la position de la souris en coordonnées monde.
     */
    private void updateMousePosition(MouseEvent mouseEvent) {
        x = mouseEvent.getX() - camera.getCurrentCamX();
        y = mouseEvent.getY() - camera.getCurrentCamY();
    }

    /**
     * Vérifie si l'item est un outil, un bloc ou un objet utilitaire.
     */
    private boolean isToolBlockOrUtilityItem(ItemTypesEnum itemType) {
        return itemType == ItemTypesEnum.TOOL ||
                itemType == ItemTypesEnum.BLOCK ||
                itemType == ItemTypesEnum.UTILITY;
    }

    /**
     * Vérifie si l'item est une pièce d'armure, un consommable ou une ressource.
     */
    private boolean isArmorConsumableOrResourceItem(ItemTypesEnum itemType) {
        return itemType == ItemTypesEnum.ARMOR_PIECE ||
                itemType == ItemTypesEnum.CONSUMABLE ||
                itemType == ItemTypesEnum.RESOURCES;
    }

    /**
     * Gère les événements pour les outils, blocs et objets utilitaires.
     */
    private void handleToolBlockOrUtilityEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            mouseClickIsPressed = true;
            this.mouseEvent = mouseEvent;
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            mouseClickIsPressed = false;
            mouseClickIsReleased = true;
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            this.mouseEvent = mouseEvent;
        }
    }

    /**
     * Gère les événements pour les pièces d'armure, consommables et ressources.
     */
    private void handleArmorConsumableOrResourceEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                onLeftClickReleased();
            }
        }
    }

    /**
     * Traite les clics gauches maintenus pour les outils/blocs/objets utilitaires.
     */
    public void onLeftClickPressedLoop() {
        if (player.usesItemInHand(this)) {
            worldView.updateTile(tileMap.getTile((int) x, (int) y));
        }
    }

    /**
     * Traite les relâchements de clics gauches pour les outils/blocs/objets utilitaires.
     */
    public void onLeftClickReleasedLoop() {
        if (player.usesItemInHand(this)) {
            worldView.updateTile(tileMap.getTile((int) x, (int) y));
        }
        mouseClickIsReleased = false;
    }

    /**
     * Traite les relâchements de clics gauches pour les autres types d'items.
     */
    public void onLeftClickReleased() {
        player.usesItemInHand(this);
        itemUseCooldown.start();
    }

    /**
     * Met à jour le cooldown en fonction de l'item tenu.
     */
    public void updateCooldown() {
        if (player.getItemInHand() != null) {
            itemUseCooldown.setLimit(player.getItemInHand().getCooldown());
        } else {
            itemUseCooldown.setLimit(0);
        }
    }

    /**
     * Vérifie les entrées souris et déclenche les actions appropriées.
     */
    public void checkMouseInput() {
        if (mouseClickIsPressed) {
            onClickPressedLoop();
        }
        if (mouseClickIsReleased) {
            onClickReleasedLoop();
        }
    }

    /**
     * Redirige vers la méthode appropriée en fonction du bouton de souris utilisé.
     */
    public void onClickPressedLoop() {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            onLeftClickPressedLoop();
        }
    }

    /**
     * Redirige vers la méthode appropriée en fonction du bouton de souris utilisé.
     */
    public void onClickReleasedLoop() {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            onLeftClickReleasedLoop();
        }
    }

    // Getters
    public Player getPlayer() { return player; }
    public boolean getMouseClickIsPressed() { return mouseClickIsPressed; }
    public boolean getMouseClickIsReleased() { return mouseClickIsReleased; }
    public TileMap getTileMap() { return tileMap; }
    public double getX() { return x; }
    public double getY() { return y; }
}
