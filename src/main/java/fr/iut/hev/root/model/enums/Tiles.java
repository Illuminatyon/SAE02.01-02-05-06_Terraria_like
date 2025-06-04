package fr.iut.hev.root.model.enums;

import javafx.scene.image.Image;

// Ca ou deux enums ?
public enum Tiles {
    AIR("air", TileTypes.AIR), // Peut etre pas nécessaire
    GRASS("grass", TileTypes.BLOCK),
    DIRT("dirt", TileTypes.BLOCK),
    STONE("stone", TileTypes.BLOCK),
    //WATER("water", TileTypes.LIQUID),
    //LAVA("lava", TileTypes.LIQUID);
    CRAFTING_TABLE("crafting_table",TileTypes.UTILITIES),
    TREE("tree",TileTypes.TREE),
    FURNACE("furnace",TileTypes.UTILITIES),
    IRON_ORE("iron_ore",TileTypes.BLOCK);

    // Est ce que je devrai plutot mettre la texture ici, ou bien seulement dans le Tile
    // Enft qui va gerer la texture
    private final String name;
    private final TileTypes type;

    Tiles(String name, TileTypes type) {
        this.name = name;
        this.type = type;
    }

    public TileTypes getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
