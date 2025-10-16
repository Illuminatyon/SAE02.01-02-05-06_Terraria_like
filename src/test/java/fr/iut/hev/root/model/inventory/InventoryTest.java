package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Inventory
 * Ces tests vérifient le bon fonctionnement des méthodes de gestion d'inventaire
 */
public class InventoryTest {

    private Inventory inventory;
    private Item woodItem;
    private Item stoneItem;
    private Item ironIngotItem;

    /**
     * Configuration initiale avant chaque test
     * Crée un nouvel inventaire et des items de test
     */
    @BeforeEach
    public void setUp() {
        // Création d'un nouvel inventaire pour chaque test
        inventory = new Inventory();
        // Création de quelques items pour les tests
        woodItem = new Item(ItemsEnum.WOOD);
        stoneItem = new Item(ItemsEnum.STONE);
        ironIngotItem = new Item(ItemsEnum.IRON_INGOT);
    }

    /**
     * Teste l'ajout d'un item dans un slot vide
     */
    @Test
    @DisplayName("Test d'ajout d'un item dans un slot vide")
    public void testAddItemToEmptySlot() {
        // Ajout d'un item dans un slot vide
        HashMap<Item, Integer> result = inventory.add(0, woodItem, 10);

        // Vérification que l'ajout a réussi
        assertNull(result, "L'ajout dans un slot vide devrait retourner null");
        assertEquals(woodItem.getItemEnum(), inventory.getInventorySlot(0).getItem().getItemEnum(),
                "L'item dans le slot devrait être celui qui a été ajouté");
        assertEquals(10, inventory.getInventorySlot(0).getQuantity(),
                "La quantité dans le slot devrait être celle qui a été ajoutée");
        assertEquals(1, inventory.getSlotsOccupied(),
                "Un slot devrait être occupé");
    }

