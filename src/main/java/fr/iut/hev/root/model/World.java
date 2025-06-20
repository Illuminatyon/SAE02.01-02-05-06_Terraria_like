package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.ArrayList;

public class World {
    private StringProperty nameProperty;
    private long lastPlayed;
    private ItemFactory itemFactory;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Actor> aliveMobs;
    private HitboxManager hitboxManager;

    public World(String name, TileMap tileMap, Player player, ArrayList<Actor> aliveMobs, HitboxManager hitboxManager, ItemFactory itemFactory) throws IOException {
        this.nameProperty = new SimpleStringProperty();
        this.nameProperty.set(name);
        this.lastPlayed = System.currentTimeMillis();
        this.tileMap = tileMap;
        this.player = player;
        this.aliveMobs = aliveMobs;
        this.hitboxManager = hitboxManager;
        this.itemFactory = itemFactory;
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

    public ArrayList<Actor> getAliveMobs() {
        return this.aliveMobs;
    }

    public void setAliveMobs(ArrayList<Actor> aliveMobs) {
        this.aliveMobs = aliveMobs;
    }

    public ItemFactory getItemFactory() {
        return itemFactory;
    }
}