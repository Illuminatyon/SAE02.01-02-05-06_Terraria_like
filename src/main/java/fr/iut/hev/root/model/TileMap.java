package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.Tiles;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tileMap;

    public static final int format = 16;

    public TileMap(int width, int height) {
        /**
         * Constructeur de TileMap. "width" et "height" en pixel.
         */
        this.width = width/format;
        this.height = height/format;
        this.tileMap = new Tile[height/format][width/format];

        //génération de la map test
        this.setTestMap();

    }

    public Tile getTile(int tileX, int tileY) {
        /**
         * Retourne la Tile de position tileX et tileY dans la TileMap
         */
        if (!(tileX < 0 || tileY < 0 || tileX >= width || tileY >= height))
            return tileMap[tileY][tileX];
        else
            return null;
    }

    public void setTestMap() {
        /**
         * crée une map en 1920p avec 120*67 tile de test
         */
        int index = 0;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (index < 5160) {
                    this.addTile(new Tile(Tiles.AIR, 0, j, i));
                }
                else if (index >= 5160 && index < 5280) {
                    this.addTile(new Tile(Tiles.GRASS,0,j,i));
                }
                else if (index >= 5280) {
                    this.addTile(new Tile(Tiles.DIRT,0,j,i));
                }
                index++;
            }
        }
        this.getTile(5, 0).setTile(Tiles.DIRT);
    }

    public void addTile(Tile tile) {
        /**
         * Fait une verification pour pas que deux tiles se superposent et ajoute une tile précisé en  paramètre
         */
        int x = tile.getX();
        int y = tile.getY();
        if (getTile(x,y) == null)
            this.tileMap[y][x] = tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Charger la TileMap d'un fichier
    // Peut etre va surement charger une partie, et charger plus tard aussi qlq parties de la TileMap pour les perfs
    // Retourne true si l'operation est reussie, false sinon
    private boolean readTileMap() {
        return false;
    }

    // Ecrire la TileMap dans un fichier
    // Retourne true si l'operation est reussie, false sinon
    private boolean writeTileMap() {
        return false;
    }
}
