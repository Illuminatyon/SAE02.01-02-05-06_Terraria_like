package fr.iut.hev.root.model.enums;

import javafx.scene.image.Image;

// Ca ou deux enums ?
public enum Tiles {
    AIR("air", TileTypes.AIR,0), // Peut etre pas nécessaire
    GRASS("grass", TileTypes.BLOCK,8),
    DIRT("dirt", TileTypes.BLOCK,8),
    STONE("stone", TileTypes.BLOCK,12),
    //WATER("water", TileTypes.LIQUID),
    //LAVA("lava", TileTypes.LIQUID);
    CRAFTING_TABLE("crafting_table",TileTypes.UTILITIES,15),
    TREE("tree",TileTypes.TREE,15),
    FURNACE("furnace",TileTypes.UTILITIES,20),
    IRON_ORE("iron_ore",TileTypes.BLOCK,20);

    private final String name;
    private final TileTypes type;
    private final int maxHealth;

    Tiles(String name, TileTypes type,int maxHealth) {
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
    }

    public TileTypes getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxHealth() {return this.maxHealth;}
}
