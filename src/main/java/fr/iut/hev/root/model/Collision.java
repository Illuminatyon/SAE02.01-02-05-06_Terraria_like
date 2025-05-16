package fr.iut.hev.root.model;

public class Collision {
    private Actor actor;
    private TileMap tileMap;
    private int formatX;
    private int formatY;

    public Collision(Actor actor,TileMap tileMap, int formatX, int formatY){
        this.actor = actor;
        this.tileMap = tileMap;
        this.formatX = formatX;
        this.formatY = formatY;

    }

    public Actor getActor() {
        return this.actor;
    }
    public TileMap getTileMap() {return this.tileMap;}

    public Boolean hasTopCollision() {

        int x = this.actor.getX();
        int y = this.actor.getY();

        int[] indexTile = this.cooToTileInd(x,y);
        return this.tileMap.getTile(indexTile[0],indexTile[1]).getTile().getType().getHasCollision();
    }

    public int[] cooToTileInd(int x, int y) {
        int[] coordonnéesTile = {x/format, y/format};
        return coordonnéesTile;

    }
}

/*
faire un algorithme permettant de chercher le point sur le TileMap
- cooJ
- calcule de la tile concerné
- chercher Tile
- Et faire la vérification
*/


/*
    private void update() {
        velocityY += gravity.getGravityForce();

        x += velocityX;
        y += velocityY;

        // Réinitialise l'état au début
        onGround = false;

        // Vérifie les collisions avec les obstacles
        checkCollisions();

        // Vérifie si le joueur touche le sol uniquement si aucune collision n'a déjà mis onGround à true
        if (!onGround && gravityChecker.isOnGround(y, playerHeight, GROUND_Y)) {
            y = GROUND_Y - playerHeight;
            velocityY = 0;
            onGround = true;
        }
    }

    private void checkCollisions() {
        Rectangle2D playerBounds = new Rectangle2D(x, y, playerWidth, playerHeight);
        for (var obs : obstacleManager.getObstacles()) {
            Rectangle2D obsBounds = obs.getBounds();
            if (playerBounds.intersects(obsBounds)) {
                double playerBottom = y + playerHeight;
                double playerTop = y;
                double playerRight = x + playerWidth;
                double playerLeft = x;
                double obsTop = obs.getY();
                double obsBottom = obs.getY() + obs.getHeight();
                double obsLeft = obs.getX();
                double obsRight = obs.getX() + obs.getWidth();
                double overlapBottom = playerBottom - obsTop;
                double overlapTop = obsBottom - playerTop;
                double overlapRight = playerRight - obsLeft;
                double overlapLeft = obsRight - playerLeft;
                double minOverlapX = Math.min(overlapRight, overlapLeft);
                double minOverlapY = Math.min(overlapBottom, overlapTop);
                if (minOverlapY < minOverlapX) {
                    if (playerBottom > obsTop && playerTop < obsTop && velocityY >= 0) {
                        y = obsTop - playerHeight;
                        velocityY = 0;
                        onGround = true;
                    } else if (playerTop < obsBottom && playerBottom > obsBottom && velocityY < 0) {
                        y = obsBottom;
                        velocityY = 0;
                    }
                } else {
                    // Collision latérale
                    if (playerRight > obsLeft && playerLeft < obsLeft) {
                        x = obsLeft - playerWidth;
                    } else if (playerLeft < obsRight && playerRight > obsRight) {
                        x = obsRight;
                    }
                    if (onGround) {
                        velocityX = 0;
                    }
                }
            }
        }
    }

 */