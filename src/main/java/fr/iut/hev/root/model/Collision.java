package fr.iut.hev.root.model;

import static fr.iut.hev.root.model.TileMap.format;

public class Collision {

    private Actor actor;
    private TileMap tileMap;
    private int formatX; //Taille en nombre de tile horizontale de la collision (de l'acteur en question par conséquent)
    private int formatY; //Même chose qu'au dessus mais verticalement

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

    public boolean hasTopCollision() {
        /**
         * Renvoie True s'il existe une collision avec un bloc de terrain au dessus de l'acteur.
         */

        boolean hasCollision = false;
        int x = this.actor.getX() + (format*formatX)/2;
        int y = this.actor.getY() + (format*formatY)/2;
        int i = 0, j = 0;

        while (i < formatX && !hasCollision) {
            while (j < 2 && !hasCollision) {
                hasCollision = checkCollisionFromTileOnPx(cooToTileInd(x,y));
                if (j == 0)
                    x-=(format - 1);
                j++;
            }
            j = 0;
            x--;
            i++;
        }
        return hasCollision;
    }

    public boolean hasBottomCollision() {
        /**
         * Renvoie True s'il existe une collision avec un bloc de terrain en dessous de l'acteur.
         */

        boolean hasCollision = false;
        int x = this.actor.getX() - (format*formatX)/2 + 1;
        int y = this.actor.getY() - (format*formatY)/2 - 1;
        int i = 0, j = 0;

        while (i < formatX && !hasCollision) {
            while (j < 2 && !hasCollision) {
                hasCollision = checkCollisionFromTileOnPx(cooToTileInd(x,y));
                if (j == 0)
                    x+=(format - 1);
                j++;
            }
            j = 0;
            x++;
            i++;
        }
        return hasCollision;
    }

    public boolean hasRightCollision() {
        /**
         * Renvoie True s'il existe une collision avec un bloc de terrain à droite de l'acteur.
         */

        boolean hasCollision = false;
        int x = this.actor.getX() + (format*formatX)/2 + 1;
        int y = this.actor.getY() - (format*formatY)/2;
        int i = 0, j = 0;

        while (i < formatX && !hasCollision) {
            while (j < 2 && !hasCollision) {
                hasCollision = checkCollisionFromTileOnPx(cooToTileInd(x,y));
                if (j == 0)
                    y+=(format - 1);
                j++;
            }
            j = 0;
            y++;
            i++;
        }
        return hasCollision;
    }

    public boolean hasLeftCollision() {
        /**
         * Renvoie True s'il existe une collision avec un bloc de terrain à gauche de l'acteur.
         */

        boolean hasCollision = false;
        int x = this.actor.getX() - (format*formatX)/2;
        int y = this.actor.getY() + (format*formatY)/2 - 1;
        int i = 0, j = 0;

        while (i < formatX && !hasCollision) {
            while (j < 2 && !hasCollision) {
                hasCollision = checkCollisionFromTileOnPx(cooToTileInd(x,y));
                if (j == 0)
                    y-=(format - 1);
                j++;
            }
            j = 0;
            y--;
            i++;
        }
        return hasCollision;
    }

    public int[] cooToTileInd(int x, int y) {
        /**
         * Renvoie dans un tableau de int à une dimension dans la forme [indice 1ere dimension, indice 2eme dimension]
         * les indices de la position de la Tile, sur laquelle se trouve le point (x,y), dans la TileMap.
         */

        int[] coordonnéesTile = {x/format, y/format};
        return coordonnéesTile;

    }

    public boolean checkCollisionFromTileOnPx(int [] cooTile) {
        /**
         * Renvoie la présence d'une collision sur la Tile de coordonnées cooTile sous la forme [indice 1ere dimension,
         * indice 2eme dimension].
         */
        return this.tileMap.getTile(cooTile[0],cooTile[1]).getTile().getType().getHasCollision();
    }

    //Eventuellement regrouper les 4 méthode de check de collision en une pour éviter la répétition de code.
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