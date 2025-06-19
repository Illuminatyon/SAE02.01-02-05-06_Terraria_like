package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.enums.RecipeAvailability;
import fr.iut.hev.root.model.enums.RecipesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import java.util.ArrayList;

public class RecipeGiver extends Entity {

    private ArrayList<RecipesEnum> recipesEnums;
    private RecipeAvailability recipeAvailability;
    private HitboxManager hitboxManager;

    public RecipeGiver(int posX, int posY, int width, int height, RecipeAvailability recipeAvailability, HitboxManager hitboxManager, Tile listenedTile) {
        super(posX,posY,width,height,null);
        this.recipeAvailability = recipeAvailability;
        this.hitboxManager = hitboxManager;
        initRecipes();
        this.hitboxManager.createHitbox(this, HitboxType.INTERACTION);
        listenedTile.
    }

    private void initRecipes() {
        for (RecipesEnum recipe : RecipesEnum.values()) {
            if (recipe.getRecipeAvailability().equals(recipeAvailability)) {
                recipesEnums.add(recipe);
            }
        }
    }

    public void givesRecipe(Player player) {
        for (RecipesEnum recipe : recipesEnums) {
            player.getCraftingManager().addRecipe(recipe);
        }
    }
}
