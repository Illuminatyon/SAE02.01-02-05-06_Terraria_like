
package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.entities.actor.PlayerMouvementsEnum;
import javafx.scene.input.KeyCode;

/**
 * <h2>Gestionnaire des entrées clavier pour les mouvements du joueur</h2>
 *
 * <p>Cette classe implémente l'interface {@link KeyCodeInterface} pour gérer
 * les touches de déplacement et d'actions du joueur pendant le gameplay normal.
 * Elle fait partie du pattern <strong>Strategy</strong> et s'active lorsque
 * l'inventaire est fermé.</p>
 *
 * <p><strong>Contexte d'utilisation :</strong></p>
 * <p>Cette stratégie est la stratégie par défaut, active lorsque le joueur
 * est en mode jeu normal (inventaire fermé). Elle gère tous les déplacements
 * et actions physiques du personnage.</p>
 *
 * <p><strong>Contrôles implémentés :</strong></p>
 * <table border="1">
 *   <tr>
 *     <th>Touche</th>
 *     <th>Action</th>
 *     <th>Type</th>
 *   </tr>
 *   <tr>
 *     <td><strong>Q</strong></td>
 *     <td>Déplacement vers la gauche</td>
 *     <td>Continue (maintenir)</td>
 *   </tr>
 *   <tr>
 *     <td><strong>D</strong></td>
 *     <td>Déplacement vers la droite</td>
 *     <td>Continue (maintenir)</td>
 *   </tr>
 *   <tr>
 *     <td><strong>ESPACE</strong></td>
 *     <td>Saut</td>
 *     <td>Impulsion (une seule fois)</td>
 *   </tr>
 * </table>
 *
 * <p><strong>Système de mouvements :</strong></p>
 * <p>Utilise un système d'ajout/retrait de mouvements actifs via
 * {@link PlayerMouvementsEnum}. Cela permet de gérer plusieurs touches
 * pressées simultanément (ex: sauter en se déplaçant).</p>
 *
 * <p><strong>Architecture :</strong></p>
 * <pre>
 * PlayerInput
 *    ↓ gère
 * Player (singleton)
 *    ↓ contient
 * Set&lt;PlayerMouvementsEnum&gt; (mouvements actifs)
 *    ↓ mis à jour par
 * addPlayerMouvements() / removePlayerMouvements()
 * </pre>
 *
 * <p><strong>Exemple d'utilisation :</strong></p>
 * <pre>{@code
 * PlayerInput playerInput = new PlayerInput();
 *
 * // Appuyer sur Q → Le joueur commence à se déplacer à gauche
 * playerInput.handleKeyPressed(KeyCode.Q);
 *
 * // Relâcher Q → Le joueur arrête de se déplacer à gauche
 * playerInput.handleKeyReleased(KeyCode.Q);
 * }</pre>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see KeyCodeInterface
 * @see Player
 * @see PlayerMouvementsEnum
 * @see InventoryInput
 * @since 1.0
 */
public class PlayerInput implements KeyCodeInterface {

    /**
     * Instance singleton du joueur.
     * Toutes les commandes sont appliquées directement sur cette instance unique.
     */
    private final Player player = Player.getInstance();

    /**
     * <h3>Gestion des touches pressées</h3>
     *
     * <p>Ajoute le mouvement correspondant à la liste des mouvements actifs du joueur.
     * Le joueur maintiendra ce mouvement jusqu'à ce que la touche soit relâchée.</p>
     *
     * <p><strong>Mécanisme de mouvements continus :</strong></p>
     * <ol>
     *   <li>La touche est pressée → Le mouvement est ajouté au Set</li>
     *   <li>La game loop vérifie les mouvements actifs → Applique la vélocité</li>
     *   <li>La touche est relâchée → Le mouvement est retiré du Set</li>
     * </ol>
     *
     * <p><strong>Gestion du saut :</strong></p>
     * <p>Le saut est une impulsion unique. Même si la touche ESPACE reste pressée,
     * le joueur ne sautera qu'une fois jusqu'à ce qu'il retouche le sol.</p>
     *
     * <p><strong>Actions par touche :</strong></p>
     * <ul>
     *   <li><strong>Q :</strong> Ajoute MOVE_LEFT → Vélocité négative en X</li>
     *   <li><strong>D :</strong> Ajoute MOVE_RIGHT → Vélocité positive en X</li>
     *   <li><strong>ESPACE :</strong> Ajoute JUMP → Impulsion négative en Y (vers le haut)</li>
     * </ul>
     *
     * @param code Le code de la touche pressée
     * @see KeyCode
     * @see Player#addPlayerMouvements(PlayerMouvementsEnum)
     * @see PlayerMouvementsEnum
     */
    @Override
    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case Q -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.addPlayerMouvements(PlayerMouvementsEnum.JUMP);
        }
    }

    /**
     * <h3>Gestion des touches relâchées</h3>
     *
     * <p>Retire le mouvement correspondant de la liste des mouvements actifs du joueur.
     * Cela arrête instantanément le mouvement associé à cette touche.</p>
     *
     * <p><strong>Importance du relâchement :</strong></p>
     * <p>Le relâchement est crucial pour les mouvements continus (Q et D).
     * Sans cette gestion, le joueur continuerait à se déplacer indéfiniment
     * même après avoir lâché la touche.</p>
     *
     * <p><strong>Comportement par touche :</strong></p>
     * <ul>
     *   <li><strong>Q :</strong> Retire MOVE_LEFT → Arrêt du déplacement gauche</li>
     *   <li><strong>D :</strong> Retire MOVE_RIGHT → Arrêt du déplacement droite</li>
     *   <li><strong>ESPACE :</strong> Retire JUMP → Permet un nouveau saut (après atterrissage)</li>
     * </ul>
     *
     * <p><strong>Note technique :</strong> Si le joueur appuie sur Q puis D sans
     * relâcher Q, les deux mouvements sont actifs. Le mouvement résultant dépend
     * de la logique de résolution dans la classe {@link Player}.</p>
     *
     * @param code Le code de la touche relâchée
     * @see KeyCode
     * @see Player#removePlayerMouvements(PlayerMouvementsEnum)
     * @see PlayerMouvementsEnum
     */
    @Override
    public void handleKeyReleased(KeyCode code) {
        switch (code) {
            case Q -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
            case D -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
            case SPACE -> player.removePlayerMouvements(PlayerMouvementsEnum.JUMP);
        }
    }
}