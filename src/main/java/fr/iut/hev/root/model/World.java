package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.LootManager;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;

import java.util.ArrayList;

public class World {

    private static World world = null;

    private ItemFactory itemFactory;
    private TileMap tileMap;
    private Player player;
    private HitboxManager hitboxManager;
    private ArrayList<Actor> aliveMobs;
    private LootManager lootManager;

    private World() {
        this.itemFactory = null;
        this.tileMap = null;
        this.player = null;
        this.hitboxManager = null;
        this.aliveMobs = null;
        this.lootManager = null;
    }

    public static World getInstance() {
        if(world==null) {
            world= new World();
        }
        return world;
    }

    public void initWorld(int width, int height) {
        this.hitboxManager = HitboxManager.getInstance();
        this.aliveMobs = new ArrayList<>();
        this.tileMap = TileMap.getInstance();
        this.itemFactory = ItemFactory.getInstance();
        this.lootManager = new LootManager(this.itemFactory);
        this.player = Player.getInstance();
        this.player.initPlayer(100,100,32,48,this.tileMap,5,10,3);
        this.tileMap.initTileMap(itemFactory,width,height);
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

    public LootManager getLootManager() {
        return this.lootManager;
    }

}