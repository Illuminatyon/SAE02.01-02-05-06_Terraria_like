package fr.iut.hev.root.view;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class HotbarView {

    private GridPane hotbar;


    public HotbarView(GridPane hotbar) {
        this.hotbar = hotbar;
        initHotbar();
    }

    public void resetHighlight(int index) {
        Pane currentPane = (Pane) hotbar.getChildren().get(index);
        currentPane.setBackground(Background.fill(Color.rgb(0, 0, 0, 0.25)));
    }

    public void setHighlight(int index) {
        Pane currentPane = (Pane) hotbar.getChildren().get(index);
        currentPane.setBackground(Background.fill(Color.rgb(255, 255, 255, 0.25)));

    }

    public void initHotbar() {
        if (!hotbar.getChildren().isEmpty()) { // <-- Ajout de cette vérification
            Pane currentPane = (Pane) hotbar.getChildren().getFirst();
            currentPane.setBackground(Background.fill(Color.rgb(255, 255, 255, 0.25)));
        } else {
            System.err.println("WARNING: hotbar.getChildren() est vide. Impossible de mettre en surbrillance le premier slot.");
            // Tu pourrais vouloir initialiser la hotbar avec des slots ici si ce n'est pas fait via FXML.
        }
    }
}
