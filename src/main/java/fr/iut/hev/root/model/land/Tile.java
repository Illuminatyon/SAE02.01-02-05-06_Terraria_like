package fr.iut.hev.root.model.land;

public class Tile {

    private TilesEnum tileEnum;
    private int health;

    // Coordonnees en Tile Position et non pas en coordonnee reelle
    private int tileX; // Pas sur d'avoir besoin de les exposer
    private int tileY;

    public Tile(TilesEnum tileEnum, int x, int y) {
        this.tileEnum = tileEnum;
        this.health = tileEnum.getMaxHealth()*20;
        this.tileX = x;
        this.tileY = y;
    }

    public void takesDamage(int amount) {
        this.health = Math.max(0, this.health - amount); // Retourne le plus grand
    }

    public TilesEnum getTileEnum() {
        return this.tileEnum;
    }

    public void setTileEnum(TilesEnum tileEnum) {
        this.tileEnum = tileEnum;
    }

    public int getTileX() {
        return this.tileX;
    }

    public int getTileY() {
        return this.tileY;
    }

    public int getHealth() {return this.health;}

    public void resetHealth() {this.health = this.tileEnum.getMaxHealth()*20;}

    @Override
    public String toString() {
        return this.getTileEnum().toString();
    }

    public void breaks() {
        this.tileEnum = TilesEnum.AIR;
    }
}
