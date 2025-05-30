package fr.iut.hev.root.model.enums;

public enum ItemTypes {
    TOOL("tool",false),
    WEAPON("weapon",false),
    CONSUMABLE("consumable",true),
    ARMOR_PIECE("armor_piece",false),
    RESOURCES("resources",false),
    BLOCK("block",false);

    private final String name;
    private final boolean isConsumable;

    ItemTypes(String name, boolean isConsumable) {
        this.name = name;
        this.isConsumable = isConsumable;
    }

    public boolean getIsConsumable() {
        return this.isConsumable;
    }
    public String getName() {return this.name;}
}
