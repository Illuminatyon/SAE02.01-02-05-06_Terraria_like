package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.entities.LootManager;
import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.entities.actor.NpcFactory.NPCFactory;
import fr.iut.hev.root.model.entities.actor.NpcFactory.PassiveNpcFactory;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;
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
    private ArrayList<Mob> aliveMobs;
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
        this.player.initPlayer(100,100);
        this.tileMap.initTileMap(itemFactory,width,height);
    }

    private void updateAliveMobs() {
        for (int i = aliveMobs.size() - 1; i >= 0; i--) {
            Actor currentActor = aliveMobs.get(i);
            if (currentActor != null) {
                currentActor.updatePosition();
            } else {
                aliveMobs.remove(i);
            }
        }
    }

    private void updateLoots() {
        for (Loot loot : lootManager.getLootOnMap()) {
            loot.updatePosition();
        }
    }

    public void updateWorld() {
        player.update();
        updateAliveMobs();
        updateLoots();
    }

    public void addMob(ActorEnum actor) {// Fonction temporaire Vue qu'onn a pas vrm de logique de spawn
        Mob poulet = new PassiveNpcFactory().mobFactory(actor, 110, 100);
        this.aliveMobs.add(poulet);

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

    public ArrayList<Mob> getAliveMobs() {
        return this.aliveMobs;
    }

    public LootManager getLootManager() {
        return this.lootManager;
    }

}