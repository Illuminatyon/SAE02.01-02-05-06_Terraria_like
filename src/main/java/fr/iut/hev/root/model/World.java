package fr.iut.hev.root.model;

import com.google.gson.annotations.Expose;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Mob;
import fr.iut.hev.root.model.entities.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.ArrayList;

public class World {
    @Expose private StringProperty nameProperty;
    @Expose private long lastPlayed;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Actor> aliveActors;

    public World() {
    }

    public World(String name, TileMap tileMap, Player player, ArrayList<Actor> aliveMobs) throws IOException {
        this.nameProperty = new SimpleStringProperty();
        this.nameProperty.set(name);
        this.lastPlayed = System.currentTimeMillis();
        this.tileMap = tileMap;
        this.player = player;
        this.aliveActors = aliveActors;
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

    public long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public TileMap getTileMap() {
        return this.tileMap;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Actor> getAliveActors() {
        return this.aliveActors;
    }

    public void setAliveActors(ArrayList<Actor> aliveActors) {
        this.aliveActors = aliveActors;
    }
}