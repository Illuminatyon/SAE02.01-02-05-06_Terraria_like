package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.TileMap;

import fr.iut.hev.root.model.entities.Pnj;
import fr.iut.hev.root.model.enums.DialogueEnum;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PnjView extends ActorView {
    private Label phrase = new Label();
    private int count = 0;

    public PnjView(Pnj actor, TileMap tileMap, AnchorPane anchorPane) {
        super(actor, tileMap, anchorPane);
        this.phrase.setFont(new Font("Arial", 14));
        this.phrase.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
        this.phrase.setVisible(false);
        this.getAnchorPane().getChildren().add(phrase);

    }

    public void speak() {
        DialogueEnum[] text = DialogueEnum.values();
        if (count >= text.length) {
            count = 0;
        }

        this.phrase.setText(text[count].getTexte());
        this.phrase.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(20));
        pause.setOnFinished(event -> this.phrase.setVisible(false));
        pause.play();
        count++;
        if (count == (text.length+1)) {
            this.getActor().receiveDamage(100);
        }

    }

    public Label getPhrase() {
        return phrase;
    }

    @Override
    public void deleteActorSprite() {
        // Remove the dialogue text from the AnchorPane
        if (phrase != null && getAnchorPane() != null) {
            getAnchorPane().getChildren().remove(phrase);
                    }


        super.deleteActorSprite();
    }
}
