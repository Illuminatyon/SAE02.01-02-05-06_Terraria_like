package fr.iut.hev.root;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
/**/
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {
    public Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/globalView.fxml"));
        Pane root = fxmlLoader.load();
        GlobalController globalController = fxmlLoader.getController();

        // Create a default world

        scene = new Scene(root, 1400, 600);
        stage.setTitle("ROOT - Playing on Default World");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
