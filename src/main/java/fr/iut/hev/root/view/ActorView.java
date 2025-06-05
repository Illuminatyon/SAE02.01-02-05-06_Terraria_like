package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ActorView {

        private Actor actor;
        private ImageView actorSprite;
        private TileMap tileMap;
        private AnchorPane anchorPane;


        public ActorView(Actor actor,TileMap tileMap,AnchorPane anchorPane) {
            this.actor = actor;
            this.tileMap = tileMap;
            this.anchorPane = anchorPane;
            this.load();
        }

        /*public void load() {
            String path = "src/main/resources/fr/iut/hev/root/img/entities/actors/".concat(actor.getName()).concat(".png");
            new Image(getClass().getResource(path).toExternalForm());
            this.actorSprite = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
            this.actorSprite.setFitWidth(format);
            this.actorSprite.setFitHeight(format);
            actorSprite.setLayoutX((tileMap.getWidth() * TileMap.format) / 2);
            actorSprite.setLayoutY((tileMap.getHeight() * TileMap.format) / 2);
            actorSprite.translateXProperty().bind(actor.posXProperty());
            actorSprite.translateYProperty().bind(actor.posYProperty());
            actorSprite.scaleXProperty().bind(actor.lookDirectionProperty());

        }*/
        public void load() {

            String path = "/fr/iut/hev/root/img/entities/actors/".concat(/*"blackbox"*/this.actor.getName()).concat(".png");
            this.actorSprite = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
            actorSprite.setFitHeight( actor.getHeight());
            actorSprite.setFitWidth( actor.getWidth());
            //actorSprite.setLayoutX((tileMap.getWidth() * TileMap.format) / 2);
            //actorSprite.setLayoutY((tileMap.getHeight() * TileMap.format) / 2);
            actorSprite.translateXProperty().bind(actor.posXProperty());
            actorSprite.translateYProperty().bind(actor.posYProperty());
            actorSprite.scaleXProperty().bind(actor.lookDirectionProperty());
            anchorPane.getChildren().add(actorSprite);
        }

        public void deletePlayerSprite() {
            actorSprite.setVisible(false);
        }

        public ImageView getActorSprite() {
            return actorSprite;
        }


}
