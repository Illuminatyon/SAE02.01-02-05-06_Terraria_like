package fr.iut.hev.root.model.enums;

public enum TileTypesEnum {
    AIR("AIR", false, false), // _ A la fin pour les vrais trucs
    LIQUID("LIQUID", false, false),
    BACKGROUND("BACKGROUND",false,true),
    UTILITIES("UTILITIES",true,true),
    BLOCK("BLOCK", true, true);

    private final String name;
    private final boolean hasCollision;
    private final boolean isBreakable;

    TileTypesEnum(String name, boolean hasCollision, boolean isBreakable) {
        this.name = "TT_".concat(name);
        this.hasCollision = hasCollision;
        this.isBreakable = isBreakable;
    }

    public boolean getHasCollision() {
        return this.hasCollision;
    }

    public boolean getIsBreakable() {
        return this.isBreakable;
    }

    public String toString() {
        return this.name;
    }
}
