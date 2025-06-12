package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.items.Consumable;
import fr.iut.hev.root.model.items.Item;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tileMap;

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

    public void setTestMap() {
        /**
         * crée une map en 1920p avec 60*33 tile de test
         */
        int index = 0;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (index < 1080) {
                    this.addTile(new Tile(TilesEnum.AIR,j, i));
                }
                else if (index >= 1080 && index < 1140) {
                    this.addTile(new Tile(TilesEnum.GRASS,j,i));
                }
                else if (index >= 1140 && index < 1320) {
                    this.addTile(new Tile(TilesEnum.DIRT,j,i));
                }
                else if (index >= 1320) {
                    this.addTile(new Tile(TilesEnum.STONE,j,i));
                }
                index++;
            }
        }

        for (int i = 0; i < 15; i++) {
            this.getTile(i, 18).setTile(TilesEnum.DIRT);
            if (i < 14) {
                this.getTile(i, 17).setTile(TilesEnum.DIRT);
            }
            if (i < 13) {
                this.getTile(i, 16).setTile(TilesEnum.DIRT);
            }
            if (i < 12) {
                this.getTile(i, 15).setTile(TilesEnum.DIRT);
            }
        }

        for (int i = 0; i < 3; i++) {
            this.getTile(14 - i, 17 - i).setTile(TilesEnum.GRASS);
        }

        for (int i = 0; i < 11; i++) {
            this.getTile(11 - i, 14).setTile(TilesEnum.GRASS);
        }

        for (int i = 0; i < 5; i++) {
            this.getTile(0, 14 - i).setTile(TilesEnum.DIRT);
        }

        for (int i = 0; i < 3; i++) {
            this.getTile(25 + i, 14).setTile(TilesEnum.GRASS);
        }

        this.getTile(27, 15).setTile(TilesEnum.DIRT);

        for (int i = 0; i < 5; i++) {
            this.getTile(35, 17 - i).setTile(TilesEnum.DIRT);
        }

        this.getTile(40, 15).setTile(TilesEnum.DIRT);

        this.getTile(57, 31).setTile(TilesEnum.STONE);

        //this.getTile(57, 31).setTile(Tiles.DIRT);

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

    public void tileGetsMined(int x, int y) {
        Tile currentTile = this.getTile(x,y);
        if (!(currentTile.getHealth() <= 0))
            currentTile.takesDamage(1);
        if (currentTile.getHealth() <= 0) {
            currentTile.breaks();
            //Item item = new Item(currentTile.getTile().getRelatedItem()); // TODO: fix issue where this get some kind of null stuff
            Item item = new Item(ItemsEnum.RAW_CHICKEN);
            Loot droppedLoot = new Loot(item, 1, currentTile.getX() * format, currentTile.getY() * format, 32, 32, this);
        }
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
