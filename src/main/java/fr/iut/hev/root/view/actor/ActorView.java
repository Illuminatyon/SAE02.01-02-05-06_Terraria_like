package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.land.TileMap;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ActorView {
    private Actor actor;
    private ImageView actorSprite;
    private TileMap tileMap;
    private AnchorPane anchorPane;
    private DoubleProperty camOffsetXProperty;
    private DoubleProperty camOffsetYProperty;

    public ActorView(Actor actor, TileMap tileMap, AnchorPane anchorPane) {
        this.camOffsetXProperty = new SimpleDoubleProperty(0);
        this.camOffsetYProperty = new SimpleDoubleProperty(0);
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

        Image image = new Image(getClass().getResource(path).toExternalForm());
        this.actorSprite = new ImageView(image);

        // Adapter la taille de l’image à l’acteur
        actorSprite.setFitWidth(actor.getWidth());
        actorSprite.setFitHeight(actor.getHeight());

        // Set initial position (will be updated by the camera)
        actorSprite.setLayoutX(getActor().getPosX());
        actorSprite.setLayoutY(getActor().getPosY());
        actorSprite.translateXProperty().bind(actor.posXProperty().add(camOffsetXProperty));
        actorSprite.translateYProperty().bind(actor.posYProperty().add(camOffsetYProperty));

        // Bind only the direction
        actorSprite.scaleXProperty().bind(actor.lookDirectionProperty());

        // Ajouter à l'AnchorPane
        anchorPane.getChildren().add(actorSprite);
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
    public double getCamOffsetX() {return this.camOffsetXProperty.getValue();}
    public double getCamOffsetY() {return this.camOffsetYProperty.getValue();}
    public void setCamOffsetX(double value) {this.camOffsetXProperty.setValue(value);}
    public void setCamOffsetY(double value) {this.camOffsetYProperty.setValue(value);}
    public DoubleProperty camOffsetXProperty() {return this.camOffsetXProperty;}
    public DoubleProperty camOffsetYProperty() {return this.camOffsetYProperty;}
}
