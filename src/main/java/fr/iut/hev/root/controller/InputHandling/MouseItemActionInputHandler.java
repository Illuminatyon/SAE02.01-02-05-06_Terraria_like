package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseItemActionInputHandler implements EventHandler<MouseEvent> {

    private InventoryView inventoryView;
    private Player player;
    private Cooldown itemUseCooldown;

    public MouseItemActionInputHandler(InventoryView inventoryView,Player player) {
        this.inventoryView = inventoryView;
        this.player = player;
        this.itemUseCooldown = new Cooldown(0);
        if (player.getItemInHand() != null)
            this.itemUseCooldown.setLimit(player.getItemInHand().getItemEnum().getCooldown());
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!inventoryView.getInventoryOpened() && !itemUseCooldown.getOnGoing()) {
            if (player.getItemInHand() == null)
                System.out.println("prout");
            else if (player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.WEAPON) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.TOOL) || player.getItemInHand().getItemEnum().getItemType().equals(ItemTypesEnum.BLOCK)) {
                System.out.println("weapon, tool or block");
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        onLeftClickPressed();
                    } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        onRightClickPressed();
                    }
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

    public void onLeftClickPressed() {

    }

    public void onRightClickPressed() {

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

    public Player getPlayer() {
        return player;
    }
}
