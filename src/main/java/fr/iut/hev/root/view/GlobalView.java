package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import static fr.iut.hev.root.model.TileMap.format;

public class GlobalView {

    private TileMap tileMap;
    private TilePane tileMapLand;
    private TilePane tileMapBackground;

    public GlobalView (TileMap tileMap,TilePane tileMapLand,TilePane tileMapBackground) {
        this.tileMap = tileMap;
        this.tileMapLand = tileMapLand;
        this.tileMapBackground = tileMapBackground;
    }

    public void loadWorld() {
        Tile currentTile;
        ImageView tileBreakable;
        ImageView tileBackground;
        int index = 0;
        for (int i = 0; i < this.tileMap.getHeight(); i++) {
            for (int j = 0; j < this.tileMap.getWidth(); j++) {
                currentTile = this.tileMap.getTile(j,i);
                tileBreakable = new ImageView(getTexture(currentTile,4));
                tileBackground = new ImageView(getTexture_background(currentTile));
                tileBreakable.setId(Integer.toString(index));
                tileBackground.setId(Integer.toString(index));
                tileBreakable.setFitWidth(format);
                tileBreakable.setFitHeight(format);
                tileBackground.setFitWidth(format);
                tileBackground.setFitHeight(format);
                tileMapLand.getChildren().add(tileBreakable);
                tileMapBackground.getChildren().add(tileBackground);
                index++;
            }
        }
    }

    public void updateTile(int x, int y,Tile tile) {
        int textureNumber;
        if (tile.getHealth() > 0 && tile.getHealth()%2 == 0) {
            textureNumber = tile.getHealth() / 2;
            tileMapLand.getChildren().set(y * 60 + x, new ImageView(getTexture(tile, textureNumber)));
        }
        else if (tile.getHealth() <= 0){
            tileMapLand.getChildren().set(y * 60 + x, new ImageView());
        }
    }

    public Image getTexture(Tile tile, int tileHealth) {
        /**
         * Retourne le sprite de la Tile en fonction des dégâts qu'elle a subit.
         */
        if (tile.getTile().getType() == TileTypes.AIR)
            return null;
        String path = "/fr/iut/hev/root/img/tile/".concat(tile.getTile().getName()).concat("_").concat(Integer.toString(tileHealth)).concat(".png");
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public Image getTexture_background(Tile tile) {
        /**
         * Retourne le sprite background de la Tile
         */
        if (tile.getTile().getType() == TileTypes.AIR)
            return null;
        String path = "/fr/iut/hev/root/img/tile/".concat(tile.getTile().getName()).concat("_background.png");
        return new Image(getClass().getResource(path).toExternalForm());
    }
}
