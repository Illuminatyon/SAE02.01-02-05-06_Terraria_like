package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.view.GlobalView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.iut.hev.root.model.TileMap.format;

public class MouseItemActionInputHandler implements EventHandler<MouseEvent> {

    private InventoryView inventoryView;
    private World world;
    private Player player;
    private Cooldown itemUseCooldown;
    private double x;
    private double y;
    private boolean mouseClickIsPressed;
    private boolean mouseClickIsReleased;
    private MouseEvent mouseEvent;
    private GlobalView worldView;
    private TileMap tileMap;

    public MouseItemActionInputHandler(World world, InventoryView inventoryView, GlobalView worldView) {
        this.world = world;
        this.inventoryView = inventoryView;
        this.player = world.getPlayer();
        this.worldView = worldView;
        this.tileMap = world.getTileMap();
        this.x = 0;
        this.y = 0;
        this.mouseClickIsPressed = false;
        this.mouseClickIsReleased = false;
        this.mouseEvent = null;
        this.itemUseCooldown = new Cooldown(0);
        if (player.getItemInHand() != null)
            this.itemUseCooldown.setLimit(player.getItemInHand().getCooldown());
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!inventoryView.getInventoryOpened() && !itemUseCooldown.getOnGoing()) {
            // Adjust mouse coordinates by camera offset
            //x = mouseEvent.getX() - world.getCamera().getOffsetX();
            //y = mouseEvent.getY() - world.getCamera().getOffsetY();

            if (player.getItemInHand() == null) {
                // Handle empty hand - no longer allows breaking blocks
                // Still track mouse events for consistency
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    this.mouseClickIsPressed = true;
                    this.mouseEvent = mouseEvent;
                }
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    mouseClickIsPressed = false;
                    mouseClickIsReleased = true;

                    // Call the appropriate method based on which mouse button was released
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        onLeftClickReleased();
                    } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        onRightClickReleased();
                    }
                }
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                    this.mouseEvent = mouseEvent;
                }
            }
            else {
                if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.WEAPON) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK)) {
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

    public void onClickPressedLoop() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickPressedLoop();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickPressedLoop();
        }
    }

    public void onClickReleasedLoop() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickReleasedLoop();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickReleasedLoop();
        }
    }

    public void onLeftClickPressedLoop() {
        if (player.getItemInHand() == null) {
            // Empty hands can no longer break blocks
            // Only tools can break blocks now
        } else if (player.usesItemInHand(this)) {
            if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK))
                worldView.updateTile(tileMap.getTile((int)x / format,(int)y / format));
        }
    }

    public void onRightClickPressedLoop() {

    }

    public void onLeftClickReleasedLoop() {
        if (player.getItemInHand() == null) {
            // Empty hands can no longer break blocks
            // Only tools can break blocks now
        } else if (player.usesItemInHand(this)) {
            if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK))
                worldView.updateTile(tileMap.getTile((int)x / format,(int)y / format));
        }
        this.mouseClickIsReleased = false;
    }

    public void onRightClickReleasedLoop() {

    }

    public void onLeftClickReleased() {
        if (player.getItemInHand() == null) {
            // Empty hands can no longer break blocks
            // Only tools can break blocks now
            // Still set a small cooldown for consistency
            itemUseCooldown.setLimit(0.1);
            itemUseCooldown.start();
        } else {
            player.usesItemInHand(this);
            itemUseCooldown.start();
        }
    }
    public void onRightClickReleased() {

    }

    public void updateCooldown() {
        if (player.getItemInHand() != null)
            itemUseCooldown.setLimit(player.getItemInHand().getCooldown());
        else
            itemUseCooldown.setLimit(0);
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
