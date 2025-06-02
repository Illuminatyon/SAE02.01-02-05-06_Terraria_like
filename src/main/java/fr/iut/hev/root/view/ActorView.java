package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Actor;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.ImageView;

public class ActorView {

        private Actor actor;
        private ImageView ActorSprite;
        private TileMap tileMap;


        public ActorView(Actor actor,ImageView playerSprite,TileMap tileMap) {
            this.actor = actor;
            this.ActorSprite = playerSprite;
            this.tileMap = tileMap;
        }

        public void load(double x, double y) {
            ActorSprite.setLayoutX(x);
            ActorSprite.setLayoutY(y);
            ActorSprite.translateXProperty().bind(actor.posXProperty());
            ActorSprite.translateYProperty().bind(actor.posYProperty());
            ActorSprite.scaleXProperty().bind(actor.lookDirectionProperty());
        }

        public void deletePlayerSprite() {
            ActorSprite.setVisible(false);
        }


}
