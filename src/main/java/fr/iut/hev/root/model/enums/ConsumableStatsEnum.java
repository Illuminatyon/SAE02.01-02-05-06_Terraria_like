package fr.iut.hev.root.model.enums;

public enum ConsumableStatsEnum {

    RAW_CHICKEN(3);

    private int healthRestored;

    ConsumableStatsEnum(int healthRestored) {
        this.healthRestored = healthRestored;
    }

    public int getHealthRestored() {return this.healthRestored;}
}
