package fr.iut.hev.root;

import com.studiohartman.jamepad.ControllerButton;
import fr.iut.hev.root.model.SceneWrapper;
import fr.iut.hev.root.model.enums.InputDevices;
import fr.iut.hev.root.model.enums.PlayerActions;
import fr.iut.hev.root.model.input.Input;
import fr.iut.hev.root.model.input.InputListener;
import fr.iut.hev.root.model.input.InputManager;
import fr.iut.hev.root.model.input.KeyInput;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        SceneWrapper scene = new SceneWrapper(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}