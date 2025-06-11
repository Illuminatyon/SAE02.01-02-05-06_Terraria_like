package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.util.Objects;

public class ActorView {

    private Actor actor;
    private ImageView actorSprite;
    private TileMap tileMap;
    private AnchorPane anchorPane;

    private Image staticImage;
    private Image movingGif;

    public ActorView(Actor actor, TileMap tileMap, AnchorPane anchorPane) {
        this.actor = actor;
        this.tileMap = tileMap;
        this.anchorPane = anchorPane;
        this.load();
        // this.setupMovementListener(); // <--- Supprimez cette ligne !
    }

    public void load() {
        // Chargement de l'image statique (PNG)
        String staticImagePath = "/fr/iut/hev/root/img/entities/actors/" + actor.getName() + ".png";
        System.out.println("Trying to load static image from path: " + staticImagePath);
        try (InputStream staticStream = getClass().getResourceAsStream(staticImagePath)) {
            if (staticStream == null) {
                System.err.println("ERROR: Static image not found at path: " + staticImagePath);
                return;
            }
            staticImage = new Image(staticStream);
            this.actorSprite = new ImageView(staticImage); // Initialise avec l'image statique
            setupImageView();
            System.out.println("Static image loaded successfully for actor: " + actor.getName());
        } catch (Exception e) {
            System.err.println("Exception while loading static image for actor " + actor.getName());
            e.printStackTrace();
        }

        // Si l'acteur est un joueur, chargez également le GIF
        if (actor instanceof Player) {
            String movingGifPath = "/fr/iut/hev/root/img/entities/actors/" + actor.getName() + "_moving.gif";
            System.out.println("Trying to load moving GIF from path: " + movingGifPath);
            try (InputStream gifStream = getClass().getResourceAsStream(movingGifPath)) {
                if (gifStream == null) {
                    System.err.println("WARNING: Moving GIF not found at path: " + movingGifPath + ". Player will not have moving animation.");
                    movingGif = null;
                } else {
                    movingGif = new Image(gifStream);
                    System.out.println("Moving GIF loaded successfully for actor: " + actor.getName());
                }
            } catch (Exception e) {
                System.err.println("Exception while loading moving GIF for actor " + actor.getName());
                e.printStackTrace();
                movingGif = null;
            }
        }
    }

    private void setupImageView() {
        actorSprite.translateXProperty().bind(actor.posXProperty());
        actorSprite.translateYProperty().bind(actor.posYProperty());
        actorSprite.scaleXProperty().bind(actor.lookDirectionProperty());
        actorSprite.setFitWidth(actor.getWidth());
        actorSprite.setFitHeight(actor.getHeight());

        if (!anchorPane.getChildren().contains(actorSprite)) {
            anchorPane.getChildren().add(actorSprite);
        }
    }

    public void setStaticImage() {
        if (staticImage != null && actorSprite.getImage() != staticImage) { // Évite les changements inutiles
            actorSprite.setImage(staticImage);
        }
    }

    public void setMovingGif() {
        if (movingGif != null && actorSprite.getImage() != movingGif) { // Évite les changements inutiles
            actorSprite.setImage(movingGif);
        }
    }

    public void deleteActorSprite() {
            actorSprite.setVisible(false);
        }

        public ImageView getActorSprite() {
            return actorSprite;
        }

}
