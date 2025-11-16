package fr.iut.hev.root.model.land;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.exception.IncorrectCoordinatesException;
import fr.iut.hev.root.model.exception.MapLoadingException;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.CreateHashmap;
import fr.iut.hev.root.model.utilities.SaveReader;

import java.io.IOException;
import java.util.HashMap;

public class TileMap {

    private static TileMap tileMap = null;

    private ItemFactory itemFactory;
    private int tilesWidth, tilesHeight;
    private Tile[][] tileArray;

    public static final int format = 32;

    private TileMap() {
        this.itemFactory = null;
        this.tilesWidth = 0;
        this.tilesHeight = 0;
        this.tileArray = null;
    }

    public static TileMap getInstance() {
        if (tileMap == null)
            tileMap = new TileMap();
        return tileMap;
    }

    public void initTileMap(int tilesWidth, int tilesHeight) {
        /**
         * TileMap's initializer
         */
        this.itemFactory = ItemFactory.getInstance();
        this.tilesWidth = tilesWidth;
        this.tilesHeight = tilesHeight;
        this.tileArray = new Tile[tilesHeight][tilesWidth];

        //Initialize land
        try {
            this.setMap("src/main/resources/fr/iut/hev/root/data/map.json");
        } catch (IOException e) {
            throw new MapLoadingException("échec du chargement de la map");
        }
    }

    public Tile getTile(int pixelsX, int pixelsY) {
        if (pixelsX < 0 || pixelsY < 0 || pixelsX >= getPixelsWidth() || pixelsY >= getPixelsHeight()) {
            throw new IncorrectCoordinatesException("x or y out of bounds");
        }
        return tileArray[pixelsY / format][pixelsX / format];
    }

    public void setMap(String Path) throws IOException {
        HashMap<Integer, TilesEnum> index = CreateHashmap.hashMapReader();
        int[][] save = SaveReader.map(Path);
        for (int y = 0; y < tilesHeight; y++) {
            for (int x = 0; x < tilesWidth; x++) {
                int tileIndex = save[y][x];
                TilesEnum tileEnum = index.get(tileIndex);
                Tile tile = new Tile(tileEnum, x, y);
                this.addTile(tile);
            }
        }
    }

    public void addTile(Tile tile) {
        /**
         * Fait une verification pour pas que deux tiles se superposent et ajoute une tile précisé en  paramètre
         */
        int tileX = tile.getTileX();
        int tileY = tile.getTileY();
        if (getTile(tileX * format, tileY * format) == null ||
                getTile(tileX * format, tileY * format).getTileEnum().getType() == TileTypesEnum.AIR)
            this.tileArray[tileY][tileX] = tile;
    }

    public int getTilesWidth() {
        return tilesWidth;
    }

    public int getPixelsWidth() {
        return tilesWidth * format;
    }

    public int getTilesHeight() {
        return tilesHeight;
    }

    public int getPixelsHeight() {
        return tilesHeight * format;
    }

    public void tileGetsMined(int pixelsX, int pixelsY, int damage) {
        Tile currentTile = this.getTile(pixelsX, pixelsY);
        if (!(currentTile.getHealth() <= 0)) {
            currentTile.takesDamage(damage);
        }
        if (currentTile.getHealth() <= 0) {
            Item item = itemFactory.createItem(currentTile.getTileEnum().getRelatedItem());
            currentTile.breaks();
            //Item item = new Item(currentTile.getTile().getRelatedItem()); // TODO: fix issue where this get some kind of null stuff
            Loot droppedLoot = new Loot(item, 1, currentTile.getTileX() * format, currentTile.getTileY() * format, 32, 32);
        }
    }

    public boolean isTileEmpty(int pixelsX,int pixelsY) {
        return this.getTile(pixelsX, pixelsY).getTileEnum().getType() == TileTypesEnum.AIR;
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
