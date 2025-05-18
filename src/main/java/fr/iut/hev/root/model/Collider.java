package fr.iut.hev.root.model;

public class Collider {
    private TileMap tileMap;
    private Actor actor;
    private int offsetX;
    private int offsetY;

    public Collider(TileMap tileMap, Actor actor) {
        this.tileMap = tileMap;
        this.actor = actor;
        this.offsetX = tileMap.getWidth() * TileMap.format / 2;
        this.offsetY = tileMap.getHeight() * TileMap.format / 2;
    }

    private boolean hasCollision(double x, double y) {
        int tileX = (int)((x + offsetX) / TileMap.format);
        int tileY = (int)((y + offsetY) / TileMap.format);

        if (tileX < 0 || tileY < 0 || tileX >= tileMap.getWidth() || tileY >= tileMap.getHeight()) {
            return true; // mettre des murs invisibles (colliders) au bords de la map
        }

        return tileMap.getTile(tileX, tileY).getTile().getType().getHasCollision();
    }

    public boolean hasCollisionTop() {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();
        return hasCollision(x - w / 2 + 1, y - h / 2)
                || hasCollision(x + w / 2 - 1, y - h / 2);
    }

    public boolean hasCollisionBottom(/*int n, int yMultiplier*/) {
        double x = actor.getPosX();
        double y = actor.getPosY() /*+ yMultiplier*/;
        double w = actor.getWidth();
        double h = actor.getHeight();

        //if (n == 0) {
            return hasCollision(x - w / 2 + 1, y + h / 2)
                    || hasCollision(x + w / 2 - 1, y + h / 2);
        //} else {
        //    return hasCollisionBottom(n - 1, n + yMultiplier);
        //}
    }

    public boolean hasCollisionRight() {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();
        return hasCollision(x + w / 2, y + h / 2 - 1)
                || hasCollision(x + w / 2, y - h / 2 + 1);
    }

    public boolean hasCollisionLeft() {
        double x = actor.getPosX();
        double y = actor.getPosY();
        double w = actor.getWidth();
        double h = actor.getHeight();
        return hasCollision(x - w / 2, y + h / 2 - 1)
                || hasCollision(x - w / 2, y - h / 2 + 1);
    }
}