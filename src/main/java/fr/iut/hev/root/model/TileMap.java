package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.utilities.CreateHashmap;
import fr.iut.hev.root.utilities.SaveReader;

import java.io.IOException;
import java.util.HashMap;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tileMap;
    private ItemFactory itemFactory;

    public static final int format = 32;

    public TileMap(int width, int height,ItemFactory itemFactory) throws IOException {
        /**
         * Constructeur de TileMap. "width" et "height" en pixel.
         */
        this.itemFactory = itemFactory;
        this.width = width/format;
        this.height = height/format;
        this.tileMap = new Tile[height/format][width/format];

        //génération de la map test
        //this.setTestMap();
        this.setMap("src/main/resources/fr/iut/hev/root/data/map.json");
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
        HashMap<Integer, TilesEnum> index = CreateHashmap.hashMapReader();
        int[][] save = SaveReader.map(Path);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tileIndex = save[y][x];
                TilesEnum tileEnum = index.get(tileIndex);
                Tile tile = new Tile(tileEnum, x, y);
                this.addTile(tile);
                System.out.println("Added Tile: " + tile);
            }
        }
    }


   /* public void setTestMap() {
        /
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
            this.getTile(i, 18).setTileEnum(TilesEnum.DIRT);
            if (i < 14) {
                this.getTile(i, 17).setTileEnum(TilesEnum.DIRT);
            }
            if (i < 13) {
                this.getTile(i, 16).setTileEnum(TilesEnum.DIRT);
            }
            if (i < 12) {
                this.getTile(i, 15).setTileEnum(TilesEnum.DIRT);
            }
        }

        for (int i = 0; i < 3; i++) {
            this.getTile(14 - i, 17 - i).setTileEnum(TilesEnum.GRASS);
        }

        for (int i = 0; i < 11; i++) {
            this.getTile(11 - i, 14).setTileEnum(TilesEnum.GRASS);
        }

        for (int i = 0; i < 5; i++) {
            this.getTile(0, 14 - i).setTileEnum(TilesEnum.DIRT);
        }

        for (int i = 0; i < 3; i++) {
            this.getTile(25 + i, 14).setTileEnum(TilesEnum.GRASS);
        }

        this.getTile(27, 15).setTileEnum(TilesEnum.DIRT);

        for (int i = 0; i < 5; i++) {
            this.getTile(35, 17 - i).setTileEnum(TilesEnum.DIRT);
        }

        this.getTile(40, 15).setTileEnum(TilesEnum.DIRT);

        this.getTile(57, 31).setTileEnum(TilesEnum.STONE);

        //this.getTile(57, 31).setTile(Tiles.DIRT);

        for (int i = 16; i > 0; i--) {
            this.getTile(20, i).setTile(Tiles.DIRT);
        }

        for (int i = 16; i > 0; i--) {
            this.getTile(40, i).setTile(Tiles.DIRT);
        }
    }*/

    public void addTile(Tile tile) {
        /**
         * Fait une verification pour pas que deux tiles se superposent et ajoute une tile précisé en  paramètre
         */
        int x = tile.getX();
        int y = tile.getY();
        if (getTile(x,y) == null || getTile(x,y).getTileEnum().getType() == TileTypesEnum.AIR)
            this.tileMap[y][x] = tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void tileGetsMined(int x, int y,int damage) {
        Tile currentTile = this.getTile(x,y);
        if (!(currentTile.getHealth() <= 0)) {
            System.out.println(currentTile.getHealth());
            currentTile.takesDamage(damage);
        }
        if (currentTile.getHealth() <= 0) {
            Item item = itemFactory.createItem(currentTile.getTileEnum().getRelatedItem());
            currentTile.breaks();
            //Item item = new Item(currentTile.getTile().getRelatedItem()); // TODO: fix issue where this get some kind of null stuff
            Loot droppedLoot = new Loot(item, 1, currentTile.getX() * format, currentTile.getY() * format, 32, 32, this);
        }
    }

    public boolean isTileEmpty(int x,int y) {
        return this.getTile(x,y).getTileEnum().getType() == TileTypesEnum.AIR;
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
