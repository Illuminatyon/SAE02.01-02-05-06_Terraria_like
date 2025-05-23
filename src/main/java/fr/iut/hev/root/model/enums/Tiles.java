package fr.iut.hev.root.model.enums;

import javafx.scene.image.Image;

// Ca ou deux enums ?
public enum Tiles {
    AIR("air", TileTypes.AIR, 0), // Peut etre pas nécessaire
    GRASS("grass", TileTypes.BLOCK, 10),
    DIRT("dirt", TileTypes.BLOCK, 10),
    STONE("stone", TileTypes.BLOCK, 40),
    //WATER("water", TileTypes.LIQUID),
    //LAVA("lava", TileTypes.LIQUID);
    CRAFTING_TABLE("crafting_table",TileTypes.UTILITIES,20),
    TREE("tree",TileTypes.TREE, 25),
    FURNACE("furnace",TileTypes.UTILITIES,40),
    IRON_ORE("iron_ore",TileTypes.BLOCK,50);

    // Est ce que je devrai plutot mettre la texture ici, ou bien seulement dans le Tile
    // Enft qui va gerer la texture
    private final String name;
    private final TileTypes type;
    private final int maxHealth;

    Tiles(String name, TileTypes type, int maxHealth) {
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
