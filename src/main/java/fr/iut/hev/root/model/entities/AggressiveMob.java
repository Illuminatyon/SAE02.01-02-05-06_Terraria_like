package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import javafx.scene.layout.Pane;

import java.util.List;

public class AggressiveMob extends Mob {
    private final Player target;
    private final int aggroDistance;
    private final int attackCooldown;
    private final int damage; // Dégâts infligés à chaque attaque
    private boolean isAggroed;
    private long lastAttackTime = 0;

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
            super.updateHorizontalMovement();  // Mouvement aléatoire du Mob
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
