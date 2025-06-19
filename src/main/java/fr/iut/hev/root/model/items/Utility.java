package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import static fr.iut.hev.root.model.TileMap.format;

public class Utility extends Item {

    private HitboxManager hitboxManager;

    public Utility(ItemsEnum itemsEnum,HitboxManager hitboxManager) {
        super(itemsEnum);
        this.hitboxManager = hitboxManager;
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / format;
        int y  = (int)eventHandler.getY() / format;
        TileMap tileMap = eventHandler.getTileMap();
        Player player = eventHandler.getPlayer();

        // Check if the target position is within reach (3 tiles)
        if (!player.isWithinReach(x, y, 3)) {
            return false;
        }

        if (tileMap.isTileEmpty(x,y)) {
            TilesEnum currentItem = player.getItemInHand().getItemEnum().getRelatedTile();
            tileMap.addTile(new Tile(currentItem,x,y));
            player.getInventory().remove(player.getIndexItemInHand(),1);
            player.consumeOneItem();
            System.out.println(tileMap.getTile(x,y));
            return true;
        }
        return false;
    }
}
