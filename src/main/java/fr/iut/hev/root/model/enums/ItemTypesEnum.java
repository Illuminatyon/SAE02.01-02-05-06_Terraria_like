package fr.iut.hev.root.model.enums;

public enum ItemTypesEnum {

    TOOL("tool",false),
    WEAPON("weapon",false),
    CONSUMABLE("consumable",true),
    ARMOR_PIECE("armor_piece",false),
    RESOURCES("resources",false),
    BLOCK("block",false),
    UTILITY("utility",false);

    private final String name;
    private final boolean isConsumable;

    ItemTypesEnum(String name, boolean isConsumable) {
        this.name = name;
        this.isConsumable = isConsumable;
    }

    public boolean getIsConsumable() {
        return this.isConsumable;
    } // TODO : A retirer mais ce sert à rien
    public String getName() {return this.name;}
}
