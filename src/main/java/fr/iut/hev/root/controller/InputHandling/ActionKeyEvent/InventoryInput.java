package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.scene.input.KeyCode;
/**
 * <h2>Gestionnaire des entrées clavier pour l'inventaire et le craft</h2>
 *
 * <p>Implémente {@link KeyCodeInterface} pour gérer les touches lorsque
 * l'inventaire est ouvert. Cette stratégie remplace {@link PlayerInput}
 * dans ce contexte.</p>
 *
 * <p><strong>Touches gérées :</strong></p>
 * <ul>
 *   <li><strong>E :</strong> Ouvre/ferme l'inventaire</li>
 *   <li><strong>R :</strong> Ouvre l'interface de craft (si inventaire ouvert)</li>
 *   <li><strong>TAB :</strong> Ouvre la carte (futur)</li>
 * </ul>
 *
 * @see KeyCodeInterface
 * @see PlayerInput
 */
public class InventoryInput implements KeyCodeInterface {

    private final InventoryView inventoryView;
    private final CraftView craftView;

    /**
     * Constructeur du gestionnaire d'entrées inventaire.
     *
     * @param inventoryView Vue de l'inventaire
     * @param craftView Vue de l'interface de craft
     */
    public InventoryInput(InventoryView inventoryView, CraftView craftView) {
        this.inventoryView = inventoryView;
        this.craftView = craftView;
    }

    /**
     * Gère les touches pressées dans le contexte inventaire.
     *
     * @param code Le code de la touche pressée
     */
    @Override
    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case E -> inventoryView.setInventoryVisible();
            case R -> {
                if (inventoryView.getInventoryOpened()) {
                    craftView.setCraftGUIVisible();
                }
            }
            case TAB -> System.out.println("map opened");
        }
    }

    /**
     * Aucune gestion du relâchement dans ce contexte.
     *
     * @param code Le code de la touche relâchée
     */
    @Override
    public void handleKeyReleased(KeyCode code) {
        // Pas de relâchement géré ici
    }
}
