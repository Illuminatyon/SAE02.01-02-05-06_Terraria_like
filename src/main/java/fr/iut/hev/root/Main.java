package fr.iut.hev.root;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Scene scene;
//--enable-native-access=javafx.graphics
    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        ImageView worldBackground = FXMLLoader.load(Main.class.getResource("view/world-background.fxml"));
        StackPane land = FXMLLoader.load(Main.class.getResource("view/Stack-background+land.fxml"));
        ImageView player  = FXMLLoader.load(Main.class.getResource("player.fxml"));
        root.getChildren().addAll(worldBackground, land, player);
        //scene = new Scene(root, 1920, 1072);
        scene = new Scene(root, 1920, 1056);
        stage.setTitle("ROOT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}