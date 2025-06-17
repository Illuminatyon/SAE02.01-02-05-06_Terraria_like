package fr.iut.hev.root.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.ArrayList;

public class World {
    private StringProperty nameProperty;
    // Maybe put the game loop here?
    private TileMap tileMap;
    private Player player;
    private ArrayList<Actor> aliveActors;

    public World(String name) {
        this.nameProperty = new SimpleStringProperty();
        this.nameProperty.set(name);
    }

    public void createWorld() throws Exception {
        new Thread(() -> {
            File worldDir = new File("saves/" + nameProperty.get());
            if (!worldDir.exists()) {
                worldDir.mkdirs();
            }

            TileMap tileMap = new TileMap(1920, 1080);
            Player player = new Player(0, 0, 16, 32, tileMap, 1, 1, 1);
        });
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public String getName() {
        return this.nameProperty.get();
    }

    public void setName(String name) {
        this.nameProperty.set(name);
    }
}