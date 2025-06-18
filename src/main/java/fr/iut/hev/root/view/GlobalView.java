package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.utilities.CreateHashmap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static fr.iut.hev.root.model.TileMap.format;
import static fr.iut.hev.root.utilities.CreateHashmap.hashMapReader;

public class GlobalView {

    private TileMap tileMap;
    private TilePane tileMapLand;
    private TilePane tileMapBackground;
    private HashMap<Integer, TilesEnum> tiles ;

        public GlobalView (TileMap tileMap,TilePane tileMapLand,TilePane tileMapBackground) throws IOException {
            this.tileMap = tileMap;
            this.tileMapLand = tileMapLand;
            this.tileMapBackground = tileMapBackground;

            this.tiles = hashMapReader();
            loadWorld();
        }

    public void loadWorld() {
        Tile currentTile;
        ImageView tileBreakable;
        ImageView tileBackground;
        int index = 0;
        for (int i = 0; i < this.tileMap.getHeight(); i++) {
            for (int j = 0; j < this.tileMap.getWidth(); j++) {
                currentTile = this.tileMap.getTile(j, i);
                if (currentTile == null) {
                    System.err.println("Warning: Tile is null at (" + j + ", " + i + ")");

                }
                tileBreakable = new ImageView(getTexture(currentTile, 4));
                tileBackground = new ImageView(getTexture_background(currentTile));


                if (tileBreakable != null) {
                    tileBreakable.setId(Integer.toString(index));
                    tileBreakable.setFitWidth(format);
                    tileBreakable.setFitHeight(format);
                    tileMapLand.getChildren().add(tileBreakable);
                }

                if (tileBackground != null) {
                    tileBackground.setId(Integer.toString(index));
                    tileBackground.setFitWidth(format);
                    tileBackground.setFitHeight(format);
                    tileMapBackground.getChildren().add(tileBackground);
                }
                index++;
            }
        }
    }


    public void updateTile(Tile tile) {
        System.out.println(tile.getHealth());
        int textureNumber = 4;
        int tileHealth = tile.getHealth();
        int tileHealthStep = ((tile.getTileEnum().getMaxHealth()*20) / 4);
        if (tileHealth > 0) {
            for (int i = 4 ; i > 0 ; i--) {
                if (tileHealth < (tileHealthStep * i) && tileHealth >= (tileHealthStep * (i - 1)))
                    textureNumber = i;
            }
            tileMapLand.getChildren().set(tile.getY() * 60 + tile.getX(), new ImageView(getTexture(tile,textureNumber)));
        }
        else
            tileMapLand.getChildren().set(tile.getY() * 60 + tile.getX(), new ImageView());
    }

    public Image getTexture(Tile tile, int textureNumber) {
        if (tile == null || tile.getTileEnum() == null) {
            System.err.println("Error: Tile or TileEnum is null.");

        }
        if (tile.getTileEnum().getType() == TileTypesEnum.AIR) {
            return null;
        }
        String path = "/fr/iut/hev/root/img/tile/"
                + tile.getTileEnum().getName() + "_" + textureNumber + ".png";
        URL resource = getClass().getResource(path);
        if (resource == null) {
            System.err.println("Error: Resource not found at " + path);

        }
        return new Image(resource.toExternalForm());
    }


    public Image getTexture_background(Tile tile) {
        /**
         * Retourne le sprite background de la Tile
         */
        if (tile.getTileEnum().getType() == TileTypesEnum.AIR)
            return null;
        String path = "/fr/iut/hev/root/img/tile/".concat(tile.getTileEnum().getName()).concat("_background.png");
        System.out.println(path);
        return new Image(getClass().getResource(path).toExternalForm());
    }
}
