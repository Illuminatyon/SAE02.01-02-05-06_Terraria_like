package fr.iut.hev.root;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestTilemap extends Application {

    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TestTilemap.class.getResource("view/Stack-background+land.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1920, 1072);
        stage.setTitle("ROOT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
