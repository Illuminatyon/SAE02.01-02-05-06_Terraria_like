package fr.iut.hev.root.model.enums;

public enum Items {
    WOOD("wood",ItemTypes.RESOURCES),
    STONE("stone",ItemTypes.BLOCK),
    IRON_INGOT("iron_ingot",ItemTypes.RESOURCES),
    IRON_BLOCK("iron_block",ItemTypes.BLOCK),
    DIRT("dirt",ItemTypes.BLOCK),
    STICK("stick",ItemTypes.RESOURCES),
    FEATHER("feather",ItemTypes.RESOURCES),

    PIOCHE("pioche",ItemTypes.TOOL),
    HAX("hax",ItemTypes.TOOL),
    HAMMER("hammer",ItemTypes.TOOL),
    CRAFTING_TABLE("crafting_table",ItemTypes.TOOL),
    FURNACE("furnace",ItemTypes.TOOL),

    DAGGER("dagger",ItemTypes.WEAPON),

    IRON_HELMET("iron_helmet",ItemTypes.ARMOR_PIECE),
    IRON_CHESTPLATE("iron_chestplate",ItemTypes.ARMOR_PIECE),
    IRON_LEGGINGS("iron_leggings",ItemTypes.ARMOR_PIECE);

    private String name;
    private ItemTypes itemType;

    Items(String name, ItemTypes itemType) {
        this.name = name;
        this.itemType = itemType;
    }

    public String getName() {return this.name;}
    public ItemTypes getItemType() {return this.itemType;}
}