    /**
     * Teste l'ajout d'un item dans un slot déjà occupé par le même type d'item
     */
    @Test
    @DisplayName("Test d'ajout d'un item dans un slot contenant déjà le même type d'item")
    public void testAddSameItemTypeToSlot() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 50);

        // Ajout du même type d'item
        HashMap<Item, Integer> result = inventory.add(0, new Item(ItemsEnum.WOOD), 30);

        // Vérification que l'ajout a été partiellement réussi (car limite de stack = 100)
        assertNull(result, "L'ajout du même type d'item sous la limite de stack devrait retourner null");
        assertEquals(80, inventory.getInventorySlot(0).getQuantity(),
                "La quantité devrait être la somme des deux ajouts");
    }

    /**
     * Teste l'ajout d'un item dépassant la limite d'empilement
     */
    @Test
    @DisplayName("Test d'ajout d'un item dépassant la limite d'empilement")
    public void testAddItemExceedingStackLimit() {
        // Préparation: ajout initial d'un item proche de la limite
        inventory.add(0, woodItem, 90);

        // Ajout qui dépasse la limite
        HashMap<Item, Integer> result = inventory.add(0, new Item(ItemsEnum.WOOD), 20);

        // Vérification que l'excédent est retourné
        assertNotNull(result, "L'ajout dépassant la limite devrait retourner l'excédent");
        assertEquals(1, result.size(), "Un seul type d'item devrait être retourné");
        assertTrue(result.containsKey(woodItem), "L'item retourné devrait être du bois");
        assertEquals(10, result.get(woodItem), "L'excédent devrait être de 10");
        assertEquals(100, inventory.getInventorySlot(0).getQuantity(),
                "La quantité dans le slot devrait être à la limite maximale");
    }

    /**
     * Teste l'ajout d'un item différent dans un slot déjà occupé
     */
    @Test
    @DisplayName("Test d'ajout d'un item différent dans un slot occupé")
    public void testAddDifferentItemToOccupiedSlot() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 50);

        // Ajout d'un item différent
        HashMap<Item, Integer> result = inventory.add(0, stoneItem, 30);

        // Vérification que l'item précédent est retourné
        assertNotNull(result, "L'ajout d'un item différent devrait retourner l'item précédent");
        assertEquals(1, result.size(), "Un seul type d'item devrait être retourné");
        assertTrue(result.containsKey(woodItem), "L'item retourné devrait être du bois");
        assertEquals(50, result.get(woodItem), "La quantité retournée devrait être 50");
        assertEquals(stoneItem.getItemEnum(), inventory.getInventorySlot(0).getItem().getItemEnum(),
                "L'item dans le slot devrait maintenant être de la pierre");
        assertEquals(30, inventory.getInventorySlot(0).getQuantity(),
                "La quantité dans le slot devrait être 30");
    }

    /**
     * Teste l'ajout automatique d'un item dans l'inventaire
     */
    @Test
    @DisplayName("Test d'ajout automatique d'un item dans l'inventaire")
    public void testAutoAddItem() {
        // Ajout automatique d'un item
        inventory.add(woodItem, 50);

        // Vérification que l'item a été ajouté au premier slot disponible
        assertEquals(woodItem.getItemEnum(), inventory.getInventorySlot(0).getItem().getItemEnum(),
                "L'item devrait être ajouté au premier slot");
        assertEquals(50, inventory.getInventorySlot(0).getQuantity(),
                "La quantité devrait être 50");

        // Ajout d'un autre type d'item
        inventory.add(stoneItem, 30);

        // Vérification que l'item a été ajouté au deuxième slot
        assertEquals(stoneItem.getItemEnum(), inventory.getInventorySlot(1).getItem().getItemEnum(),
                "Le deuxième item devrait être ajouté au deuxième slot");
        assertEquals(30, inventory.getInventorySlot(1).getQuantity(),
                "La quantité devrait être 30");
    }

    /**
     * Teste l'ajout automatique d'un item existant dans l'inventaire
     */
    @Test
    @DisplayName("Test d'ajout automatique d'un item existant")
    public void testAutoAddExistingItem() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 50);

        // Ajout automatique du même type d'item
        inventory.add(woodItem, 30);

        // Vérification que l'item a été ajouté au slot existant
        assertEquals(80, inventory.getInventorySlot(0).getQuantity(),
                "La quantité devrait être la somme des deux ajouts");
        assertNull(inventory.getInventorySlot(1).getItem(),
                "Le deuxième slot devrait être vide");
    }

    /**
     * Teste la suppression d'une quantité d'items d'un slot
     */
    @Test
    @DisplayName("Test de suppression partielle d'items d'un slot")
    public void testRemovePartialQuantity() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 50);

        // Suppression partielle
        HashMap<Item, Integer> result = inventory.remove(0, 20);

        // Vérification que la suppression a réussi
        assertNotNull(result, "La suppression devrait retourner les items supprimés");
        assertEquals(1, result.size(), "Un seul type d'item devrait être retourné");
        assertTrue(result.containsKey(woodItem), "L'item retourné devrait être du bois");
        assertEquals(20, result.get(woodItem), "La quantité retournée devrait être 20");
        assertEquals(30, inventory.getInventorySlot(0).getQuantity(),
                "La quantité restante devrait être 30");
    }

    /**
     * Teste la suppression complète d'items d'un slot
     */
    @Test
    @DisplayName("Test de suppression complète d'items d'un slot")
    public void testRemoveEntireQuantity() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 50);

        // Suppression complète
        HashMap<Item, Integer> result = inventory.remove(0, 50);

        // Vérification que la suppression a réussi
        assertNotNull(result, "La suppression devrait retourner les items supprimés");
        assertEquals(1, result.size(), "Un seul type d'item devrait être retourné");
        assertTrue(result.containsKey(woodItem), "L'item retourné devrait être du bois");
        assertEquals(50, result.get(woodItem), "La quantité retournée devrait être 50");
        assertNull(inventory.getInventorySlot(0).getItem(),
                "Le slot devrait être vide");
        assertEquals(0, inventory.getInventorySlot(0).getQuantity(),
                "La quantité devrait être 0");
        assertEquals(0, inventory.getSlotsOccupied(),
                "Aucun slot ne devrait être occupé");
    }

    /**
     * Teste la suppression d'items par type
     */
    @Test
    @DisplayName("Test de suppression d'items par type")
    public void testRemoveByItemType() {
        // Préparation: ajout de plusieurs items du même type dans différents slots
        inventory.add(0, woodItem, 30);
        inventory.add(1, woodItem, 40);
        inventory.add(2, stoneItem, 20);

        // Suppression par type
        inventory.remove(ItemsEnum.WOOD, 50);

        // Vérification que la suppression a réussi
        assertEquals(20, inventory.getInventorySlot(0).getQuantity(),
                "Il devrait rester 20 bois dans le premier slot");
        assertEquals(0, inventory.getInventorySlot(1).getQuantity(),
                "Le deuxième slot devrait être vide");
        assertNull(inventory.getInventorySlot(1).getItem(),
                "L'item du deuxième slot devrait être null");
        assertEquals(20, inventory.getInventorySlot(2).getQuantity(),
                "La pierre ne devrait pas être affectée");
    }

    /**
     * Teste le comptage d'items par type
     */
    @Test
    @DisplayName("Test de comptage d'items par type")
    public void testCountItemsByType() {
        // Préparation: ajout de plusieurs items du même type dans différents slots
        inventory.add(0, woodItem, 30);
        inventory.add(1, woodItem, 40);
        inventory.add(2, stoneItem, 20);

        // Comptage par type
        int woodCount = inventory.getItemIteration(ItemsEnum.WOOD);
        int stoneCount = inventory.getItemIteration(ItemsEnum.STONE);
        int ironCount = inventory.getItemIteration(ItemsEnum.IRON_INGOT);

        // Vérification des résultats
        assertEquals(70, woodCount, "Le total de bois devrait être 70");
        assertEquals(20, stoneCount, "Le total de pierre devrait être 20");
        assertEquals(0, ironCount, "Le total de fer devrait être 0");
    }

    /**
     * Teste le calcul de l'espace disponible pour un item
     */
    @Test
    @DisplayName("Test du calcul d'espace disponible pour un item")
    public void testAvailableRoomForItem() {
        // Préparation: ajout de plusieurs items du même type dans différents slots
        inventory.add(0, woodItem, 70);
        inventory.add(1, woodItem, 90);

        // Calcul de l'espace disponible
        int availableRoom = inventory.getAvailableRoomForItem(woodItem);

        // Vérification du résultat
        assertEquals(40, availableRoom, "L'espace disponible pour le bois devrait être 40 (30 + 10)");
    }

    /**
     * Teste l'ajout d'items provenant du crafting
     */
    @Test
    @DisplayName("Test d'ajout d'items provenant du crafting")
    public void testAddFromCraft() {
        // Préparation: ajout initial d'un item
        inventory.add(0, woodItem, 80);

        // Ajout depuis le crafting
        inventory.addFromCraft(woodItem, 50);

        // Vérification que l'ajout a réussi
        assertEquals(100, inventory.getInventorySlot(0).getQuantity(),
                "Le premier slot devrait être rempli à sa capacité maximale");
        assertEquals(woodItem.getItemEnum(), inventory.getInventorySlot(1).getItem().getItemEnum(),
                "L'excédent devrait être dans le deuxième slot");
        assertEquals(30, inventory.getInventorySlot(1).getQuantity(),
                "L'excédent dans le deuxième slot devrait être 30");
    }
}