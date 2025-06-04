package fr.iut.hev.root.model.enums;

public enum Items {
    WOOD("wood",100,ItemTypes.RESOURCES),
    STONE("stone",100,ItemTypes.BLOCK),
    IRON_INGOT("iron_ingot",100,ItemTypes.RESOURCES),
    IRON_BLOCK("iron_block",100,ItemTypes.BLOCK),
    DIRT("dirt",100,ItemTypes.BLOCK),
    STICK("stick",100,ItemTypes.RESOURCES),
    FEATHER("feather",100,ItemTypes.RESOURCES),

    PIOCHE("pioche",1,ItemTypes.TOOL),
    HAX("hax",1,ItemTypes.TOOL),
    HAMMER("hammer",1,ItemTypes.TOOL),
    CRAFTING_TABLE("crafting_table",1,ItemTypes.TOOL),
    FURNACE("furnace",100,ItemTypes.TOOL),

    DAGGER("dagger",1,ItemTypes.WEAPON),

    IRON_HELMET("iron_helmet",1,ItemTypes.ARMOR_PIECE),
    IRON_CHESTPLATE("iron_chestplate",1,ItemTypes.ARMOR_PIECE),
    IRON_LEGGINGS("iron_leggings",1,ItemTypes.ARMOR_PIECE),

    RAW_CHICKEN("raw_chicken",100,ItemTypes.CONSUMABLE);

    private String name;
    private int limitStacking;
    private ItemTypes itemType;
    private ToolStats stats;

    Items(String name,int limitStacking,ItemTypes itemType) {
        this.name = name;
        this.limitStacking = limitStacking;
        this.itemType = itemType;
    }

    public String getName() {return this.name;}
    public ItemTypes getItemType() {return this.itemType;}
    public int getLimitStacking() {return this.limitStacking;}
}
