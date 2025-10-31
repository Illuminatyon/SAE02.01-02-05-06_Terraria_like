package fr.iut.hev.root.model.physics;

import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.entities.Entity;

public class Collider {
    private TileMap tileMap;
    private Entity entity;
    private int offsetX;
    private int offsetY;
    private int marge;

    // TODO : On pourrait potentiellement la découpé un peu de façon à ne pas avoir des fonctions qui font 100 lignes
    // TODO : ce serait plus facile à modifier donc bon

    public Collider(TileMap tileMap, Entity entity) {
        this.tileMap = tileMap;
        this.entity = entity;
        this.marge = 1;
        this.offsetX = entity.getWidth() / 2;
        this.offsetY = entity.getHeight() / 2;
    }

    private boolean hasCollision(double x, double y, boolean negativeCheckX, boolean negativeCheckY) {
        int tileX = 0;
        int tileY = 0;
        /*if (negativeCheckX) {
            tileX = (int) ((x + offsetX - 1) / TileMap.format * 2);
        } else {
            tileX = (int) ((x + offsetX) / TileMap.format * 2);
        }

        if (negativeCheckY) {
            tileY = (int) ((y + offsetY - 1) / TileMap.format);
        } else {
            tileY = (int) ((y + offsetY) / TileMap.format);
        }*/
        tileX = (int)x / TileMap.format;
        tileY = (int)y / TileMap.format;

        if (tileX < 0 || tileY < 0 || tileX >= tileMap.getWidth() || tileY >= tileMap.getHeight()) {
            return true; // mettre des murs invisibles (colliders) au bords de la map
        }

        return tileMap.getTile(tileX, tileY).getTileEnum().getType().getHasCollision();
    }

    public boolean hasCollisionTop(int n) {
        double x = entity.getPosX();
        double y = entity.getPosY();
        double w = entity.getWidth();
        double h = entity.getHeight();

        int lastTrue = -1;

        for (int i = n; i > 0; i--) {
            boolean collision = hasCollision(x - w / 2 + marge, y - h / 2 + i, false, true)
                    || hasCollision(x + w / 2 - marge, y - h / 2 + i, false, true);
            if (!collision) {
                for (int checkPoint = TileMap.format; checkPoint < w; i += TileMap.format) {
                    collision |= hasCollision(x - w / 2 + checkPoint, y - h / 2 + i, false, true);
                }
            }

            if (collision) {
                lastTrue = i;
            } else if (lastTrue >= 0) {
                entity.setPosY(entity.getPosY() + lastTrue);
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
        double x = entity.getPosX();
        double y = entity.getPosY();
        double w = entity.getWidth();
        double h = entity.getHeight();

        int lastTrue = -1;

        for (int i = n; i > 0; i--) {
            boolean collision = hasCollision(x - w / 2 + marge, y + h / 2 + i, false, false)
                    || hasCollision(x + w / 2 - marge, y + h / 2 + i, false, false);
            if (!collision) {
                for (int checkPoint = TileMap.format; checkPoint < w; i += TileMap.format) {
                    collision |= hasCollision(x - w / 2 + checkPoint, y + h / 2 + i, false, false);
                }
            }

            if (collision) {
                lastTrue = i;
            } else if (lastTrue >= 0) {
                entity.setPosY(entity.getPosY() + lastTrue);
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
        double x = entity.getPosX();
        double y = entity.getPosY();
        double w = entity.getWidth();
        double h = entity.getHeight();

        boolean collision = hasCollision(x + w / 2, y + h / 2 - marge, false, false)
                || hasCollision(x + w / 2, y - h / 2 + marge, false, false);
        if (!collision) {
            for (int checkPoint = TileMap.format; checkPoint < h; checkPoint += TileMap.format) {
                collision |= hasCollision(x + w / 2, y - h / 2 + checkPoint, false, false);
            }
        }

        return collision;
    }

    public boolean hasCollisionLeft() {
        double x = entity.getPosX();
        double y = entity.getPosY();
        double w = entity.getWidth();
        double h = entity.getHeight();

        boolean collision = hasCollision(x - w / 2, y + h / 2 - marge, true, false)
                || hasCollision(x - w / 2, y - h / 2 + marge, true, false);
        if (!collision) {
            for (int checkPoint = TileMap.format; checkPoint < h; checkPoint += TileMap.format) {
                collision |= hasCollision(x - w / 2, y - h / 2 + checkPoint, true, false);
            }
        }

        return collision;
    }

    /*public boolean intersectsWith(Collider other) {
        return entity.getPosX() < other.entity.getPosX() + other.entity.getWidth() &&
                entity.getPosX() + entity.getWidth() > other.entity.getPosX() &&
                entity.getPosY() < other.entity.getPosY() + other.entity.getHeight() &&
                entity.getPosY() + entity.getHeight() > other.entity.getPosY();
    }*/

    public boolean intersectsWith(Collider other) {
        double thisLeft = entity.getPosX() + this.offsetX - entity.getWidth() / 2.0;
        double thisRight = entity.getPosX() + this.offsetX + entity.getWidth() / 2.0;
        double thisTop = entity.getPosY() + this.offsetY - entity.getHeight() / 2.0;
        double thisBottom = entity.getPosY() + this.offsetY + entity.getHeight() / 2.0;

        double otherLeft = other.entity.getPosX() + other.offsetX - other.entity.getWidth() / 2.0;
        double otherRight = other.entity.getPosX() + other.offsetX + other.entity.getWidth() / 2.0;
        double otherTop = other.entity.getPosY() + other.offsetY - other.entity.getHeight() / 2.0;
        double otherBottom = other.entity.getPosY() + other.offsetY + other.entity.getHeight() / 2.0;

        return thisRight > otherLeft &&
                thisLeft < otherRight &&
                thisBottom > otherTop &&
                thisTop < otherBottom;
    }
}