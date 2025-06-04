package fr.iut.hev.root.model.enums;

public enum ConsumableStats {

    RAW_CHICKEN(3);

    private int healthRestored;

    ConsumableStats(int healthRestored) {
        this.healthRestored = healthRestored;
    }
}
