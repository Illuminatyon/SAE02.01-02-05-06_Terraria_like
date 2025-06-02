package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Mob;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.ImageView;

public class MobView extends ActorView {
    private Mob mob;
    private ImageView mobsprite;
    private TileMap tileMap;

    public MobView(Mob mob, ImageView mobsprite, TileMap tileMap) {
        super(mob,mobsprite,tileMap);

    }

}

