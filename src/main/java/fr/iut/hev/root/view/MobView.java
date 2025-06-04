package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Mob;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import static fr.iut.hev.root.model.TileMap.format;

public class MobView extends ActorView {
    private Mob mob;
    private ImageView mobsprite;

    public MobView(Mob mob, TileMap tileMap, AnchorPane anchorPane) {
        super(mob, tileMap,anchorPane);




    }
}

