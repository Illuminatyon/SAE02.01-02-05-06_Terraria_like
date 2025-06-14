package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.PlayerMouvements;
import fr.iut.hev.root.model.pathfinder.Pathfinder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends Actor {
    private List<Pathfinder.PathPoint> currentPath;
    private int currentPathIndex = 0;
    private Inventory inventory;
    private final Set<PlayerMouvements> playerMouvements;
    private BooleanProperty isMoving;

    public Player(int posX, int posY, int width, int height, TileMap tileMap, int moveSpeed, int jumpForce, int reach, ActorEnum actor) {
        super(posX, posY, width, height, tileMap,10, moveSpeed, jumpForce,reach, actor);
        this.inventory = new Inventory();
        this.playerMouvements = new HashSet<>();
        this.isMoving = new SimpleBooleanProperty(false);
    }

    public void setPath(List<Pathfinder.PathPoint> path) {
        this.currentPath = path;
        this.currentPathIndex = 0;
        if (path != null && !path.isEmpty()) {
            System.out.println("Nouveau chemin trouvé avec " + path.size() + " points");
        }
    }

    // Getter pour la propriété isMoving
    public BooleanProperty isMovingProperty() {
        return isMoving;
    }

    // Méthode pour mettre à jour l'état de mouvement
    public void setMoving(boolean moving) {
        this.isMoving.set(moving);
    }

    public void addPlayerMouvements(PlayerMouvements playerMouvements) {
        this.playerMouvements.add(playerMouvements);
    }

    public void removePlayerMouvements(PlayerMouvements playerMouvements) {
        this.playerMouvements.remove(playerMouvements);
    }

    public Set<PlayerMouvements> getPlayerMouvements() {return playerMouvements;}

    public void update() {
        updatePosition();
        for (Loot loot : Loot.lootOnMapProperty.get()) {
        if (getCollider().intersectsWith(loot.getCollider())) {
            pickUp(loot);
        }
    }
}

    @Override
    public void updatePosition() {
        applyGravity();
        updateHorizontalMovement();
        updateVerticalMovement();

        // Si nous avons un chemin à suivre
        if (currentPath != null && !currentPath.isEmpty() && currentPathIndex < currentPath.size()) {
            Pathfinder.PathPoint target = currentPath.get(currentPathIndex);

            // Convertir la position cible en coordonnées monde
            int targetWorldX = target.x * TileMap.format;
            int targetWorldY = target.y * TileMap.format;

            // Convertir notre position actuelle en coordonnées tuile
            int currentTileX = (int)(getPosX() / TileMap.format);
            int currentTileY = (int)((getPosY() + getHeight()) / TileMap.format);

            // Si nous sommes arrivés à la position cible (ou proches)
            if (Math.abs(currentTileX - target.x) <= 1 && Math.abs(currentTileY - target.y) <= 1) {
                currentPathIndex++;
                if (currentPathIndex >= currentPath.size()) {
                    // Nous avons atteint la fin du chemin
                    currentPath = null;
                    currentPathIndex = 0;
                    System.out.println("Destination atteinte!");
                }
            } else {
                // Déplacer le joueur vers la position cible
                if (targetWorldX > getPosX() + 1) {
                    // Se déplacer vers la droite
                    playerMouvements.add(PlayerMouvements.MOVE_RIGHT);
                    playerMouvements.remove(PlayerMouvements.MOVE_LEFT);
                } else if (targetWorldX < getPosX() - 1) {
                    // Se déplacer vers la gauche
                    playerMouvements.add(PlayerMouvements.MOVE_LEFT);
                    playerMouvements.remove(PlayerMouvements.MOVE_RIGHT);
                } else {
                    // Nous sommes alignés horizontalement, arrêter le mouvement horizontal
                    playerMouvements.remove(PlayerMouvements.MOVE_LEFT);
                    playerMouvements.remove(PlayerMouvements.MOVE_RIGHT);
                }

                // Gérer le saut si nécessaire
                if (target.y < currentTileY && currentPathIndex < currentPath.size() - 1) {
                    // La cible est plus haute, peut-être besoin de sauter
                    // Vérifions si nous devons sauter pour atteindre la cible
                    Pathfinder.PathPoint nextPoint = currentPath.get(currentPathIndex + 1);
                    if (nextPoint.isJumping) {
                        if (!getIsJumping() && getCollider().hasCollisionBottom(getVelocityY() - Gravity.getGravityForce())) {
                            playerMouvements.add(PlayerMouvements.JUMP);
                        }
                    }
                }
            }
        }

        // Mettre à jour la position
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());

        // Logique centralisée pour déterminer si le joueur est en mouvement
        boolean isCurrentlyMoving = false;
        // Mouvement horizontal
        if (playerMouvements.contains(PlayerMouvements.MOVE_LEFT) || playerMouvements.contains(PlayerMouvements.MOVE_RIGHT)) {
            if (super.getVelocityX() != 0) { // S'il y a une vélocité horizontale effective
                isCurrentlyMoving = true;
            }
        }
        // Mouvement vertical (saut ou chute)
        if (super.getIsJumping() || super.getVelocityY() != 0) { // Si le joueur est en train de sauter ou de tomber
            isCurrentlyMoving = true;
        }

        // Si aucune touche de mouvement n'est pressée et pas en saut/chute et vélocité à zéro, il est statique.
        // Sinon, il est en mouvement.
        if (super.getVelocityX() == 0 && super.getVelocityY() == 0 && !playerMouvements.contains(PlayerMouvements.JUMP) && !playerMouvements.contains(PlayerMouvements.MOVE_LEFT) && !playerMouvements.contains(PlayerMouvements.MOVE_RIGHT)) {
            isCurrentlyMoving = false;
        }

        if (isMoving.get() != isCurrentlyMoving) {
            System.out.println("Player isMoving changed from " + isMoving.get() + " to " + isCurrentlyMoving);
            setMoving(isCurrentlyMoving);
        }

        // TMP
        for (Loot loot : Loot.lootOnMapProperty.get()) {
            if (getCollider().intersectsWith(loot.getCollider())) {
                pickUp(loot);
            }
        }
        boolean horizontalMove = playerMouvements.contains(PlayerMouvements.MOVE_LEFT) || playerMouvements.contains(PlayerMouvements.MOVE_RIGHT);
        boolean verticalMove = playerMouvements.contains(PlayerMouvements.JUMP) || super.getVelocityY() != 0; // Si en saut ou en chute

        if (horizontalMove || verticalMove) {
            setMoving(true);
        } else {
            setMoving(false);
        }
    }

    @Override
    public void updateHorizontalMovement() {
        boolean wasMoving = isMoving.get(); // Sauvegarder l'état précédent
        boolean currentlyMoving = false; // Indicateur pour le mouvement horizontal

        if (playerMouvements.contains(PlayerMouvements.MOVE_RIGHT)
                && playerMouvements.contains(PlayerMouvements.MOVE_LEFT)) {
            super.setVelocityX(0);
        } else if (playerMouvements.contains(PlayerMouvements.MOVE_RIGHT)) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
                currentlyMoving = true; // Détecte le mouvement
            } else {
                super.setVelocityX(0);
            }
        } else if (playerMouvements.contains(PlayerMouvements.MOVE_LEFT)) {
            super.setLookDirection(LookDirections.LEFT);
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
                currentlyMoving = true; // Détecte le mouvement
            } else {
                super.setVelocityX(0);
            }
        } else {
            super.setVelocityX(0);
        }

        // Mettez à jour isMoving en fonction du mouvement horizontal et vertical
        // Nous allons consolider la logique dans updatePosition()
        // ou ici, mais soyez clair sur ce qui définit "isMoving"
        // Pour l'instant, ne changeons pas isMoving ici pour voir le log global
    }

    @Override
    public void updateVerticalMovement() {
        if (playerMouvements.contains(PlayerMouvements.JUMP) && super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping()) {
            super.setIsJumping(true);
            super.setJumpingTestDecay(0);
            setMoving(true);
        } else if (super.getIsJumping()) {
            if (super.getJumpingTestDecay() == super.getJumpForce()) {
                super.setVelocityY(0);
                super.setIsJumping(false);
            } else if (!super.getCollider().hasCollisionTop(super.getVelocityY() + 1)) {
                super.setVelocityY(-super.getJumpForce() + super.getJumpingTestDecay());
                super.setJumpingTestDecay(super.getJumpingTestDecay() + 1);
                setMoving(true);
            } else {
                super.setIsJumping(false);
                setMoving(false);
            }
        }
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping() && super.getVelocityY() > 0) {
            setMoving(true);
        }
    }

    public void pickUp(Loot loot) {
        this.inventory.add(loot.getItem(), loot.getQuantity());
        loot.removeSelf();
    }

    public void updateBreaksBlock(int x,int y, TileMap tileMap) {
        tileMap.tileGetsMined(x,y);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
