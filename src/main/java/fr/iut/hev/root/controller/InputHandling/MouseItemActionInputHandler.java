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

import static fr.iut.hev.root.model.land.TileMap.format;
/**
 * Gestionnaire des actions de la souris liées aux objets.
 * Cette classe est responsable de la capture et du traitement des événements souris
 * pour l'utilisation des objets dans le jeu (minage, placement de blocs, utilisation d'armes, etc.).
 */
public class MouseItemActionInputHandler implements EventHandler<MouseEvent> {

    private InventoryView inventoryView;
    private Player player;
    private Camera camera;
    private Cooldown itemUseCooldown;
    private double x;
    private double y;
    private boolean mouseClickIsPressed;
    private boolean mouseClickIsReleased;
    private MouseEvent mouseEvent;
    private TileMapView worldView;
    private TileMap tileMap;

    /**
     * Constructeur du gestionnaire d'actions souris liées aux objets.
     * Initialise les références aux composants du jeu et configure l'état initial.
     *
     * @param camera Caméra du jeu
     * @param inventoryView Vue de l'inventaire du joueur
     * @param worldView Vue globale du monde
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
        this.mouseEvent = null;
        this.itemUseCooldown = new Cooldown(0);
        if (player.getItemInHand() != null)
            this.itemUseCooldown.setLimit(player.getItemInHand().getCooldown());
    }

    /**
     * Gère les événements souris.
     * Cette méthode traite les événements de clic et de déplacement de la souris,
     * et déclenche les actions correspondantes en fonction du type d'objet tenu par le joueur.
     *
     * @param mouseEvent L'événement souris à traiter
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!inventoryView.getInventoryOpened() && !itemUseCooldown.getOnGoing()) {

            if (player.getItemInHand() == null) {
                System.out.println("OK");
            }
            else {
                if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.WEAPON) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.UTILITY)) {
                    x = mouseEvent.getX() - camera.getCurrentCamX();
                    y = mouseEvent.getY() - camera.getCurrentCamY();
                    if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                        this.mouseClickIsPressed = true;
                        this.mouseEvent = mouseEvent;
                    }
                    if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                        mouseClickIsPressed = false;
                        mouseClickIsReleased = true;
                    }
                    if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                        this.mouseEvent = mouseEvent;
                    }
                }
                else if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.ARMOR_PIECE) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.CONSUMABLE) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.RESOURCES)) {
                    if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            onLeftClickReleased();
                        } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                            onRightClickReleased();
                        }
                    }
                }
            }
        }
    }

    /**
     * Traite les clics de souris maintenus.
     * Redirige vers la méthode appropriée en fonction du bouton de souris utilisé.
     */
    public void onClickPressedLoop() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickPressedLoop();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickPressedLoop();
        }
    }

    /**
     * Traite les relâchements de clics de souris.
     * Redirige vers la méthode appropriée en fonction du bouton de souris utilisé.
     */
    public void onClickReleasedLoop() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickReleasedLoop();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickReleasedLoop();
        }
    }

    /**
     * Traite les clics gauches maintenus.
     * Utilise l'objet tenu par le joueur et met à jour la tuile ciblée si nécessaire.
     */
    public void onLeftClickPressedLoop() {
        if (player.usesItemInHand(this)) {
            if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.UTILITY))
                worldView.updateTile(tileMap.getTile((int)x / format,(int)y / format));
        }
    }


    public void onRightClickPressedLoop() {

    }
    /**
     * Traite les relâchements de clics gauches.
     * Utilise l'objet tenu par le joueur, met à jour la tuile ciblée si nécessaire,
     * et réinitialise l'état du clic.
     */
    public void onLeftClickReleasedLoop() {
        if (player.usesItemInHand(this)) {
            if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.UTILITY))
                worldView.updateTile(tileMap.getTile((int)x / format,(int)y / format));
        }
        this.mouseClickIsReleased = false;
    }

    public void onRightClickReleasedLoop() {

    }

    public void onLeftClickReleased() {
        player.usesItemInHand(this);
        itemUseCooldown.start();
    }
    public void onRightClickReleased() {
    }

    public void updateCooldown() {
        if (player.getItemInHand() != null)
            itemUseCooldown.setLimit(player.getItemInHand().getCooldown());
        else
            itemUseCooldown.setLimit(0);
    }

    public void checkMouseInput() {
        if (mouseClickIsPressed)
            onClickPressedLoop();
        if (mouseClickIsReleased)
            onClickReleasedLoop();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getMouseClickIsPressed() {return this.mouseClickIsPressed;}
    public boolean getMouseClickIsReleased() {return this.mouseClickIsReleased;}
    public TileMap getTileMap() {return this.tileMap;}
    public double getX() {return this.x;}
    public double getY() {return this.y;}
}
