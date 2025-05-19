package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class WorldController implements Initializable {

    //@FXML
    //private TilePane backgroundTileMap;

    @FXML
    private TilePane tileMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TileMap tilemaptest = new TileMap(1920,1072);
        TileMap tilemaptest = new TileMap(1920,1056);
        PlayerController.setTileMap(tilemaptest);
        Tile currentTile;
        ImageView tileBreakable;
        //ImageView tileBackground;
        int index = 0;
        for (int i = 0; i < tilemaptest.getHeight(); i++) {
            for (int j = 0; j < tilemaptest.getWidth(); j++) {
                currentTile = tilemaptest.getTile(j,i);
                tileBreakable = new ImageView(currentTile.getTexture());
                //tileBackground = new ImageView(currentTile.getTexture_background());
                tileBreakable.setId(Integer.toString(index));
                //tileBackground.setId(Integer.toString(index));
                tileBreakable.setFitWidth(TileMap.format);
                tileBreakable.setFitHeight(TileMap.format);
                tileMap.getChildren().add(tileBreakable);
                //backgroundTileMap.getChildren().add(tileBackground);
                index++;
            }
        }
    }
}
