package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.Tiles;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tileMap;

    public TileMap(int width, int height, int format) {
        this.width = width/format;
        this.height = height/format;
        this.tileMap = new Tile[height/format][width/format];

        /*for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileMap[x][y] = new Tile(Tiles.AIR, 0, x, y);
            }
        }*/
        this.setTestMap();

    }

    public Tile getTile(int tileX, int tileY) {
        if (!(tileX < 0 || tileY < 0 || tileX >= width || tileY >= height))
            return tileMap[tileY][tileX];
        else
            return null;
    }

    public void setTestMap() {
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
    }

    // Faire une verification pour pas que deux tiles se superposent et ajoute une tile précisé en  paramètre
    public void addTile(Tile tile) {
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
