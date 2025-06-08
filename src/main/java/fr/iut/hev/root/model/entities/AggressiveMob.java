package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import javafx.scene.layout.Pane;

import java.util.List;

public class AggressiveMob extends Actor {
    private final Player target;
    private final int aggroDistance;
    private final int attackCooldown;
    private final int damage; // Dégâts infligés à chaque attaque
    private boolean isAggroed;
    private long lastAttackTime = 0;

    private long lastDirectionChangeTime = 0;
    private static final long DIRECTION_CHANGE_INTERVAL = 2000;
    private int currentDirection = 0; // -1 gauche, 1 droite, 0 neutre

    public AggressiveMob(
            int posX, int posY, int width, int height,
            TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach,
            ActorEnum type, Player player,
            int aggroDistance, int attackCooldown,
            List<Actor> aliveActors, Pane globalPane,
            int damage
    ) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, type);
        this.target = player;
        this.aggroDistance = aggroDistance;
        this.attackCooldown = attackCooldown;
        this.damage = damage;
        this.isAggroed = false;

        aliveActors.add(this);
    }

    @Override
    public void updatePosition() {
        // Gravité
        if (!getCollider().hasCollisionBottom(getVelocityY() + 1) && !getIsJumping()) {
            setVelocityY(getVelocityY() + Gravity.getGravityForce());
        } else {
            setVelocityY(0);
        }

        // Mouvement horizontal
        updateHorizontalMovement();

        // Déplacement X
        double velocityX = getVelocityX() * getMoveSpeed();
        double moveStepX = Math.signum(velocityX);
        double remainingX = Math.abs(velocityX);

        while (remainingX > 0) {
            if (moveStepX > 0 && !getCollider().hasCollisionRight()) {
                posXProperty().set(posXProperty().get() + 1);
            } else if (moveStepX < 0 && !getCollider().hasCollisionLeft()) {
                posXProperty().set(posXProperty().get() - 1);
            } else {
                setVelocityX(0);
                break;
            }
            remainingX -= 1;
        }

        // Déplacement Y
        double velocityY = getVelocityY();
        double moveStepY = Math.signum(velocityY);
        double remainingY = Math.abs(velocityY);

        while (remainingY > 0) {
            if (moveStepY > 0 && !getCollider().hasCollisionBottom(1)) {
                posYProperty().set(posYProperty().get() + 1);
            } else if (moveStepY < 0 && !getCollider().hasCollisionTop(-1)) {
                posYProperty().set(posYProperty().get() - 1);
            } else {
                setVelocityY(0);
                break;
            }
            remainingY -= 1;
        }

        // Saut auto
        updateVerticalMovement();
    }

    @Override
    public void updateHorizontalMovement() {
        int dx = Math.abs(target.getPosX() - getPosX());
        int dy = Math.abs(target.getPosY() - getPosY());
        int distance = dx + dy;

        if (distance <= aggroDistance) {
            isAggroed = true;
        }

        if (isAggroed) {
            followPlayer();
        } else {
            randomMove();
        }
    }

    private void followPlayer() {
        boolean moved = false;

        if (target.getPosX() < getPosX()) {
            setLookDirection(LookDirections.LEFT);
            if (!getCollider().hasCollisionLeft()) {
                setVelocityX(-getMoveSpeed());
                moved = true;
            }
        } else if (target.getPosX() > getPosX()) {
            setLookDirection(LookDirections.RIGHT);
            if (!getCollider().hasCollisionRight()) {
                setVelocityX(getMoveSpeed());
                moved = true;
            }
        }

        if (!moved) {
            setVelocityX(0);
            updateVerticalMovement(); // saute si bloqué
        }

        checkAttackRange();
    }

    private void randomMove() {
        long currentTime = System.currentTimeMillis();

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

        if (currentDirection == -1) {
            setLookDirection(LookDirections.LEFT);
            if (!getCollider().hasCollisionLeft()) {
                setVelocityX(-getMoveSpeed());
            } else {
                updateVerticalMovement(); // saut si bloqué
            }
        } else if (currentDirection == 1) {
            setLookDirection(LookDirections.RIGHT);
            if (!getCollider().hasCollisionRight()) {
                setVelocityX(getMoveSpeed());
            } else {
                updateVerticalMovement(); // saut si bloqué
            }
        } else {
            setVelocityX(0);
        }
    }

    @Override
    public void updateVerticalMovement() {
        if (getCollider().hasCollisionBottom(getVelocityY() - Gravity.getGravityForce())
                && !getIsJumping()
                && (!getCollider().hasCollisionLeft() || !getCollider().hasCollisionRight())) {
            setIsJumping(true);
            setJumpingTestDecay(0);
        } else if (getIsJumping()) {
            if (getJumpingTestDecay() == getJumpForce()) {
                setVelocityY(0);
                setIsJumping(false);
            } else if (!getCollider().hasCollisionTop(getVelocityY() + 1)) {
                setVelocityY(-getJumpForce() + getJumpingTestDecay());
                setJumpingTestDecay(getJumpingTestDecay() + 1);
            } else {
                setIsJumping(false);
            }
        }
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
}
