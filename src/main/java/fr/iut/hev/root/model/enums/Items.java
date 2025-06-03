package fr.iut.hev.root.model.enums;

public enum Items {
    WOOD("wood",100,ItemTypes.RESOURCES),
    STONE("stone",100,ItemTypes.BLOCK, Tiles.STONE),
    IRON_INGOT("iron_ingot",100,ItemTypes.RESOURCES),
    IRON_BLOCK("iron_block",100,ItemTypes.BLOCK),
    DIRT("dirt",100,ItemTypes.BLOCK, Tiles.DIRT),
    STICK("stick",100,ItemTypes.RESOURCES),
    FEATHER("feather",100,ItemTypes.RESOURCES),

    PIOCHE("pioche",1,ItemTypes.TOOL),
    HAX("hax",1,ItemTypes.TOOL),
    HAMMER("hammer",1,ItemTypes.TOOL),
    CRAFTING_TABLE("crafting_table",1,ItemTypes.TOOL, Tiles.CRAFTING_TABLE),
    FURNACE("furnace",100,ItemTypes.TOOL, Tiles.FURNACE),

    DAGGER("dagger",1,ItemTypes.WEAPON),

    IRON_HELMET("iron_helmet",1,ItemTypes.ARMOR_PIECE),
    IRON_CHESTPLATE("iron_chestplate",1,ItemTypes.ARMOR_PIECE),
    IRON_LEGGINGS("iron_leggings",1,ItemTypes.ARMOR_PIECE);

    private String name;
    private int limitStacking;
    private final ItemTypes itemType;
    private final Tiles relatedTile;

    Items(String name,int limitStacking, ItemTypes itemType, Tiles relatedTile) {
        this.name = name;
        this.limitStacking = limitStacking;
        this.itemType = itemType;
        this.relatedTile = relatedTile;
    }

    Items(String name,int limitStacking, ItemTypes itemType) {
        this(name, limitStacking, itemType, null);
    }

    public String getName() {return this.name;}

    public ItemTypes getItemType() {return this.itemType;}

    public Tiles getRelatedTile() {
        return this.relatedTile;
    }
}
