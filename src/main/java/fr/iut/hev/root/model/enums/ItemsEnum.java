package fr.iut.hev.root.model.enums;

public enum ItemsEnum {
    WOOD("wood",100,0, ItemTypesEnum.RESOURCES),
    STONE("stone",100,0.2, ItemTypesEnum.BLOCK, TilesEnum.STONE),
    IRON_INGOT("iron_ingot",100,0, ItemTypesEnum.RESOURCES),
    IRON_BLOCK("iron_block",100,0.2, ItemTypesEnum.BLOCK),
    DIRT("dirt",100,0.2, ItemTypesEnum.BLOCK, TilesEnum.DIRT),
    STICK("stick",100,0, ItemTypesEnum.RESOURCES),
    FEATHER("feather",100,0, ItemTypesEnum.RESOURCES),

    PIOCHE("pioche",1,0, ItemTypesEnum.TOOL),
    HAX("hax",1,0, ItemTypesEnum.TOOL),
    HAMMER("hammer",1,0, ItemTypesEnum.TOOL),
    CRAFTING_TABLE("crafting_table",1,0, ItemTypesEnum.TOOL, TilesEnum.CRAFTING_TABLE),
    FURNACE("furnace",100,0, ItemTypesEnum.TOOL, TilesEnum.FURNACE),

    DAGGER("dagger",1,1, ItemTypesEnum.WEAPON),

    IRON_HELMET("iron_helmet",1,0, ItemTypesEnum.ARMOR_PIECE),
    IRON_CHESTPLATE("iron_chestplate",1,0, ItemTypesEnum.ARMOR_PIECE),
    IRON_LEGGINGS("iron_leggings",1,0, ItemTypesEnum.ARMOR_PIECE),

    RAW_CHICKEN("raw_chicken",100,1, ItemTypesEnum.CONSUMABLE,ItemStatsEnum.RAW_CHICKEN);

    private String name;
    private int limitStacking;
    private final ItemTypesEnum itemType;
    private final TilesEnum relatedTile;
    private double cooldown;
    private ItemStatsEnum stats;

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType, TilesEnum relatedTile,ItemStatsEnum stats) {
        this.name = name;
        this.limitStacking = limitStacking;
        this.cooldown = cooldown;
        this.itemType = itemType;
        this.relatedTile = relatedTile;
        this.stats = stats;
    }

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType,ItemStatsEnum stats) {
        this(name, limitStacking, cooldown, itemType, null,stats);
    }

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType) {
        this(name, limitStacking, cooldown, itemType, null,null);
    }

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType, TilesEnum relatedTile) {
        this(name, limitStacking, cooldown, itemType, relatedTile,null);
    }

    public String getName() {return this.name;}
    public ItemTypesEnum getItemType() {return this.itemType;}
    public int getLimitStacking() {return this.limitStacking;}
    public TilesEnum getRelatedTile() {
        return this.relatedTile;
    }
    public double getCooldown() {
        return cooldown;
    }
    public ItemStatsEnum getStats() {return stats;}
}
