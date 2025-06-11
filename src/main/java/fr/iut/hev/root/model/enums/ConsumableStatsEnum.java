package fr.iut.hev.root.model.enums;

public enum ConsumableStatsEnum implements StatEnum{

    RAW_CHICKEN(3);

    private int healthRestored;

    ConsumableStatsEnum(int healthRestored) {
        this.healthRestored = healthRestored;
    }

    public int getHealthRestored() {return this.healthRestored;}
}
