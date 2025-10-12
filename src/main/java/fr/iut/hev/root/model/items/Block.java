package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.land.Tile;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.land.TilesEnum;

import static fr.iut.hev.root.model.land.TileMap.format;

public class Block extends Item {

    public Block(ItemsEnum itemsEnum) {super(itemsEnum);}

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
            System.out.println(currentItem);
            tileMap.addTile(new Tile(currentItem,x,y));
            player.getInventory().remove(player.getIndexItemInHand(),1);
            player.consumeOneItem();
            return true;
        }
        return false;
    }
}
