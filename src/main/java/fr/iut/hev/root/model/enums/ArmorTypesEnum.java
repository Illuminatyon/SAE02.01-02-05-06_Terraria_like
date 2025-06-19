package fr.iut.hev.root.model.enums;

public enum ArmorTypesEnum {
    HELMET("helmet", 0),
    CHESTPLATE("chestplate", 1),
    LEGGINGS("leggings", 2),
    BOOTS("boots", 3);

    private final String name;
    private final int slotIndex;

    ArmorTypesEnum(String name, int slotIndex) {
        this.name = name;
        this.slotIndex = slotIndex;
    }

    public String getName() {
        return this.name;
    }

    public int getSlotIndex() {
        return this.slotIndex;
    }
}