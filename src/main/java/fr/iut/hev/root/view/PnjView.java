package fr.iut.hev.root.view;

import fr.iut.hev.root.model.TileMap;

import fr.iut.hev.root.model.entities.Pnj;
import fr.iut.hev.root.model.enums.DialogueEnum;
import javafx.animation.PauseTransition;
import javafx.scene.LightBase;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PnjView extends ActorView{
    private Label phrase = new Label();

    public PnjView(Pnj actor, TileMap tileMap, AnchorPane anchorPane) {
        super(actor, tileMap, anchorPane);
        this.phrase.setFont(new Font("Arial", 14));
        this.phrase.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
        this.phrase.setVisible(false);
        this.getAnchorPane().getChildren().add(phrase);
    }

    public void speak(DialogueEnum dialogue) {
        // Update text and make the label visible.
        this.phrase.setText(dialogue.toString());
        this.phrase.setLayoutX(super.getActor().posXProperty().getValue() + super.getActor().getWidth() + 10);
        this.phrase.setLayoutY(super.getActor().posYProperty().getValue() - 20);
        this.phrase.setVisible(true);

        // Hide the speech bubble after 3 seconds.
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(event -> this.phrase.setVisible(false));
        pause.play();
    }
}

