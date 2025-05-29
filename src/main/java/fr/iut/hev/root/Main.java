package fr.iut.hev.root;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Scene scene;
//--enable-native-access=javafx.graphics
    @Override
    public void start(Stage stage) throws IOException {
        Pane root = FXMLLoader.load(Main.class.getResource("view/globalView.fxml"));
        scene = new Scene(root, 1920, 1056);
        stage.setTitle("ROOT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}