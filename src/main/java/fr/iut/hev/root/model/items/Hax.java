package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.TreeManager;
import fr.iut.hev.root.model.enums.BlockTypesEnum;
import fr.iut.hev.root.model.enums.ItemStatsEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

import static fr.iut.hev.root.model.TileMap.format;

public class Hax extends Tool {

    public Hax(ItemsEnum itemsEnum) {super(itemsEnum);}

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        System.out.println("tries break");
        int x = (int)eventHandler.getX();
        int y = (int)eventHandler.getY();
        TreeManager treeManager = eventHandler.getTreeManager();
        if (eventHandler.getMouseClickIsPressed()) {
            ItemStatsEnum statsToolInHand = eventHandler.getPlayer().getItemInHand().getItemEnum().getStats();
            if (statsToolInHand.getEfficientBlockAgainst().equals(BlockTypesEnum.WOOD_TYPE)) {
                treeManager.treeTakesDamage(x, y, statsToolInHand.getMiningSpeed());
            }
            System.out.println("breaks");
            System.out.println(treeManager.getTree(x,y));
            return true;
        }
        else if (eventHandler.getMouseClickIsReleased()) {
            treeManager.getTree(x,y).resetHealth();
            return true;
        }
        return false;
    }
}
