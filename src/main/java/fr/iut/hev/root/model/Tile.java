package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.TileTypes;
import fr.iut.hev.root.model.enums.Tiles;
import javafx.scene.image.Image;

import java.net.URL;

public class Tile {
    private Tiles tile;
    private int health;

    // Coordonnees en Tile Position et non pas en coordonnee reelle
    private int tileX;
    private int tileY;

    public Tile(Tiles tile, int x, int y) {
        this.tile = tile;
        this.health = tile.getMaxHealth();
        this.tileX = x;
        this.tileY = y;

    }

    public void takesDamage(int amount) {
        this.health = Math.max(0, this.health - amount); // Retourne le plus grand
    }

    public Tiles getTile() {
        return this.tile;
    }

    public void setTile(Tiles tile) {
        this.tile = tile;
    }

    public int getX() {
        return this.tileX;
    }

    public int getY() {
        return this.tileY;
    }

    public int getHealth() {return this.health;}

    /*public String toString() {
        return "{"
                .concat(this.name)
                .concat(": ")
                .concat(Boolean.toString(this.hasCollision))
                .concat(", {")
                .concat(Integer.toString(this.tileX))
                .concat(", ")
                .concat(Integer.toString(this.tileY))
                .concat("}}");
    }*/

    @Override
    public String toString() {
        return this.getTile().toString();
    }

    public void breaks() {
        this.tile = Tiles.AIR;
    }
}
