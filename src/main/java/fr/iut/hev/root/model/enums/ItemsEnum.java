package fr.iut.hev.root.model.enums;

public enum ItemsEnum {
    WOOD("wood",100,0, ItemTypesEnum.RESOURCES),
    STONE("stone",100,0.2, ItemTypesEnum.BLOCK),
    IRON_INGOT("iron_ingot",100,0, ItemTypesEnum.RESOURCES),
    IRON_BLOCK("iron_block",100,0.2, ItemTypesEnum.BLOCK),
    IRON_ORE("iron_ore",100,0,ItemTypesEnum.BLOCK),
    DIRT("dirt",100,0.2, ItemTypesEnum.BLOCK),
    STICK("stick",100,0, ItemTypesEnum.RESOURCES),
    FEATHER("feather",100,0, ItemTypesEnum.RESOURCES),

    WOODEN_PICKAXE("wooden_pickaxe",1,0, ItemTypesEnum.TOOL,ItemStatsEnum.WOODEN_PICKAXE),
    WOODEN_HAX("wooden_hax",1,0, ItemTypesEnum.TOOL,ItemStatsEnum.WOODEN_HAX),
    WOODEN_SHOVEL("wooden_shovel",1,0,ItemTypesEnum.TOOL,ItemStatsEnum.WOODEN_SHOVEL),
    HAMMER("hammer",1,0, ItemTypesEnum.TOOL,ItemStatsEnum.HAMMER),
    CRAFTING_TABLE("crafting_table",1,0, ItemTypesEnum.UTILITY),
    FURNACE("furnace",100,0, ItemTypesEnum.UTILITY),

    DAGGER("dagger",1,1, ItemTypesEnum.WEAPON,ItemStatsEnum.DAGGER),

    IRON_HELMET("iron_helmet",1,0, ItemTypesEnum.ARMOR_PIECE,ItemStatsEnum.IRON_HELMET),
    IRON_CHESTPLATE("iron_chestplate",1,0, ItemTypesEnum.ARMOR_PIECE,ItemStatsEnum.IRON_CHESTPLATE),
    IRON_LEGGINGS("iron_leggings",1,0, ItemTypesEnum.ARMOR_PIECE,ItemStatsEnum.IRON_LEGGINGS),

    RAW_CHICKEN("raw_chicken",100,1.5, ItemTypesEnum.CONSUMABLE,ItemStatsEnum.RAW_CHICKEN),
    CACA("caca",100,1,ItemTypesEnum.RESOURCES);

    private String name;
    private int limitStacking;
    private final ItemTypesEnum itemType;
    private TilesEnum relatedTile;
    private ItemStatsEnum stats;
    private double cooldown;

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType, TilesEnum relatedTile,ItemStatsEnum stats) {
        this.name = name;
        this.limitStacking = limitStacking;
        this.itemType = itemType;
        this.relatedTile = relatedTile;
        this.stats = stats;
        this.cooldown = cooldown;
    }

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType,ItemStatsEnum stats) {
        //constructor for stats items (tools, weapons, armor pieces, consumable)
        this(name, limitStacking, cooldown, itemType, null,stats);
    }

    ItemsEnum(String name, int limitStacking, double cooldown, ItemTypesEnum itemType) {
        //constructor for ressources and blocks
        this(name, limitStacking, cooldown, itemType, null,null);
    }

    public String getName() {return this.name;}
    public ItemTypesEnum getItemType() {return this.itemType;}
    public int getLimitStacking() {return this.limitStacking;}
    public TilesEnum getRelatedTile() {
        return this.relatedTile;
    }
    public double getCooldown() {
        return this.cooldown;
    }
    public ItemStatsEnum getStats() {return stats;}
    public void setRelatedTile(TilesEnum tilesEnum) {this.relatedTile = tilesEnum;}

    public void itemEnumInit() {
        for (TilesEnum tilesEnum : TilesEnum.values()) {
            if (this.getName().equals(tilesEnum.getName())) {
                this.setRelatedTile(tilesEnum);
            }
        }
    }
}
