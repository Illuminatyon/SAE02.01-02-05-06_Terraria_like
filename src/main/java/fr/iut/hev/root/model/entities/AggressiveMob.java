package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.pathfinding.AStar;
import fr.iut.hev.root.model.pathfinding.Point;
import javafx.scene.layout.Pane;

import java.util.*;

public class AggressiveMob extends Mob {
    private final Player target;
    private final int aggroDistance;
    private final int attackCooldown;
    private final int damage; // Dégâts infligés à chaque attaque
    private boolean isAggroed;
    private long lastAttackTime = 0;
    private List<Point> path; // Path to follow
    private long lastInRangeTime = 0; // Time when player was last in range
    private static final long AGGRO_TIMEOUT = 5000; // 5 seconds timeout
    private int jumpCooldown = 1000; // 1 second cooldown between jumps
    private long lastJumpTime = 0; // Time of the last jump
    private List<Actor> aliveActors; // List of all alive actors
    private int currentDirection = 0; // -1 pour gauche, 1 pour droite, 0 pour stationnaire
    private long lastDirectionChangeTime = 0;
    private AStar pathfinder; // A* pathfinding algorithm

    @Override
    public void updatePosition() {
        // Use Mob's implementation for physical behavior
        super.updatePosition();
    }

    public AggressiveMob(
            int posX, int posY, int width, int height,
            TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach,
            ActorEnum type, Player player,
            int aggroDistance, int attackCooldown,
            List<Actor> aliveActors, Pane globalPane,
            int damage,
            HitboxManager hitboxManager
    ) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, type, hitboxManager);
        this.target = player;
        this.aggroDistance = aggroDistance;
        this.attackCooldown = attackCooldown;
        this.damage = damage;
        this.isAggroed = false;
        this.path = new ArrayList<>();
        this.lastInRangeTime = System.currentTimeMillis(); // Initialize the last in-range time
        this.lastJumpTime = System.currentTimeMillis(); // Initialize the last jump time
        this.aliveActors = aliveActors; // Store the list of alive actors
        this.pathfinder = new AStar(tileMap); // Initialize the pathfinder
        getHitboxManager().createHitbox(this, HitboxType.ATTACK);
    }

    /**
     * Finds the shortest path from the mob to the player using A* algorithm
     */
    private void findPathToPlayer() {
        // Convert entity positions to tile coordinates
        int startX = getPosX() / TileMap.format;
        int startY = getPosY() / TileMap.format;
        int targetX = target.getPosX() / TileMap.format;
        int targetY = target.getPosY() / TileMap.format;

        // Use the AStar class to find the path
        List<Point> newPath = pathfinder.findPath(startX, startY, targetX, targetY);

        // Update the path
        path.clear();
        path.addAll(newPath);
    }


    @Override
    public void updateHorizontalMovement() {
        // Calculate Manhattan distance to player (in pixels)
        int dx = Math.abs(target.getPosX() - getPosX());
        int dy = Math.abs(target.getPosY() - getPosY());
        int distance = dx + dy;

        // Convert to tile distance
        int tileDistance = distance / TileMap.format;

        long currentTime = System.currentTimeMillis();

        // Check if player is in range using the aggroDistance parameter
        boolean inRange = tileDistance <= aggroDistance;

        if (inRange) {
            // Update last in-range time
            lastInRangeTime = currentTime;

            // Set aggro if within range
            isAggroed = true;
        } else if (isAggroed) {
            // Check if player has been out of range for too long (5 seconds)
            if (currentTime - lastInRangeTime > AGGRO_TIMEOUT) {
                // Reset aggro
                isAggroed = false;
                path.clear();
            }
        }

        if (isAggroed) {
            // Update path every few frames or when needed
            if (path.isEmpty() || Math.random() < 0.05) { // 5% chance to recalculate path each frame
                findPathToPlayer();
            }
            followPlayer();
        } else {
            // Use the parent Mob class's behavior directly when not aggressive
            super.updateHorizontalMovement();

            // Additional check for entities in front (specific to AggressiveMob)
            if (isEntityInFront() && super.getCollider().hasCollisionBottom(1)) {
                updateVerticalMovement(); // saute si un obstacle (entité) est devant
            }
        }
    }

    private void followPlayer() {
        // If we have a path, follow it
        if (!path.isEmpty()) {
            // Get the next point in the path
            Point nextPoint = path.get(0);

            // Convert tile coordinates to pixel coordinates (center of the tile)
            int nextX = nextPoint.x * TileMap.format + TileMap.format / 2;
            int nextY = nextPoint.y * TileMap.format + TileMap.format / 2;

            // Move towards the next point horizontally
            if (nextX < getPosX()) {
                setLookDirection(LookDirections.LEFT);
                if (!getCollider().hasCollisionLeft()) {
                    setVelocityX(-getMoveSpeed());
                }
            } else if (nextX > getPosX()) {
                setLookDirection(LookDirections.RIGHT);
                if (!getCollider().hasCollisionRight()) {
                    setVelocityX(getMoveSpeed());
                }
            }

            // Handle vertical movement
            int verticalDiff = nextY - getPosY();

            // If the next point is above us and we're on the ground, jump
            if (verticalDiff < -TileMap.format/2 && super.getCollider().hasCollisionBottom(1)) {
                updateVerticalMovement(); // Jump to reach higher points
            }

            // If we're close enough to the next point, remove it from the path
            int distanceToNext = Math.abs(nextX - getPosX()) + Math.abs(nextY - getPosY());
            if (distanceToNext < TileMap.format) {
                path.remove(0);
            }

            // Jump if blocked horizontally or if there's an entity in front, but only if the obstacle is one tile high
            if ((((super.getCollider().hasCollisionLeft() || super.getCollider().hasCollisionRight()) 
                    && super.getCollider().hasCollisionBottom(1)) || 
                    (isEntityInFront() && super.getCollider().hasCollisionBottom(1))) 
                    && isObstacleOneTileHigh()) {
                updateVerticalMovement(); // saute si bloqué ou si un obstacle (entité) est devant et que l'obstacle fait exactement une tile de hauteur
            }
        } else {
            // Direct movement if no path is found

            // Check if the mob is directly above or below the player (with a small tolerance)
            int verticalDiff = target.getPosY() - getPosY();
            int horizontalDiff = target.getPosX() - getPosX();
            boolean isDirectlyAboveOrBelow = Math.abs(horizontalDiff) < getWidth() / 2;

            // Only change direction if not directly above/below the player or if on the ground
            if (!isDirectlyAboveOrBelow || super.getCollider().hasCollisionBottom(1)) {
                if ((target.getPosX() < getPosX())) {
                    setLookDirection(LookDirections.LEFT);
                    if (!getCollider().hasCollisionLeft()) {
                        setVelocityX(-getMoveSpeed());
                    }
                } else if (target.getPosX() > getPosX()) {
                    setLookDirection(LookDirections.RIGHT);
                    if (!getCollider().hasCollisionRight()) {
                        setVelocityX(getMoveSpeed());
                    }
                }
            } else {
                // If directly above/below and in the air, maintain current velocity
                setVelocityX(0);
            }

            // Check if player is above and jump if needed
            // Reuse the verticalDiff variable declared above
            if (verticalDiff < -TileMap.format && super.getCollider().hasCollisionBottom(1)) {
                updateVerticalMovement(); // Jump to try to reach the player
            }

            // Jump if blocked horizontally or if there's an entity in front, but only if the obstacle is one tile high
            if ((((super.getCollider().hasCollisionLeft() || super.getCollider().hasCollisionRight()) 
                    && super.getCollider().hasCollisionBottom(1)) || 
                    (isEntityInFront() && super.getCollider().hasCollisionBottom(1))) 
                    && isObstacleOneTileHigh()) {
                updateVerticalMovement(); // saute si bloqué ou si un obstacle (entité) est devant et que l'obstacle fait exactement une tile de hauteur
            }
        }

        checkAttackRange();
    }

    private void checkAttackRange() {
        int attackRange = getReach();
        int dx = Math.abs(target.getPosX() - getPosX());
        int dy = Math.abs(target.getPosY() - getPosY());

        if (dx <= attackRange && dy <= attackRange) {
            attackPlayer();
        }
    }

    private void attackPlayer() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAttackTime >= attackCooldown) {
            target.receiveDamage(this.damage);
            lastAttackTime = currentTime;
        }
    }

    public boolean isAggroed() {
        return isAggroed;
    }

    /**
     * Checks if there is an entity in front of the mob in the direction it's moving
     * @return true if there is an entity in front of the mob, false otherwise
     */
    public int Changement(){
        long currentTime = System.currentTimeMillis();
        int DIRECTION_CHANGE_INTERVAL = 2000;
        // Changement aléatoire de direction
        if (currentTime - lastDirectionChangeTime > DIRECTION_CHANGE_INTERVAL && !getIsJumping()) {
            double direction = Math.random();
            if (direction < 0.33) {
                currentDirection = -1;
            } else if (direction < 0.66) {
                currentDirection = 1;
            } else {
                currentDirection = 0;
            }
            lastDirectionChangeTime = currentTime;
        }
        return currentDirection;
    }

    private boolean isEntityInFront() {
        // Get the direction the mob is moving
        int direction = getLookDirection();

        // Check all alive actors
        for (Actor actor : aliveActors) {
            // Skip self and the target (player)
            if (actor == this || actor == target) {
                continue;
            }

            // Check if the actor is in front of the mob
            boolean isInFront = false;
            if (direction > 0) { // Moving right
                isInFront = actor.getPosX() > getPosX() && 
                           Math.abs(actor.getPosX() - getPosX()) < getWidth() * 2;
            } else { // Moving left
                isInFront = actor.getPosX() < getPosX() && 
                           Math.abs(actor.getPosX() - getPosX()) < getWidth() * 2;
            }

            // Check if the actor is at the same height
            boolean isAtSameHeight = Math.abs(actor.getPosY() - getPosY()) < getHeight();

            if (isInFront && isAtSameHeight) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the obstacle in front of the mob is exactly one tile high and has air above it
     * or if there's air beside the obstacle to navigate around it
     * @return true if the obstacle is exactly one tile high and has air above it or beside it, false otherwise
     */
    private boolean isObstacleOneTileHigh() {
        // Get the direction the mob is moving
        int direction = getLookDirection();

        // Get the mob's position in tile coordinates
        int mobTileX = getPosX() / TileMap.format;
        int mobTileY = getPosY() / TileMap.format;

        // Check the tile in front of the mob
        int frontTileX = mobTileX + (direction > 0 ? 1 : -1);

        // Check if there's an obstacle at the mob's height
        boolean hasObstacleAtHeight = !pathfinder.isValidPosition(frontTileX, mobTileY);

        // Check if there's an obstacle one tile above
        boolean hasObstacleAbove = !pathfinder.isValidPosition(frontTileX, mobTileY - 1);

        // Check if the block two tiles above is air
        boolean hasAirTwoAbove = pathfinder.isValidPosition(frontTileX, mobTileY - 2);

        // Check if there's air beside the obstacle (to the left or right)
        boolean hasAirBeside = pathfinder.isValidPosition(frontTileX + 1, mobTileY) || 
                              pathfinder.isValidPosition(frontTileX - 1, mobTileY);

        // The obstacle is navigable if:
        // 1. It's exactly one tile high and has air above it, or
        // 2. There's air beside it to navigate around
        return (hasObstacleAtHeight && hasObstacleAbove && hasAirTwoAbove) || hasAirBeside;
    }

    @Override
    public void updateVerticalMovement() {
        long currentTime = System.currentTimeMillis();

        if (super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping() && (!super.getCollider().hasCollisionLeft() || !super.getCollider().hasCollisionRight())) {

            // Vérifiez si le temps de recharge est écoulé
            if (currentTime - lastJumpTime >= jumpCooldown) {
                super.setIsJumping(true);
                super.setJumpingTestDecay(0);
                lastJumpTime = currentTime; // Mettre à jour le dernier temps de saut
            }
        } else if (super.getIsJumping()) {
            if (super.getJumpingTestDecay() == super.getJumpForce()) {
                super.setVelocityY(0);
                super.setIsJumping(false);
            } else if (!super.getCollider().hasCollisionTop(super.getVelocityY() + 1)) {
                // Apply a multiplier to make the jump higher (2.5x higher)
                super.setVelocityY((int)(-super.getJumpForce() * 2.5) + super.getJumpingTestDecay());
                super.setJumpingTestDecay(super.getJumpingTestDecay() + 1);
            } else {
                super.setIsJumping(false);
            }
        }
    }
}
