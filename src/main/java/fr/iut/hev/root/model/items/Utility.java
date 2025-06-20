package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.entities.RecipeGiver;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.RecipeAvailability;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import static fr.iut.hev.root.model.TileMap.format;

public class Utility extends Item {

    private HitboxManager hitboxManager;
    private RecipeGiver recipeGiver;

    public Utility(ItemsEnum itemsEnum,HitboxManager hitboxManager) {
        super(itemsEnum);
        this.hitboxManager = hitboxManager;
        this.recipeGiver = null;
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
            Tile newTile = new Tile(player.getItemInHand().getItemEnum().getRelatedTile(),x,y);
            this.recipeGiver = new RecipeGiver(x * format + 16,y * format + 16,format,format, RecipeAvailability.CRAFTING_TABLE,hitboxManager);
            newTile.brokenProperty().addListener((observableValue, aBoolean, t1) -> {
                if (t1)
                    this.recipeGiver = null;
            });
            tileMap.addTile(newTile);
            player.getInventory().remove(player.getIndexItemInHand(),1);
            player.consumeOneItem();
            return true;
        }
        return false;
    }
}
