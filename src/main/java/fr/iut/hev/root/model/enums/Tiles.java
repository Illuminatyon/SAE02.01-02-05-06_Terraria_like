package fr.iut.hev.root.model.enums;

import javafx.scene.image.Image;

// Ca ou deux enums ?
public enum Tiles {
    AIR("air", TileTypes.AIR), // Peut etre pas nécessaire
    GRASS("grass", TileTypes.BLOCK),
    DIRT("dirt", TileTypes.BLOCK),
    STONE("stone", TileTypes.BLOCK),
    WATER("water", TileTypes.LIQUID),
    LAVA("lava", TileTypes.LIQUID);

    // Est ce que je devrai plutot mettre la texture ici, ou bien seulement dans le Tile
    // Enft qui va gerer la texture
    private final String name;
    private final TileTypes type;

    Tiles(String name, TileTypes type) {
        this.name = name;
        this.type = type;
    }

    public Image getTexture(/*int damageAmount*/) {
        if (this.type == TileTypes.AIR) return null;
        String path = "/fr/iut/hev/terraria/terraria/img/tile/".concat(this.name)./*concat(Integer.toString(damageAmount)).*/concat(".png");
        System.out.println(path);
        return new Image(getClass().getResource(path).toExternalForm());
    }

    //public Image getTexture_background()

    public TileTypes getType() {
        return this.type;
    }

    public String toString() {
        return this.name;
    }
}
