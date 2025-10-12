package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.actor.mobs.AggressiveMob;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;
import fr.iut.hev.root.model.land.TileMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MobView extends ActorView {
    private Mob mob;
    private ImageView mobsprite;

    public MobView(Mob mob, TileMap tileMap, AnchorPane anchorPane) {
        super(mob, tileMap,anchorPane);
    }

    public MobView(AggressiveMob mob, TileMap tileMap, AnchorPane anchorPane) {
        super(mob, tileMap,anchorPane);
    }
}

