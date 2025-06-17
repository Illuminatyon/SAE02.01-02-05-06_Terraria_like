package fr.iut.hev.root.model.enums;

// Ca ou deux enums ?
public enum TilesEnum {
    AIR("air", TileTypesEnum.AIR,0),
    GRASS("grass", TileTypesEnum.BLOCK,8, ItemsEnum.DIRT, BlockTypesEnum.GROUND_TYPE),
    DIRT("dirt", TileTypesEnum.BLOCK,8, ItemsEnum.DIRT, BlockTypesEnum.GROUND_TYPE),
    STONE("stone", TileTypesEnum.BLOCK,12, ItemsEnum.STONE, BlockTypesEnum.ROCK_TYPE),
    IRON_ORE("iron_ore",TileTypesEnum.BLOCK,14,ItemsEnum.IRON_ORE,BlockTypesEnum.ROCK_TYPE),
    IRON_BLOCK("iron_block",TileTypesEnum.BLOCK,15,ItemsEnum.IRON_BLOCK,BlockTypesEnum.ROCK_TYPE),
    //WATER("water", TileTypes.LIQUID),
    //LAVA("lava", TileTypes.LIQUID);
    CRAFTING_TABLE("crafting_table", TileTypesEnum.UTILITIES,15, ItemsEnum.CRAFTING_TABLE, BlockTypesEnum.WOOD_TYPE),
    FURNACE("furnace", TileTypesEnum.UTILITIES,20, ItemsEnum.FURNACE);

    private final String name;
    private final TileTypesEnum type;
    private final int maxHealth;
    private final ItemsEnum relatedItem;
    private BlockTypesEnum blockTypesEnum;

    TilesEnum(String name, TileTypesEnum type, int maxHealth, ItemsEnum relatedItem, BlockTypesEnum blockTypesEnum) {
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
        this.relatedItem = relatedItem;
        this.blockTypesEnum = blockTypesEnum;
    }

    TilesEnum(String name, TileTypesEnum type, int maxHealth, ItemsEnum relatedItem) {
        this(name, type, maxHealth,relatedItem,null);
    }

    TilesEnum(String name, TileTypesEnum type, int maxHealth) {
        this(name, type, maxHealth, null);
    }

    public TileTypesEnum getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxHealth() {return this.maxHealth;}

    public ItemsEnum getRelatedItem() {
        return this.relatedItem;
    }
    public BlockTypesEnum getBlockTypesEnum() {return this.blockTypesEnum;}
}
