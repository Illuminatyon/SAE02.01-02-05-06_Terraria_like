package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.ArrayList;

public class World {
<<<<<<< HEAD
    private static World world = null;

    private ItemFactory itemFactory;
    private TileMap tileMap;
    private Player player;
    private HitboxManager hitboxManager;
    private ArrayList<Actor> aliveMobs;
=======

    private StringProperty nameProperty;
    private long lastPlayed;
    private ItemFactory itemFactory;
    private TileMap tileMap;
    private Player player;
    private ArrayList<Actor> aliveMobs;
    private HitboxManager hitboxManager; // TODO : ??
>>>>>>> 90a010a1c5d23d965d450b48eff6a6ce3712befe

    private World() {
        this.itemFactory = null;
        this.tileMap = null;
        this.player = null;
        this.hitboxManager = null;
        this.aliveMobs = null;
    }


    public static World getInstance() {
        if(world==null) {
            world= new World();
        }
        return world;
    }

    public void initWorld(int width, int height) throws IOException {
        this.hitboxManager = HitboxManager.getInstance();
        this.aliveMobs=new ArrayList<>();
        this.tileMap = TileMap.getInstance();
        this.itemFactory = ItemFactory.getInstance();
        this.player = Player.getInstance(100, 100, 32, 48, this.tileMap, 5, 10,  3, ActorEnum.PLAYER, this.hitboxManager);
        this.tileMap.initTileMap(itemFactory,width,height);
    }

    public void mobAdd(Actor actor) {
        this.aliveMobs.add(actor);
    }

<<<<<<< HEAD
=======
    public long getLastPlayed() {
        return lastPlayed;
    } // TODO : retirer les setters et guetters qui ne servent pas

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    } // TODO : retirer les setters et guetters qui ne servent pas
>>>>>>> 90a010a1c5d23d965d450b48eff6a6ce3712befe

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
    } // TODO : retirer les setters et guetters qui ne servent pas

    public ItemFactory getItemFactory() {
        return itemFactory;
    }
}