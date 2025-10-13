package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.items.enums.ItemTypesEnum;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Item et ses sous-classes
 * Ces tests vérifient le bon fonctionnement des items du jeu
 */
public class ItemTest {

    private Item woodItem;
    private Item stoneItem;
    private Weapon daggerItem;
    private Tool pickaxeItem;
    private HitboxManager hitboxManager;

    /**
     * Configuration initiale avant chaque test
     * Crée différents types d'items pour les tests
     */
    @BeforeEach
    public void setUp() {
        // Création du gestionnaire de hitboxes pour les armes
        hitboxManager = new HitboxManager(); // A modifier pour fonctionne avec le singleton

        // Création d'items de base
        woodItem = new Item(ItemsEnum.WOOD);
        stoneItem = new Item(ItemsEnum.STONE);

        // Création d'une arme
        daggerItem = new Weapon(ItemsEnum.DAGGER, hitboxManager);

        // Création d'un outil
        pickaxeItem = new Tool(ItemsEnum.WOODEN_PICKAXE);
    }

    /**
     * Teste la création d'items de base
     */
    @Test
    @DisplayName("Test de création d'items de base")
    public void testCreateBasicItems() {
        // Vérification des propriétés de l'item en bois
        assertEquals(ItemsEnum.WOOD, woodItem.getItemEnum(),
                "L'item devrait être du bois");
        assertEquals(ItemTypesEnum.RESOURCES, woodItem.getItemEnum().getItemType(),
                "Le type d'item devrait être RESOURCES");
        assertEquals(100, woodItem.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 100");
        assertEquals(0, woodItem.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0");

        // Vérification des propriétés de l'item en pierre
        assertEquals(ItemsEnum.STONE, stoneItem.getItemEnum(),
                "L'item devrait être de la pierre");
        assertEquals(ItemTypesEnum.BLOCK, stoneItem.getItemEnum().getItemType(),
                "Le type d'item devrait être BLOCK");
        assertEquals(100, stoneItem.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 100");
        assertEquals(0.2, stoneItem.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0.2");
    }

    /**
     * Teste la création d'une arme
     */
    @Test
    @DisplayName("Test de création d'une arme")
    public void testCreateWeapon() {
        // Vérification des propriétés de la dague
        assertEquals(ItemsEnum.DAGGER, daggerItem.getItemEnum(),
                "L'item devrait être une dague");
        assertEquals(ItemTypesEnum.WEAPON, daggerItem.getItemEnum().getItemType(),
                "Le type d'item devrait être WEAPON");
        assertEquals(1, daggerItem.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 1");
        assertEquals(0.5, daggerItem.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0.5");
        assertNotNull(daggerItem.getStats(),
                "Les statistiques de l'arme ne devraient pas être nulles");
    }

    /**
     * Teste la création d'un outil
     */
    @Test
    @DisplayName("Test de création d'un outil")
    public void testCreateTool() {
        // Vérification des propriétés de la pioche en bois
        assertEquals(ItemsEnum.WOODEN_PICKAXE, pickaxeItem.getItemEnum(),
                "L'item devrait être une pioche en bois");
        assertEquals(ItemTypesEnum.TOOL, pickaxeItem.getItemEnum().getItemType(),
                "Le type d'item devrait être TOOL");
        assertEquals(1, pickaxeItem.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 1");
        assertEquals(0, pickaxeItem.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0");
        assertNotNull(pickaxeItem.getStats(),
                "Les statistiques de l'outil ne devraient pas être nulles");
    }

    /**
     * Teste la création d'un bloc
     */
    @Test
    @DisplayName("Test de création d'un bloc")
    public void testCreateBlock() {
        // Création d'un bloc
        Block stoneBlock = new Block(ItemsEnum.STONE);

        // Vérification des propriétés du bloc
        assertEquals(ItemsEnum.STONE, stoneBlock.getItemEnum(),
                "L'item devrait être de la pierre");
        assertEquals(ItemTypesEnum.BLOCK, stoneBlock.getItemEnum().getItemType(),
                "Le type d'item devrait être BLOCK");
        assertEquals(100, stoneBlock.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 100");
        assertEquals(0.2, stoneBlock.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0.2");
    }

    /**
     * Teste la création d'une ressource
     */
    @Test
    @DisplayName("Test de création d'une ressource")
    public void testCreateResource() {
        // Création d'une ressource
        Resource woodResource = new Resource(ItemsEnum.WOOD);

        // Vérification des propriétés de la ressource
        assertEquals(ItemsEnum.WOOD, woodResource.getItemEnum(),
                "L'item devrait être du bois");
        assertEquals(ItemTypesEnum.RESOURCES, woodResource.getItemEnum().getItemType(),
                "Le type d'item devrait être RESOURCES");
        assertEquals(100, woodResource.getItemEnum().getLimitStacking(),
                "La limite d'empilement devrait être 100");
        assertEquals(0, woodResource.getItemEnum().getCooldown(),
                "Le temps de recharge devrait être 0");
    }

    /**
     * Teste la modification d'un item
     */
    @Test
    @DisplayName("Test de modification d'un item")
    public void testModifyItem() {
        // Modification de l'item en bois pour en faire de la pierre
        woodItem.setItemEnum(ItemsEnum.STONE);

        // Vérification que l'item a été modifié
        assertEquals(ItemsEnum.STONE, woodItem.getItemEnum(),
                "L'item devrait maintenant être de la pierre");
        assertEquals(ItemTypesEnum.BLOCK, woodItem.getItemEnum().getItemType(),
                "Le type d'item devrait maintenant être BLOCK");
    }

    /**
     * Teste la comparaison d'items
     */
    @Test
    @DisplayName("Test de comparaison d'items")
    public void testCompareItems() {
        // Création d'un nouvel item en bois
        Item anotherWoodItem = new Item(ItemsEnum.WOOD);

        // Vérification que les deux items en bois sont considérés comme du même type
        assertEquals(woodItem.getItemEnum(), anotherWoodItem.getItemEnum(),
                "Les deux items devraient avoir le même type");

        // Vérification que l'item en bois et l'item en pierre sont considérés comme différents
        assertNotEquals(woodItem.getItemEnum(), stoneItem.getItemEnum(),
                "Les items devraient avoir des types différents");
    }

    /**
     * Teste les statistiques des items
     */
    @Test
    @DisplayName("Test des statistiques des items")
    public void testItemStats() {
        // Vérification que les items de base n'ont pas de statistiques
        assertNull(woodItem.getStats(),
                "L'item en bois ne devrait pas avoir de statistiques");
        assertNull(stoneItem.getStats(),
                "L'item en pierre ne devrait pas avoir de statistiques");

        // Vérification que les armes et outils ont des statistiques
        assertNotNull(daggerItem.getStats(),
                "La dague devrait avoir des statistiques");
        assertNotNull(pickaxeItem.getStats(),
                "La pioche devrait avoir des statistiques");
    }

    /**
     * Teste le temps de recharge des items
     */
    @Test
    @DisplayName("Test du temps de recharge des items")
    public void testItemCooldown() {
        // Vérification du temps de recharge des items
        assertEquals(0, woodItem.getCooldown(),
                "Le temps de recharge du bois devrait être 0");
        assertEquals(0.2, stoneItem.getCooldown(),
                "Le temps de recharge de la pierre devrait être 0.2");
        assertEquals(0.5, daggerItem.getCooldown(),
                "Le temps de recharge de la dague devrait être 0.5");
    }
}