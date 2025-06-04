package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.TileTypes;
import fr.iut.hev.root.model.enums.Tiles;
import javafx.scene.image.Image;

import java.net.URL;

public class Tile {
    private Tiles tile;
    private int health;
    private int maxHealth;

    // Coordonnees en Tile Position et non pas en coordonnee reelle
    private int tileX;
    private int tileY;

    public Tile(Tiles tile, int maxHealth, int x, int y) {
        this.tile = tile;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.tileX = x;
        this.tileY = y;

    }

    public void damage(int amount) {
        this.health = Math.max(0, this.health - amount); // Retourne le plus grand
    }

    public Tiles getTile() {
        return this.tile;
    }

    public void setTile(Tiles tile) {
        this.tile = tile;
    }

    public Image getTexture(/*int damageAmount*/) {
        /**
         * Retourne le sprite de la Tile en fonction des dégâts qu'elle a subit.
         */
        if (this.tile.getType() == TileTypes.AIR)
            return null;
        String path = "/fr/iut/hev/root/img/tile/".concat(this.tile.getName())./*concat(Integer.toString(damageAmount)).*/concat(".png");
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public Image getTexture_background() {
        /**
         * Retourne le sprite background de la Tile
         */
        if (this.tile.getType() == TileTypes.AIR)
            return null;
        String path = "/fr/iut/hev/root/img/tile/".concat(this.tile.getName()).concat("_background.png");
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public int getX() {
        return this.tileX;
    }

    public int getY() {
        return this.tileY;
    }

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
}
