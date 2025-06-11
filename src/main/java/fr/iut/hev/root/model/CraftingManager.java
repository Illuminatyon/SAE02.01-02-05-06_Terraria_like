package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.items.Item;

import java.util.*;

public class CraftingManager {
    private static final List<Recipe> recipes = new ArrayList<>();

    static {
        recipes.add(new Recipe(ItemsEnum.RAW_CHICKEN, 30, Map.of(ItemsEnum.RAW_CHICKEN, 10)));
    }

    public static List<Recipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    public static Optional<Recipe> getRecipeFor(ItemsEnum targetItem) {
        return recipes.stream()
                .filter(r -> r.getResult() == targetItem)
                .findFirst();
    }

    /**
     * Tente de fabriquer un objet à partir d'une recette et d'un inventaire donnés.
     * <p>
     * Cette méthode vérifie d'abord si l'inventaire contient suffisamment d'ingrédients pour la recette.
     * Si c'est le cas, elle retire les ingrédients nécessaires de l'inventaire, puis tente d'ajouter
     * le ou les objets résultants dans l'inventaire. L'ajout se fait d'abord dans les emplacements
     * contenant déjà le même objet, puis dans les emplacements vides si besoin.
     * </p>
     *
     * <h2>Étapes détaillées :</h2>
     * <ol>
     *   <li>
     *     <b>Vérification de la possibilité de craft :</b><br>
     *     Utilise {@link #canCraft(Recipe, Inventory)} pour s'assurer que l'inventaire possède
     *     tous les ingrédients nécessaires en quantité suffisante. Si ce n'est pas le cas, la méthode retourne <code>false</code>.
     *   </li>
     *   <li>
     *     <b>Retrait des ingrédients :</b><br>
     *     Pour chaque ingrédient de la recette, parcourt tous les slots de l'inventaire pour retirer la quantité requise.
     *     Si un slot contient l'ingrédient, on retire autant que possible jusqu'à atteindre la quantité nécessaire.
     *     Si un slot est vidé, il est mis à <code>null</code>.
     *   </li>
     *   <li>
     *     <b>Ajout du résultat :</b><br>
     *     On tente d'ajouter le résultat de la recette dans l'inventaire :
     *     <ul>
     *       <li>D'abord dans les slots contenant déjà le même objet (empilement).</li>
     *       <li>Puis dans les slots vides si besoin.</li>
     *     </ul>
     *     L'ajout utilise la méthode <code>inventory.add()</code> qui peut retourner une map des objets non ajoutés
     *     (par manque de place). On continue jusqu'à ce que tout soit ajouté ou que l'inventaire soit plein.
     *   </li>
     * </ol>
     *
     * @param recipe    La recette à fabriquer (ne doit pas être <code>null</code>).
     * @param inventory L'inventaire du joueur ou du conteneur (ne doit pas être <code>null</code>).
     * @return <code>true</code> si la fabrication a réussi (ingrédients retirés et résultat ajouté), <code>false</code> sinon.
     *
     * @see #canCraft(Recipe, Inventory)
     * @see Inventory#add(int, Item, int)
     * @see Recipe
     * @see Inventory
     *
     * <h2>Exemple d'utilisation :</h2>
     * <pre>
     * Recipe recipe = ...;
     * Inventory inventory = ...;
     * boolean success = CraftingManager.craft(recipe, inventory);
     * if (success) {
     *     System.out.println("Objet fabriqué !");
     * } else {
     *     System.out.println("Ingrédients insuffisants.");
     * }
     * </pre>
     */
    public static boolean craft(Recipe recipe, Inventory inventory) {
        // mettre le code dans Inventory pour que l'inventaire puisse faire le craft
        if (!canCraft(recipe, inventory)) return false;
        for (Map.Entry<ItemsEnum, Integer> ingredient : recipe.getIngredients().entrySet()) {
            int remaining = ingredient.getValue();

            for (InventorySlot slot : inventory.getSlots()) {
                if (slot.getItem() != null && slot.getItem().getItemEnum() == ingredient.getKey()) {
                    int qty = slot.getQuantity();
                    if (qty >= remaining) {
                        slot.setQuantity(qty - remaining);
                        if (slot.getQuantity() == 0) slot.setItem(null);
                        remaining = 0;
                    } else {
                        slot.setQuantity(0);
                        slot.setItem(null);
                        remaining -= qty;
                    }
                    if (remaining == 0) break;
                }
            }
        }

        int amountToAdd = recipe.getAmount();
        boolean added = false;
        int i = 0;
        while (i < inventory.getSize() && !added) {
            InventorySlot slot = inventory.getInventorySlot(i);
            if (slot.getItem() != null && slot.getItem().getItemEnum() == recipe.getResult()) {
                HashMap<Item, Integer> replaced = inventory.add(i, new Item(recipe.getResult()), amountToAdd);
                if (replaced == null || replaced.isEmpty()) {
                    added = true;
                    amountToAdd = 0;
                } else {
                    for (Map.Entry<Item, Integer> e : replaced.entrySet()) {
                        amountToAdd = e.getValue();
                    }
                }
            }
            i++;
        }
        i = 0;
        while (!added && amountToAdd > 0 && i < inventory.getSize()) {
            InventorySlot slot = inventory.getInventorySlot(i);
            if (slot.getItem() == null) {
                HashMap<Item, Integer> replaced = inventory.add(i, new Item(recipe.getResult()), amountToAdd);
                if (replaced == null || replaced.isEmpty()) {
                    added = true;
                    amountToAdd = 0;
                } else {
                    for (Map.Entry<Item, Integer> e : replaced.entrySet()) {
                        amountToAdd = e.getValue();
                    }
                }
            }
            i++;
        }
        return true;
    }

    public static boolean canCraft(Recipe recipe, Inventory inventory) {
        Map<ItemsEnum, Integer> counts = new HashMap<>();
        for (InventorySlot slot : inventory.getSlots()) {
            if (slot.getItem() != null) {
                counts.put(slot.getItem().getItemEnum(), counts.getOrDefault(slot.getItem().getItemEnum(), 0) + slot.getQuantity());
            }
        }
        return canCraft(recipe, counts);
    }

    public static boolean canCraft(Recipe recipe, Map<ItemsEnum, Integer> counts) {
        for (Map.Entry<ItemsEnum, Integer> ingredient : recipe.getIngredients().entrySet()) {
            ItemsEnum item = ingredient.getKey();
            int requiredAmount = ingredient.getValue();
            int availableAmount = counts.getOrDefault(item, 0);
            if (availableAmount < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    public static Recipe[] getAllRecipes() {
        return recipes.toArray(new Recipe[0]);
    }
}
