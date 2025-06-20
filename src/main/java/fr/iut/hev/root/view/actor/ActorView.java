package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.TileMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class ActorView {
        private Actor actor;
        private ImageView actorSprite;
        private TileMap tileMap;
        private AnchorPane anchorPane;

        public ActorView(Actor actor, TileMap tileMap, AnchorPane anchorPane) {
            this.actor = actor;
            this.tileMap = tileMap;
            this.anchorPane = anchorPane;
            this.load();
        }

        public Actor getActor() {
            return this.actor;
        }

        public AnchorPane getAnchorPane() {
            return this.anchorPane;
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
            String path = "/fr/iut/hev/root/img/entities/actors/" + actor.getName() + ".png";
            System.out.println("Trying to load image from path: " + path);

            try (InputStream stream = getClass().getResourceAsStream(path)) {
                if (stream == null) {
                    System.err.println("ERROR: Image not found at path: " + path);
                    // Optionnel : charger une image "placeholder" ou rien faire
                    return;
                }

                Image image = new Image(stream);
                this.actorSprite = new ImageView(image);

                // Adapter la taille de l’image à l’acteur
                actorSprite.setFitWidth(actor.getWidth());
                actorSprite.setFitHeight(actor.getHeight());

                // Set initial position (will be updated by the camera)
                actorSprite.setLayoutX(0);
                actorSprite.setLayoutY(0);
                /*actorSprite.translateXProperty().bind(actor.posXProperty());
                actorSprite.translateYProperty().bind(actor.posYProperty());*/

                // Bind only the direction
                actorSprite.scaleXProperty().bind(actor.lookDirectionProperty());

                // Ajouter à l'AnchorPane
                anchorPane.getChildren().add(actorSprite);

                System.out.println("Image loaded successfully for actor: " + actor.getName());
            } catch (Exception e) {
                System.err.println("Exception while loading image for actor " + actor.getName());
                e.printStackTrace();
            }
        }



    public void deleteActorSprite() {
            // Remove the sprite from the AnchorPane instead of just hiding it
            if (actorSprite != null && anchorPane != null) {
                anchorPane.getChildren().remove(actorSprite);
            }
        }

        public ImageView getActorSprite() {
            return actorSprite;
        }




}
