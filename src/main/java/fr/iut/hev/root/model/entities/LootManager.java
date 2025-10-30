package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.land.TileMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import java.util.Random;

public class LootManager {

    private final ObservableSet<Loot> lootsOnMap;
    private final ItemFactory itemFactory;
    private final Random random;

    public LootManager(ItemFactory itemFactory) {
        this.lootsOnMap = FXCollections.observableSet();
        this.itemFactory = itemFactory;
        this.random = new Random();
    }

    //TODO: pourquoi pas laisser la responsabilité du drop du loot à l'actor ? Dans ce cas, le lootManager servirait uniquement à stocker tous les loots et faire attention à leur aspiration
    public void dropLootForActor(Actor actor) {
        if (actor.getName().equals("poulet")) {
            dropChickenLoot(actor);
        }
        // TODO : On ajoute ici les trucs avec les autres méthodes
        // Par exemple, on pourrait faire ça :
        /*else if (actor.getName().equals("vache")) {
            dropCowLoot(actor);
        }*/
    }

    // Voici un exemple pour le fonctionnement du loot sur une vache ( même si on a pas de vache dans le jeu )
    /*private void dropCowLoot(Actor actor) {
        int numBeef = random.nextInt(2) + 1;
        for (int i = 0; i < numBeef; i++) {
            Loot loot = createLoot(ItemsEnum.RAW_MEAT, 1, ...); // Bien évidemment, à modifier pour le code fonctionne
            // Mais sur le papier, et si on avait plus de trucs dans l'Enum
            lootsOnMap.add(loot);
        }
    }*/


    private void dropChickenLoot(Actor actor) {
        int numChicken = random.nextInt(3) + 1;
        //System.out.println("[DEBUG_LOG] Chicken died, dropping " + numChicken + " raw chicken pieces");
        for (int i = 0; i < numChicken; i++) {
            Loot loot = createLoot(
                    ItemsEnum.RAW_CHICKEN,
                    1,
                    (int) actor.getPosX() + random.nextInt(20) - 10,
                    (int) actor.getPosY(),
                    32,
                    32
            );
            lootsOnMap.add(loot);
            //System.out.println("[DEBUG_LOG] Dropped raw chicken at position (" + loot.getPosX() + ", " + loot.getPosY() + ")");
        }
    }

    public ObservableSet<Loot> getLootOnMap() {
        return FXCollections.unmodifiableObservableSet(lootsOnMap);
    }

    public Loot createLoot(ItemsEnum itemType, int quantity, int posX, int posY, int width, int height) {
        return new Loot(
                itemFactory.createItem(itemType),
                quantity,
                posX,
                posY,
                width,
                height
        );
    }
}
