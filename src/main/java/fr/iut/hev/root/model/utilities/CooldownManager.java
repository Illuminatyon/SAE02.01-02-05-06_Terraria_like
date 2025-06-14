package fr.iut.hev.root.model.utilities;

import java.util.ArrayList;

public class CooldownManager {

    public static ArrayList<Cooldown> cooldowns = new ArrayList<>();

    public CooldownManager() {
    }

    public void allCooldownsTick() {
        for (Cooldown cooldowns : cooldowns) {
            cooldowns.ticks();
        }
    }
}
