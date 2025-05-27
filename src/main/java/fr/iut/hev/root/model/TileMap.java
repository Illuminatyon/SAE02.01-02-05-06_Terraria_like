package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.Tiles;
import fr.iut.hev.root.utilities.CreateHashmap;
import fr.iut.hev.root.utilities.SaveReader;

import java.io.IOException;
import java.util.HashMap;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tileMap;

    //public static final int format = 16;
    public static final int format = 32;

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

    public void setMap(String Path) throws IOException {
        HashMap<Integer, Tiles> index = CreateHashmap.HashMapReader();
        int[][] save = SaveReader.map(Path);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.addTile(new Tile (index.get(save[y][x]), x,y));
            }
        }
    }

    public void setTestMap() {
        /**
         * crée une map en 1920p avec 120*67 tile de test
         */
        int index = 0;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                //if (index < 5160) {
                if (index < 1080) {
                    this.addTile(new Tile(Tiles.AIR, j, i));
                }
                //else if (index >= 5160 && index < 5280) {
                else if (index >= 1080 && index < 1140) {
                    this.addTile(new Tile(Tiles.GRASS,j,i));
                }
                //else if (index >= 5280) {
                else if (index >= 1140) {
                    this.addTile(new Tile(Tiles.DIRT,j,i));
                }
                index++;
            }
        }
        this.getTile(15, 17).setTile(Tiles.DIRT);
        this.getTile(14, 17).setTile(Tiles.DIRT);
        this.getTile(13, 17).setTile(Tiles.DIRT);
        this.getTile(12, 17).setTile(Tiles.DIRT);
        this.getTile(11, 17).setTile(Tiles.DIRT);
        this.getTile(10, 17).setTile(Tiles.DIRT);
        this.getTile(14, 16).setTile(Tiles.DIRT);
        this.getTile(11, 16).setTile(Tiles.DIRT);

        /*for (int i = 16; i > 0; i--) {
            this.getTile(20, i).setTile(Tiles.DIRT);
        }

        for (int i = 16; i > 0; i--) {
            this.getTile(40, i).setTile(Tiles.DIRT);
        }*/
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
