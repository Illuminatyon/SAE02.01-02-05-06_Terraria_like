package fr.iut.hev.root.model.utilities;

import java.util.ArrayList;

public class CooldownManager {

    public static ArrayList<Cooldown> cooldowns = new ArrayList<>();

    public CooldownManager() {
    }

    public void allCooldownsTick() {
        cooldowns.removeIf(cooldown1 -> !cooldown1.getOnGoing());
        for (Cooldown cooldown : cooldowns) {
            cooldown.ticks();
        }
    }
}
