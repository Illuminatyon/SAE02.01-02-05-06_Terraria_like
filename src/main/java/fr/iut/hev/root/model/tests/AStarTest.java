package fr.iut.hev.root.model.tests;

import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.pathfinding.AStar;
import fr.iut.hev.root.model.pathfinding.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe AStar
 * Ces tests vérifient le bon fonctionnement de l'algorithme de pathfinding A*
 */
public class AStarTest {

    private TileMap tileMap;
    private AStar aStar;
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;

    /**
     * Configuration initiale avant chaque test
     * Crée une carte de test et initialise l'algorithme A*
     */
    @BeforeEach
    public void setUp() throws IOException {
        // Création d'une carte de test
        HitboxManager hitboxManager = new HitboxManager();
        ItemFactory itemFactory = new ItemFactory(hitboxManager);
        tileMap = new TileMap(MAP_WIDTH * TileMap.format, MAP_HEIGHT * TileMap.format, itemFactory);

        // Réinitialisation de la carte avec des tuiles d'air (passables)
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                tileMap.addTile(new Tile(TilesEnum.AIR, x, y));
            }
        }

        // Initialisation de l'algorithme A*
        aStar = new AStar(tileMap);
    }

    /**
     * Teste le chemin en ligne droite horizontale
     */
    @Test
    @DisplayName("Test de chemin en ligne droite horizontale")
    public void testStraightHorizontalPath() {
        // Définition des points de départ et d'arrivée
        int startX = 1;
        int startY = 5;
        int targetX = 8;
        int targetY = 5;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Vérification que le chemin existe
        assertNotNull(path, "Le chemin ne devrait pas être null");
        assertFalse(path.isEmpty(), "Le chemin ne devrait pas être vide");

        // Vérification que le chemin commence par le point adjacent au départ et se termine par la cible
        assertEquals(new Point(2, 5), path.get(0), "Le premier point du chemin devrait être (2, 5)");
        assertEquals(new Point(targetX, targetY), path.get(path.size() - 1), "Le dernier point du chemin devrait être la cible");

        // Vérification que le chemin est de la bonne longueur (7 points pour aller de 1,5 à 8,5)
        assertEquals(7, path.size(), "Le chemin devrait contenir 7 points");
    }

    /**
     * Teste le chemin en ligne droite verticale
     */
    @Test
    @DisplayName("Test de chemin en ligne droite verticale")
    public void testStraightVerticalPath() {
        // Définition des points de départ et d'arrivée
        int startX = 5;
        int startY = 1;
        int targetX = 5;
        int targetY = 8;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Vérification que le chemin existe
        assertNotNull(path, "Le chemin ne devrait pas être null");
        assertFalse(path.isEmpty(), "Le chemin ne devrait pas être vide");

        // Vérification que le chemin commence par le point adjacent au départ et se termine par la cible
        assertEquals(new Point(5, 2), path.get(0), "Le premier point du chemin devrait être (5, 2)");
        assertEquals(new Point(targetX, targetY), path.get(path.size() - 1), "Le dernier point du chemin devrait être la cible");

        // Vérification que le chemin est de la bonne longueur (7 points pour aller de 5,1 à 5,8)
        assertEquals(7, path.size(), "Le chemin devrait contenir 7 points");
    }

    /**
     * Teste le chemin en diagonale
     */
    @Test
    @DisplayName("Test de chemin en diagonale")
    public void testDiagonalPath() {
        // Définition des points de départ et d'arrivée
        int startX = 1;
        int startY = 1;
        int targetX = 8;
        int targetY = 8;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Vérification que le chemin existe
        assertNotNull(path, "Le chemin ne devrait pas être null");
        assertFalse(path.isEmpty(), "Le chemin ne devrait pas être vide");

        // Vérification que le chemin se termine par la cible
        assertEquals(new Point(targetX, targetY), path.get(path.size() - 1), "Le dernier point du chemin devrait être la cible");

        // Vérification que le chemin est optimal (devrait utiliser des diagonales)
        assertTrue(path.size() <= 8, "Le chemin devrait être optimal (utiliser des diagonales)");
    }

    /**
     * Teste le contournement d'obstacles
     */
    @Test
    @DisplayName("Test de contournement d'obstacles")
    public void testPathAroundObstacles() {
        // Création d'un mur d'obstacles
        for (int y = 3; y < 7; y++) {
            tileMap.addTile(new Tile(TilesEnum.STONE, 5, y));
        }

        // Définition des points de départ et d'arrivée (de part et d'autre du mur)
        int startX = 3;
        int startY = 5;
        int targetX = 7;
        int targetY = 5;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Debug output
        System.out.println("[DEBUG_LOG] Path size: " + path.size());
        System.out.println("[DEBUG_LOG] Path points:");
        for (Point point : path) {
            System.out.println("[DEBUG_LOG] Point: (" + point.x + ", " + point.y + ")");
        }

        // Vérification que le chemin existe
        assertNotNull(path, "Le chemin ne devrait pas être null");
        assertFalse(path.isEmpty(), "Le chemin ne devrait pas être vide");

        // Vérification que le chemin se termine par la cible
        assertEquals(new Point(targetX, targetY), path.get(path.size() - 1), "Le dernier point du chemin devrait être la cible");

        // Vérification que le chemin contourne l'obstacle (doit être au moins de longueur 4)
        assertTrue(path.size() >= 4, "Le chemin devrait contourner l'obstacle");

        // Vérification qu'aucun point du chemin ne passe par l'obstacle
        for (Point point : path) {
            assertFalse(point.x == 5 && point.y >= 3 && point.y < 7,
                    "Le chemin ne devrait pas passer à travers l'obstacle");
        }
    }

    /**
     * Teste le cas où aucun chemin n'est possible
     */
    @Test
    @DisplayName("Test de chemin impossible")
    public void testImpossiblePath() {
        // Création d'un mur complet qui bloque le passage
        for (int y = 0; y < MAP_HEIGHT; y++) {
            tileMap.addTile(new Tile(TilesEnum.STONE, 5, y));
        }

        // Définition des points de départ et d'arrivée (de part et d'autre du mur)
        int startX = 3;
        int startY = 5;
        int targetX = 7;
        int targetY = 5;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Vérification que le chemin est vide (impossible)
        assertTrue(path.isEmpty(), "Le chemin devrait être vide car impossible");
    }

    /**
     * Teste le cas où le départ et l'arrivée sont identiques
     */
    @Test
    @DisplayName("Test de chemin avec départ et arrivée identiques")
    public void testSameStartAndTarget() {
        // Définition des points de départ et d'arrivée identiques
        int x = 5;
        int y = 5;

        // Recherche du chemin
        List<Point> path = aStar.findPath(x, y, x, y);

        // Vérification que le chemin est vide (déjà à destination)
        assertTrue(path.isEmpty(), "Le chemin devrait être vide car déjà à destination");
    }

    /**
     * Teste la validation des positions
     */
    @Test
    @DisplayName("Test de validation des positions")
    public void testPositionValidation() {
        // Test de positions valides
        assertTrue(aStar.isValidPosition(5, 5), "La position (5, 5) devrait être valide");

        // Test de positions hors limites
        assertFalse(aStar.isValidPosition(-1, 5), "La position (-1, 5) devrait être invalide");
        assertFalse(aStar.isValidPosition(5, -1), "La position (5, -1) devrait être invalide");
        assertFalse(aStar.isValidPosition(MAP_WIDTH, 5), "La position (MAP_WIDTH, 5) devrait être invalide");
        assertFalse(aStar.isValidPosition(5, MAP_HEIGHT), "La position (5, MAP_HEIGHT) devrait être invalide");

        // Test avec un obstacle
        tileMap.addTile(new Tile(TilesEnum.STONE, 3, 3));
        assertFalse(aStar.isValidPosition(3, 3), "La position (3, 3) devrait être invalide car occupée par un obstacle");
    }

    /**
     * Teste le saut par-dessus un obstacle bas
     */
    @Test
    @DisplayName("Test de saut par-dessus un obstacle bas")
    public void testJumpOverLowObstacle() {
        // Création d'un obstacle bas (1 tuile de haut)
        tileMap.addTile(new Tile(TilesEnum.STONE, 5, 5));

        // Définition des points de départ et d'arrivée
        int startX = 3;
        int startY = 5;
        int targetX = 7;
        int targetY = 5;

        // Recherche du chemin
        List<Point> path = aStar.findPath(startX, startY, targetX, targetY);

        // Vérification que le chemin existe
        assertNotNull(path, "Le chemin ne devrait pas être null");
        assertFalse(path.isEmpty(), "Le chemin ne devrait pas être vide");

        // Vérification que le chemin se termine par la cible
        assertEquals(new Point(targetX, targetY), path.get(path.size() - 1), "Le dernier point du chemin devrait être la cible");

        // Le chemin devrait soit contourner l'obstacle, soit sauter par-dessus
        boolean pathGoesAroundOrJumps = true;
        for (Point point : path) {
            if (point.x == 5 && point.y == 5) {
                pathGoesAroundOrJumps = false;
                break;
            }
        }
        assertTrue(pathGoesAroundOrJumps, "Le chemin devrait contourner l'obstacle ou sauter par-dessus");
    }
}
