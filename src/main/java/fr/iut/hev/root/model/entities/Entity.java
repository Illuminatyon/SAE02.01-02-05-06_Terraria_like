package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.physics.Collider;
import fr.iut.hev.root.model.physics.Gravity;
import fr.iut.hev.root.model.land.TileMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * <h2>Classe de base pour toutes les entités du jeu</h2>
 *
 * <p>Cette classe représente une entité physique dans le monde du jeu.
 * Elle gère la position, les dimensions, le mouvement et les collisions.
 * Toutes les entités du jeu (joueur, mobs, items, projectiles) héritent
 * de cette classe de base.</p>
 *
 * <p><strong>Caractéristiques principales :</strong></p>
 * <ul>
 *   <li><strong>Position :</strong> Coordonnées X et Y dans le monde</li>
 *   <li><strong>Dimensions :</strong> Largeur et hauteur de l'entité</li>
 *   <li><strong>Vélocité :</strong> Vitesse de déplacement en X et Y</li>
 *   <li><strong>Physique :</strong> Gravité et détection de collisions</li>
 * </ul>
 *
 * <p><strong>Système de coordonnées :</strong></p>
 * <ul>
 *   <li>X : Horizontal (positif → droite)</li>
 *   <li>Y : Vertical (positif → bas, car origine en haut à gauche)</li>
 * </ul>
 *
 * <p><strong>Pattern utilisé :</strong> Template Method - Les sous-classes
 * peuvent personnaliser le comportement tout en réutilisant la logique commune.</p>
 *
 * @author Équipe de développement
 * @version 1.0
 * @see Actor
 * @see Player
 * @see Loot
 * @see Collider
 * @see Gravity
 * @since 1.0
 */
public class Entity {
    /**
     * Property de la position horizontale (X) de l'entité.
     * Utilise IntegerProperty pour le binding avec l'UI.
     */
    private IntegerProperty posXProperty;

    /**
     * Property de la position verticale (Y) de l'entité.
     * Utilise IntegerProperty pour le binding avec l'UI.
     */
    private IntegerProperty posYProperty;

    /**
     * Largeur de l'entité en pixels.
     */
    private int width;

    /**
     * Hauteur de l'entité en pixels.
     */
    private int height; // TODO : passer height et width dans collider (vérifier les conséquences dans player)

    /**
     * Vitesse horizontale de l'entité (pixels par frame).
     * Positif = droite, négatif = gauche.
     */
    private int velocityX;

    /**
     * Vitesse verticale de l'entité (pixels par frame).
     * Positif = bas, négatif = haut.
     */
    private int velocityY;

    /**
     * Référence à la carte de tuiles pour les collisions.
     */
    private TileMap tileMap;

    /**
     * Gestionnaire de collisions pour cette entité.
     */
    private Collider collider;
    // TODO : Potentiellement faire d'autres classes pour les attributs, afin d'alléger le constructeur et même la classe
    // TODO : en général

    /**
     * Constructeur complet d'une entité.
     *
     * <p>Initialise une entité avec une position et des dimensions spécifiques.
     * Le collider est automatiquement créé et configuré.</p>
     *
     * @param posX Position initiale en X (pixels)
     * @param posY Position initiale en Y (pixels)
     * @param width Largeur de l'entité (pixels)
     * @param height Hauteur de l'entité (pixels)
     */
    public Entity(int posX, int posY, int width, int height) {
        this.posXProperty = new SimpleIntegerProperty(posX);
        this.posYProperty = new SimpleIntegerProperty(posY);
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.tileMap = TileMap.getInstance();
        this.collider = new Collider(tileMap, this);
    }

    /**
     * Constructeur par défaut.
     *
     * <p>Crée une entité sans initialisation. À utiliser avec précaution,
     * l'entité doit être initialisée manuellement après création.</p>
     */
    public Entity() {

    }

    /**
     * <h3>Mise à jour de la position</h3>
     *
     * <p>Applique la gravité puis déplace l'entité selon ses vélocités.
     * Cette méthode devrait être appelée à chaque frame de la game loop.</p>
     *
     * <p><strong>Ordre d'exécution :</strong></p>
     * <ol>
     *   <li>Application de la gravité (modifie velocityY)</li>
     *   <li>Déplacement horizontal (posX += velocityX)</li>
     *   <li>Déplacement vertical (posY += velocityY)</li>
     * </ol>
     *
     * @see #applyGravity()
     */
    public void updatePosition() {
        applyGravity();
        posXProperty.set(posXProperty.getValue() + velocityX);
        posYProperty.set(posYProperty.getValue() + velocityY);
    }

    /**
     * <h3>Application de la gravité</h3>
     *
     * <p>Applique la force de gravité à l'entité si elle n'est pas au sol.
     * La gravité augmente progressivement la vélocité verticale (vers le bas)
     * jusqu'à ce qu'une collision avec le sol soit détectée.</p>
     *
     * <p><strong>Comportement :</strong></p>
     * <ul>
     *   <li><strong>En l'air :</strong> velocityY += gravityForce (accélération)</li>
     *   <li><strong>Au sol :</strong> velocityY = 0 (arrêt de la chute)</li>
     * </ul>
     *
     * @see Gravity#getGravityForce()
     * @see Collider#hasCollisionBottom(int)
     */
    public void applyGravity() {
        if (!this.collider.hasCollisionBottom(this.velocityY + 1)) {
            //if (super.getVelocityY() < maxVelocityY)
            velocityY += Gravity.getGravityForce();
        } else {
            velocityY = 0;
        } // TODO : peut être le refactor ? (enlever les if)
    }

    // ==================== GETTERS / SETTERS ====================

    /**
     * @return La position X actuelle de l'entité
     */
    public final int getPosX() {
        return this.posXProperty.getValue();
    }

    /**
     * @param newPosX La nouvelle position X
     */
    public final void setPosX(double newPosX) {
        posXProperty.setValue(newPosX);
    }

    /**
     * @return La property observable de la position X
     */
    public final IntegerProperty posXProperty() {
        return this.posXProperty;
    }

    /**
     * @return La position Y actuelle de l'entité
     */
    public final int getPosY() {
        return this.posYProperty.getValue();
    }

    /**
     * @param newPosY La nouvelle position Y
     */
    public final void setPosY(double newPosY) {
        this.posYProperty.setValue(newPosY);
    }

    /**
     * @return La property observable de la position Y
     */
    public final IntegerProperty posYProperty() {
        return this.posYProperty;
    }

    /**
     * @return La largeur de l'entité en pixels
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return La hauteur de l'entité en pixels
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @param width La nouvelle largeur en pixels
     */
    public void setWidth(int width) {this.width = width;}

    /**
     * @param height La nouvelle hauteur en pixels
     */
    public void setHeight(int height) {this.height = height;}

    /**
     * @return La vélocité horizontale actuelle (pixels/frame)
     */
    public int getVelocityX() {
        return this.velocityX;
    }

    /**
     * @param velocity La nouvelle vélocité horizontale
     */
    public void setVelocityX(int velocity) {
        this.velocityX = velocity;
    }

    /**
     * @return La vélocité verticale actuelle (pixels/frame)
     */
    public int getVelocityY() {
        return this.velocityY;
    }

    /**
     * @param velocity La nouvelle vélocité verticale
     */
    public void setVelocityY(int velocity) {
        this.velocityY = velocity;
    }

    /**
     * @return Le gestionnaire de collisions de cette entité
     */
    public Collider getCollider() {
        return this.collider;
    }
}