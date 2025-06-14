package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.TilesEnum;

import static fr.iut.hev.root.model.TileMap.format;

public class Block extends Item {

    public Block(ItemsEnum itemsEnum) {super(itemsEnum);}

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / format;
        int y  = (int)eventHandler.getY() / format;
        TileMap tileMap = eventHandler.getTileMap();
        Player player = eventHandler.getPlayer();
        if (tileMap.isTileEmpty(x,y)) {
            TilesEnum currentItem = player.getItemInHand().getItemEnum().getRelatedTile();
            System.out.println(currentItem);
            tileMap.addTile(new Tile(currentItem,x,y));
            player.getInventory().remove(player.getItemInHand().getItemEnum(),1);
            return true;
        }
        return false;
    }
}
