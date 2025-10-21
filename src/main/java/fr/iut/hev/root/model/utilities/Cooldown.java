package fr.iut.hev.root.model.utilities;

import static fr.iut.hev.root.model.utilities.CooldownManager.cooldowns;

public class Cooldown {

    /**
     * <p>This class is in charge of creating and managing your cooldowns.
     * Your cooldown is ready to use once it's been instanced by its constructor.
     * Your cooldown will start ticking once the method {@link #start()} is called.
     * Then, it will continue to tick with the {@link #ticks()} method (called by the {@link CooldownManager})
     * until the {@link #ticks} attribut equals the {@link #limit} attribut.
     * At this point, the {@link CooldownManager} calls the {@link #stop()} in order to stop the cooldown.
     * </p>
     */

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
        System.out.println(ticks);
        if (ticks >= limit) {
            stop();
        }

    }

    public void start() {
        setOnGoing(true);
        ticks = 0;
        cooldowns.add(this);
    }

    public void stop() {
        setOnGoing(false);
    }

    private void setOnGoing(boolean onGoing) {this.onGoing = onGoing;}
    public boolean getOnGoing() {return this.onGoing;}
    public void setLimit(double secondes) {this.limit = (int)(secondes*60);}
}
