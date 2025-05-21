package fr.iut.hev.root.model;

import javafx.animation.AnimationTimer;

public abstract class FixedAnimationTimer extends AnimationTimer {

    private static final double UPDATE_RATE = 60.0;
    private static final double UPDATE_INTERVAL = 1_000_000_000.0 / UPDATE_RATE;

    private long previousTime = 0;
    private double lag = 0;

    private boolean running = false;

    @Override
    public void start() {
        previousTime = System.nanoTime();
        lag = 0;
        running = true;
        super.start();
    }

    @Override
    public void stop() {
        running = false;
        super.stop();
    }

    @Override
    public void handle(long now) {
        if (!running) return;

        double elapsed = now - previousTime;
        previousTime = now;
        lag += elapsed;

        while (lag >= UPDATE_INTERVAL) {
            update();
            lag -= UPDATE_INTERVAL;
        }
    }

    protected abstract void update();
}