
package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.exception.InsufficientQuantityException;
import fr.iut.hev.root.model.items.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * <h2>Emplacement individuel dans un inventaire</h2>
 *
 * <p>Cette classe représente un seul slot (emplacement) d'un inventaire pouvant
 * contenir un type d'item et une quantité. Elle utilise les <strong>JavaFX Properties</strong>
 * pour permettre le binding automatique avec l'interface graphique.</p>
 *
 * <p><strong>Caractéristiques :</strong></p>
 * <ul>
 *   <li>Peut contenir un seul type d'item à la fois</li>
 *   <li>Gère automatiquement le stacking (empilement) des items</li>
 *   <li>Position fixe dans l'inventaire via un index</li>
 *   <li>État observable via JavaFX Properties (réactivité UI)</li>
 * </ul>
 *
 * <p><strong>États possibles :</strong></p>
 * <ul>
 *   <li><strong>Vide :</strong> item = null, quantity = 0</li>
 *   <li><strong>Occupé :</strong> item != null, quantity > 0</li>
 * </ul>
 *
 * <p><strong>Exemple d'utilisation :</strong></p>
 * <pre>{@code
 * InventorySlot slot = new InventorySlot(0);
 * slot.setItem(ItemFactory.createItem(ItemsEnum.WOOD));
 * slot.setQuantity(10);
 *
 * // Binding avec l'UI
 * label.textProperty().bind(slot.quantityProperty().asString());
 * }</pre>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see AbstractInventory
 * @see Item
 * @see javafx.beans.property.Property
 * @since 1.0
 */
public class InventorySlot {

    /**
     * Property contenant l'item stocké dans ce slot (null si vide).
     * Utilise ObjectProperty pour permettre le binding avec l'UI.
     */
    private ObjectProperty<Item> itemProperty;

    /**
     * Property contenant la quantité d'items dans ce slot.
     * Utilise IntegerProperty pour permettre le binding avec l'UI.
     */
    private IntegerProperty quantityProperty;

    /**
     * Index du slot dans l'inventaire (position fixe, non modifiable).
     */
    private final int index;

    /**
     * Constructeur d'un slot d'inventaire.
     *
     * <p>Initialise un slot vide avec une position donnée dans l'inventaire.</p>
     *
     * @param index La position du slot dans l'inventaire (0-based)
     */
    public InventorySlot(int index) {
        this.itemProperty = new SimpleObjectProperty<Item>();
        this.quantityProperty = new SimpleIntegerProperty(0);
        this.index = index;
    }

    /**
     * <h3>Retirer des items du slot</h3>
     *
     * <p>Retire une quantité spécifiée d'items de ce slot. Si la quantité
     * atteint zéro, le slot devient vide (item = null).</p>
     *
     * <p><strong>Comportement :</strong></p>
     * <ul>
     *   <li>Si quantity devient 0 → Le slot est vidé (item = null)</li>
     *   <li>Si quantity reste > 0 → Le slot conserve l'item avec la nouvelle quantité</li>
     * </ul>
     *
     * @param quantity La quantité à retirer (doit être > 0)
     * @throws InsufficientQuantityException Si quantity > quantité disponible
     * @see InsufficientQuantityException
     */
    public void remove(int quantity) {
        if (quantity > quantityProperty.get()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot remove ");
            sb.append(quantity);
            sb.append(" from ");
            sb.append(this.itemProperty.get().getItemEnum());
            sb.append(" at slot ");
            sb.append(this.index);
            sb.append(": only ");
            sb.append(this.quantityProperty.get());
            sb.append(" available");
            throw new InsufficientQuantityException(sb.toString());
        }

        quantityProperty.set(quantityProperty.get() - quantity);
        if (quantityProperty.get() == 0) {
            itemProperty.set(null);
        }
    }

    /**
     * <h3>Obtenir l'item du slot</h3>
     *
     * @return L'item contenu dans le slot, ou null si le slot est vide
     */
    public Item getItem() {
        return this.itemProperty.get();
    }

    /**
     * <h3>Définir l'item du slot</h3>
     *
     * <p>Change l'item stocké dans ce slot. Ne modifie pas la quantité.</p>
     *
     * @param item Le nouvel item à stocker (peut être null pour vider)
     */
    public void setItem(Item item) {
        this.itemProperty.set(item);
    }

    /**
     * <h3>Obtenir la property de l'item</h3>
     *
     * <p>Permet le binding bidirectionnel avec l'UI JavaFX.</p>
     *
     * @return La property observable de l'item
     * @see ObjectProperty
     */
    public ObjectProperty<Item> itemProperty() {
        return this.itemProperty;
    }

    /**
     * <h3>Obtenir la quantité</h3>
     *
     * @return La quantité d'items dans le slot (0 si vide)
     */
    public int getQuantity() {
        return this.quantityProperty.get();
    }

    /**
     * <h3>Définir la quantité</h3>
     *
     * <p>Modifie directement la quantité d'items dans le slot.</p>
     *
     * @param value La nouvelle quantité (doit être ≥ 0)
     */
    public void setQuantity(int value) {
        this.quantityProperty.set(value);
    }

    /**
     * <h3>Obtenir la property de la quantité</h3>
     *
     * <p>Permet le binding bidirectionnel avec l'UI JavaFX.</p>
     *
     * @return La property observable de la quantité
     * @see IntegerProperty
     */
    public IntegerProperty quantityProperty() {
        return this.quantityProperty;
    }

    /**
     * <h3>Obtenir l'index du slot</h3>
     *
     * @return La position du slot dans l'inventaire (0-based, immutable)
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * <h3>Vérifier si le slot est vide</h3>
     *
     * <p>Un slot est considéré vide si sa quantité est égale à zéro,
     * ce qui implique normalement que l'item est null.</p>
     *
     * @return true si le slot est vide (quantity == 0), false sinon
     */
    public boolean isEmpty() {return quantityProperty.getValue() == 0;}
}