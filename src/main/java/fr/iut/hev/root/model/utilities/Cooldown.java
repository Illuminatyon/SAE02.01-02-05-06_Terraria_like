package fr.iut.hev.root.model.utilities;

import static fr.iut.hev.root.model.utilities.CooldownManager.cooldowns;

public class Cooldown {

    private int ticks;
    private int limit;
    private boolean onGoing;

    public Cooldown(double secondes) {
        this.limit = (int)(secondes*60);
        this.ticks = 0;
        this.onGoing = false;
    }

    public void ticks() {
        this.ticks++;
        if (ticks >= limit) {
            stop();
        }

    }

    public void start() {
        setOnGoing(true);
        cooldowns.add(this);
    }

    public void stop() {
        setOnGoing(false);
        cooldowns.remove(this);
    }

    public void setOnGoing(boolean onGoing) {this.onGoing = onGoing;}
    public boolean getOnGoing() {return this.onGoing;}
    public void setLimit(double secondes) {this.limit = (int)(secondes*60);}
}
