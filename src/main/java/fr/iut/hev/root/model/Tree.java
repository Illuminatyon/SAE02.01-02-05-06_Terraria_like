package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.items.Item;

public class Tree {

    private int taille;
    private int x;
    private int y;
    private int health;

    public Tree(int taille,int x,int y) {
        this.taille = taille;
        this.x = x;
        this.y = y;
        this.health = taille*40;
    }

    public void takesDamage(int damage) {
        if (!(health - damage <= 0))
            this.health -= damage;
        else if (health <= 0) {
            Loot droppedLoot = new Loot()
        }
    }
}
