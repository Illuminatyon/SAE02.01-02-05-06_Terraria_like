package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.BlockTypesEnum;
import fr.iut.hev.root.model.enums.ItemStatsEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

import static fr.iut.hev.root.model.TileMap.format;

public class Tool extends Item {

    private int miningSpeed;
    private BlockTypesEnum efficientBlockAgainst;

    public Tool(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.miningSpeed = getStats().getMiningSpeed();
        this.efficientBlockAgainst = getStats().getEfficientBlockAgainst();
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / format;
        int y = (int)eventHandler.getY() / format;
        TileMap tileMap = eventHandler.getTileMap();
        if (eventHandler.getMouseClickIsPressed()) {
            if (!(tileMap.isTileEmpty(x,y))) {
                ItemStatsEnum statsToolInHand = eventHandler.getPlayer().getItemInHand().getItemEnum().getStats();
                if (statsToolInHand.getEfficientBlockAgainst().equals(tileMap.getTile(x,y).getTileEnum().getBlockTypesEnum())) {
                    tileMap.tileGetsMined(x, y, statsToolInHand.getMiningSpeed());
                }
                else {
                    tileMap.tileGetsMined(x, y, 1);
                    System.out.println("weak");
                }
                return true;
            }
        }
        else if (eventHandler.getMouseClickIsReleased()) {
            if (!(tileMap.isTileEmpty(x,y))) {
                tileMap.getTile(x,y).resetHealth();
                return true;
            }
        }
        return false;
    }
}
