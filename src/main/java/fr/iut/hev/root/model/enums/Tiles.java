package fr.iut.hev.root.model.enums;

import javafx.scene.image.Image;

// Ca ou deux enums ?
public enum Tiles {
    AIR("air", TileTypes.AIR,0),
    GRASS("grass", TileTypes.BLOCK,8, Items.DIRT,BlockType.GROUND_TYPE),
    DIRT("dirt", TileTypes.BLOCK,8, Items.DIRT,BlockType.GROUND_TYPE),
    STONE("stone", TileTypes.BLOCK,12, Items.STONE,BlockType.ROCK_TYPE),
    //WATER("water", TileTypes.LIQUID),
    //LAVA("lava", TileTypes.LIQUID);
    CRAFTING_TABLE("crafting_table",TileTypes.UTILITIES,15, Items.CRAFTING_TABLE,BlockType.WOOD_TYPE),
    TREE("tree",TileTypes.BACKGROUND,15, Items.WOOD,BlockType.WOOD_TYPE),
    FURNACE("furnace",TileTypes.UTILITIES,20, Items.FURNACE),
    IRON_ORE("iron_ore",TileTypes.BLOCK,20,Items.IRON_BLOCK,BlockType.ROCK_TYPE);

    private final String name;
    private final TileTypes type;
    private final int maxHealth;
    private final Items relatedItem;
    private BlockType blockType;

    Tiles(String name, TileTypes type,int maxHealth, Items relatedItem,BlockType blockType) {
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
        this.relatedItem = relatedItem;
        this.blockType = blockType;
    }

    Tiles(String name, TileTypes type,int maxHealth, Items relatedItem) {
        this(name, type, maxHealth,relatedItem,null);
    }

    Tiles(String name, TileTypes type,int maxHealth) {
        this(name, type, maxHealth, null);
    }

    public TileTypes getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxHealth() {return this.maxHealth;}

    public Items getRelatedItem() {
        return this.relatedItem;
    }
}
