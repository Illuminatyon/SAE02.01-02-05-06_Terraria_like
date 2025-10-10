package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.exception.MapLoadingException;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.CreateHashmap;
import fr.iut.hev.root.model.utilities.SaveReader;

import java.io.IOException;
import java.util.HashMap;

public class TileMap {

    private static TileMap tileMap;

    private ItemFactory itemFactory;
    private int width, height;
    private Tile[][] tileArray;

    public static final int format = 32;

    private TileMap() {
        /**
         * Constructeur de TileMap. "width" et "height" en pixel.
         */
        this.itemFactory = null;
        this.width = 0;
        this.height = 0;
        this.tileArray = null;
    }

    public static TileMap getInstance() {
        if (tileMap == null)
            tileMap = new TileMap();
        return tileMap;
    }

    public void initTileMap(ItemFactory itemFactory, int width, int height) {
        /**
         * TileMap's initializer
         */
        this.itemFactory = itemFactory;
        this.width = width/format;
        this.height = height/format;
        this.tileArray = new Tile[height/format][width/format];

        //Initialize all tiles with air
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                this.addTile(new Tile(TilesEnum.AIR, j, i));
            }
        } // TODO : Faire un refactoring de ça

        //Initialize land
        try {
            this.setMap("src/main/resources/fr/iut/hev/root/data/map.json");
        } catch (IOException e) {
            throw new MapLoadingException("échec du chargement de la map");
        }
    }

    public Tile getTile(int tileX, int tileY) {
        /**
         * Retourne la Tile de position tileX et tileY dans la TileMap
         */
        if (!(tileX < 0 || tileY < 0 || tileX >= width || tileY >= height))
            return tileArray[tileY][tileX];
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
            }
        }
    }

    public void setTestMap() {
        // TODO : Faire un refactor de ça
        /**
         * crée une map en 1920p avec 60*33 tile de test
         * Avec vérification des limites pour éviter les erreurs dans les tests
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

        // Vérification des limites pour chaque opération
        if (this.getHeight() > 18) {
            for (int i = 0; i < Math.min(15, this.getWidth()); i++) {
                this.getTile(i, 18).setTileEnum(TilesEnum.DIRT);
                if (i < 14 && this.getHeight() > 17) {
                    this.getTile(i, 17).setTileEnum(TilesEnum.DIRT);
                }
                if (i < 13 && this.getHeight() > 16) {
                    this.getTile(i, 16).setTileEnum(TilesEnum.DIRT);
                }
                if (i < 12 && this.getHeight() > 15) {
                    this.getTile(i, 15).setTileEnum(TilesEnum.DIRT);
                }
            }
        }

        if (this.getWidth() > 14 && this.getHeight() > 17) {
            for (int i = 0; i < 3; i++) {
                if (14 - i >= 0 && 14 - i < this.getWidth() && 17 - i >= 0 && 17 - i < this.getHeight()) {
                    this.getTile(14 - i, 17 - i).setTileEnum(TilesEnum.GRASS);
                }
            }
        }

        if (this.getHeight() > 14) {
            for (int i = 0; i < 11; i++) {
                if (11 - i >= 0 && 11 - i < this.getWidth()) {
                    this.getTile(11 - i, 14).setTileEnum(TilesEnum.GRASS);
                }
            }
        }

        if (this.getHeight() > 14) {
            for (int i = 0; i < 5; i++) {
                if (14 - i >= 0 && 14 - i < this.getHeight()) {
                    this.getTile(0, 14 - i).setTileEnum(TilesEnum.DIRT);
                }
            }
        }

        if (this.getWidth() > 27 && this.getHeight() > 14) {
            for (int i = 0; i < 3; i++) {
                if (25 + i < this.getWidth()) {
                    this.getTile(25 + i, 14).setTileEnum(TilesEnum.GRASS);
                }
            }
        }

        if (this.getWidth() > 27 && this.getHeight() > 15) {
            this.getTile(27, 15).setTileEnum(TilesEnum.DIRT);
        }

        if (this.getWidth() > 35 && this.getHeight() > 17) {
            for (int i = 0; i < 5; i++) {
                if (17 - i >= 0 && 17 - i < this.getHeight()) {
                    this.getTile(35, 17 - i).setTileEnum(TilesEnum.DIRT);
                }
            }
        }

        if (this.getWidth() > 40 && this.getHeight() > 15) {
            this.getTile(40, 15).setTileEnum(TilesEnum.DIRT);
        }

        if (this.getWidth() > 57 && this.getHeight() > 31) {
            this.getTile(57, 31).setTileEnum(TilesEnum.STONE);
        }

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
        if (getTile(x,y) == null || getTile(x,y).getTileEnum().getType() == TileTypesEnum.AIR)
            this.tileArray[y][x] = tile;
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

    public void setItemFactory(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }
}
