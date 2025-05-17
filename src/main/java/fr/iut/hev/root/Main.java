package fr.iut.hev.root;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        Pane background = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("world-background.fxml")));
        ImageView player  = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("player.fxml")));
        root.getChildren().addAll(background, player);
        scene = new Scene(root, 300, 300);
        stage.setTitle("ROOT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}