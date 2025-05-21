package fr.iut.hev.root.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Collider {
    private TileMap tileMap;
    private Actor actor;
    private int offsetX;
    private int offsetY;
    private int marge;

    public Collider(TileMap tileMap, Actor actor) {
        this.tileMap = tileMap;
        this.actor = actor;
        this.offsetX = (tileMap.getWidth() * TileMap.format) / 2 + actor.getWidth() / 2;
        this.offsetY = (tileMap.getHeight() * TileMap.format) / 2 + actor.getHeight() / 2;
        this.marge = 1;
    }

    private boolean hasCollision(double x, double y, boolean negativeCheckX, boolean negativeCheckY) {
        int tileX = 0;
        int tileY = 0;
        if (negativeCheckX) {
            tileX = (int) ((x + offsetX - 1) / TileMap.format);
        } else {
            tileX = (int) ((x + offsetX) / TileMap.format);
        }

        if (negativeCheckY) {
            tileY = (int) ((y + offsetY - 1) / TileMap.format);
        } else {
            tileY = (int) ((y + offsetY) / TileMap.format);
        }

        if (tileX < 0 || tileY < 0 || tileX >= tileMap.getWidth() || tileY >= tileMap.getHeight()) {
            return true; // mettre des murs invisibles (colliders) au bords de la map
        }

        return tileMap.getTile(tileX, tileY).getTile().getType().getHasCollision();
    }

    public boolean hasCollisionTop(int n) {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();

        /*return hasCollision(x - w / 2 + marge, y - h / 2, false, true)
                || hasCollision(x + w / 2 - marge, y - h / 2, false, true);*/

        int lastTrue = -1;

        for (int i = n; i > 0; i--) {
            if (hasCollision(x - w / 2 + marge, y - h / 2 + i, false, true)
                    || hasCollision(x + w / 2 - marge, y - h / 2 + i, false, true)) {
                lastTrue = i;
            } else if (lastTrue >= 0) {
                actor.setPosY(actor.getPosY() + lastTrue);
                return true;
            }
        }

        if (hasCollision(x - w / 2 + marge, y - h / 2, false, true)
                || hasCollision(x + w / 2 - marge, y - h / 2, false, true)) {
            return true;
        }
        return false;
    }

    public boolean hasCollisionBottom(int n) {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();

        int lastTrue = -1;

        for (int i = n; i > 0; i--) {
            if (hasCollision(x - w / 2 + marge, y + h / 2 + i, false, false)
                    || hasCollision(x + w / 2 - marge, y + h / 2 + i, false, false)) {
                lastTrue = i;
            } else if (lastTrue >= 0) {
                actor.setPosY(actor.getPosY() + lastTrue);
                return true;
            }
        }

        if (hasCollision(x - w / 2 + marge, y + h / 2, false, false)
                || hasCollision(x + w / 2 - marge, y + h / 2, false, false)) {
            return true;
        }
        return false;
    }

    public boolean hasCollisionRight() {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();

        return hasCollision(x + w / 2, y + h / 2 - marge, false, false)
                || hasCollision(x + w / 2, y - h / 2 + marge, false, false);
    }

    public boolean hasCollisionLeft() {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();

        return hasCollision(x - w / 2, y + h / 2 - marge, true, false)
                || hasCollision(x - w / 2, y - h / 2 + marge, true, false);
    }
}