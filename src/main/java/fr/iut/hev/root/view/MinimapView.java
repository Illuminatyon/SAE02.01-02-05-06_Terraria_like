package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.Tiles;
import fr.iut.hev.root.model.pathfinder.Pathfinder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class MinimapView extends StackPane {
    public final Canvas canvas;
    private final GraphicsContext gc;
    private final TileMap tileMap;
    private final Player player;
    private final Pathfinder pathfinder;
    private List<Pathfinder.PathPoint> currentPath = new ArrayList<>();
    private int playerReach;

    public static final int MINIMAP_SIZE = 150;
    private static final Color PLAYER_COLOR = Color.RED;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.BLUE;
    private static final Color JUMP_PATH_COLOR = Color.CYAN;
    private static final Color FALL_PATH_COLOR = Color.ORANGE;
    private static final int MAX_JUMP_HEIGHT = 5;
    private static final int MAX_HORIZONTAL_MOVE = 1;

    public MinimapView(TileMap tileMap, Player player) {
        this.tileMap = tileMap;
        this.player = player;
        this.playerReach = player.getReach();
        this.pathfinder = new Pathfinder(tileMap, MAX_JUMP_HEIGHT, MAX_HORIZONTAL_MOVE);

        this.canvas = new Canvas(MINIMAP_SIZE, MINIMAP_SIZE);
        this.gc = canvas.getGraphicsContext2D();

        // Set up the minimap appearance
        this.setMaxSize(MINIMAP_SIZE, MINIMAP_SIZE);
        this.setMinSize(MINIMAP_SIZE, MINIMAP_SIZE);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: white; -fx-border-width: 1;");
        this.getChildren().add(canvas);

        // Initial debug and render
        debugTileMap();
        render();

        // Add listeners to update minimap when player moves
        player.posXProperty().addListener((obs, oldVal, newVal) -> render());
        player.posYProperty().addListener((obs, oldVal, newVal) -> render());

        // Setup mouse click handler
        setupMouseHandlers();
    }

    private void setupMouseHandlers() {
        this.canvas.setOnMouseClicked(event -> {
            int[] targetCoords = convertClickToWorldCoordinates(event.getX(), event.getY());
            int targetTileX = targetCoords[0];
            int targetTileY = targetCoords[1];

            int playerTileX = (int)(player.getPosX() / TileMap.format);
            int playerTileY = (int)((player.getPosY() + player.getHeight()) / TileMap.format);

            System.out.println("Position du joueur : (" + playerTileX + ", " + playerTileY + ")");
            System.out.println("Position cible : (" + targetTileX + ", " + targetTileY + ")");

            // Vérifier que les positions sont valides
            if (!isValidPosition(playerTileX, playerTileY)) {
                System.out.println("Position du joueur invalide !");
                return;
            }
            if (!isValidPosition(targetTileX, targetTileY)) {
                System.out.println("Position cible invalide !");
                return;
            }

            // Limiter la distance en fonction de la portée du joueur
            double distance = Math.sqrt(Math.pow(targetTileX - playerTileX, 2) +
                    Math.pow(targetTileY - playerTileY, 2));

            if (distance > playerReach) {
                double angle = Math.atan2(targetTileY - playerTileY, targetTileX - playerTileX);
                targetTileX = playerTileX + (int)(playerReach * Math.cos(angle));
                targetTileY = playerTileY + (int)(playerReach * Math.sin(angle));

                // Vérifier que la position ajustée est valide
                targetTileX = Math.max(0, Math.min(tileMap.getWidth() - 1, targetTileX));
                targetTileY = Math.max(0, Math.min(tileMap.getHeight() - 1, targetTileY));
            }

            // Trouver un chemin vers la position cible
            findPath(playerTileX, playerTileY, targetTileX, targetTileY);
        });
    }

    /**
     * Méthode pour vérifier et corriger les positions si nécessaire
     */
    private int[] getValidPlayerTilePosition() {
        double playerWorldX = player.getPosX();
        double playerWorldY = player.getPosY();

        // Calculer la position en tuiles
        int playerTileX = (int)Math.floor(playerWorldX / TileMap.format);
        int playerTileY = (int)Math.floor((playerWorldY + player.getHeight()) / TileMap.format);

        // Vérifier et corriger si nécessaire
        if (!isValidPosition(playerTileX, playerTileY)) {
            System.out.println("Position du joueur hors limites, corrigeons...");
            // Trouver la position valide la plus proche
            playerTileX = Math.max(0, Math.min(tileMap.getWidth() - 1, playerTileX));
            playerTileY = Math.max(0, Math.min(tileMap.getHeight() - 1, playerTileY));
            System.out.println("Position corrigée: (" + playerTileX + ", " + playerTileY + ")");
        }

        return new int[]{playerTileX, playerTileY};
    }

    /**
     * Méthode pour obtenir une position de cible valide
     */
    private int[] getValidTargetPosition(int[] clickedCoords) {
        int targetX = clickedCoords[0];
        int targetY = clickedCoords[1];

        // Vérifier et corriger si nécessaire
        if (!isValidPosition(targetX, targetY)) {
            System.out.println("Position cible hors limites, corrigeons...");
            targetX = Math.max(0, Math.min(tileMap.getWidth() - 1, targetX));
            targetY = Math.max(0, Math.min(tileMap.getHeight() - 1, targetY));
            System.out.println("Position cible corrigée: (" + targetX + ", " + targetY + ")");
        }

        // Si la position est un obstacle, trouver une position accessible à proximité
        if (isSolid(targetX, targetY)) {
            System.out.println("Position cible est un obstacle, recherche d'une alternative...");
            // Rechercher une position accessible à proximité
            int range = 3;
            for (int dy = -range; dy <= range; dy++) {
                for (int dx = -range; dx <= range; dx++) {
                    if (dx == 0 && dy == 0) continue;
                    int newX = targetX + dx;
                    int newY = targetY + dy;
                    if (isValidPosition(newX, newY) && !isSolid(newX, newY)) {
                        System.out.println("Position alternative trouvée: (" + newX + ", " + newY + ")");
                        return new int[]{newX, newY};
                    }
                }
            }
            // Si aucune position accessible n'est trouvée, garder la position originale (même si solide)
            System.out.println("Aucune position alternative accessible trouvée");
        }

        return new int[]{targetX, targetY};
    }

    /**
     * Méthode de gestion des clics sur la minimap, version corrigée
     */
    private void handleMinimapClick(double mouseX, double mouseY) {
        try {
            // Obtenir la position du joueur en tuiles
            int[] playerTilePos = getValidPlayerTilePosition();
            int playerTileX = playerTilePos[0];
            int playerTileY = playerTilePos[1];

            // Convertir le clic en coordonnées de tuiles
            int[] clickedCoords = convertClickToWorldCoordinates(mouseX, mouseY);
            int[] targetPos = getValidTargetPosition(clickedCoords);

            System.out.println("\n=== NOUVELLE DESTINATION ===");
            System.out.println("Position joueur (tuiles): (" + playerTileX + ", " + playerTileY + ")");
            System.out.println("Position cible (tuiles): (" + targetPos[0] + ", " + targetPos[1] + ")");

            // Calculer la distance
            double distance = Math.sqrt(Math.pow(targetPos[0] - playerTileX, 2) +
                    Math.pow(targetPos[1] - playerTileY, 2));

            // Appliquer la portée si nécessaire
            if (distance > playerReach) {
                double angle = Math.atan2(targetPos[1] - playerTileY, targetPos[0] - playerTileX);
                targetPos[0] = playerTileX + (int)(playerReach * Math.cos(angle));
                targetPos[1] = playerTileY + (int)(playerReach * Math.sin(angle));

                // S'assurer que la position est toujours valide
                targetPos = getValidTargetPosition(targetPos);
            }

            // Trouver le chemin
            System.out.println("Recherche de chemin...");
            findPath(playerTileX, playerTileY, targetPos[0], targetPos[1]);

        } catch (Exception e) {
            System.err.println("Erreur dans handleMinimapClick: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode corrigée pour convertir les clics en coordonnées monde
     */
    private int[] convertClickToWorldCoordinates(double mouseX, double mouseY) {
        // Obtenir la position du joueur en tuiles
        int[] playerTilePos = getValidPlayerTilePosition();
        int playerTileX = playerTilePos[0];
        int playerTileY = playerTilePos[1];

        // Calculer la zone visible
        int visibleTiles = 10;
        int startX = Math.max(0, playerTileX - visibleTiles);
        int endX = Math.min(tileMap.getWidth(), playerTileX + visibleTiles + 1);
        int startY = Math.max(0, playerTileY - visibleTiles);
        int endY = Math.min(tileMap.getHeight(), playerTileY + visibleTiles + 1);

        int visibleWidth = Math.max(1, endX - startX);
        int visibleHeight = Math.max(1, endY - startY);

        // Éviter la division par zéro
        if (visibleWidth == 0) visibleWidth = 1;
        if (visibleHeight == 0) visibleHeight = 1;

        float tileSizeX = (float)MINIMAP_SIZE / visibleWidth;
        float tileSizeY = (float)MINIMAP_SIZE / visibleHeight;
        float tileSize = Math.min(tileSizeX, tileSizeY);

        // Calculer les offsets
        float offsetX = MINIMAP_SIZE / 2f - (playerTileX - startX) * tileSize;
        float offsetY = MINIMAP_SIZE / 2f - (playerTileY - startY) * tileSize;

        // Calculer la position cliquée en coordonnées de tuiles
        int clickedTileX = startX + (int)((mouseX - offsetX) / tileSize);
        int clickedTileY = startY + (int)((mouseY - offsetY) / tileSize);

        // S'assurer que les coordonnées sont dans les limites
        clickedTileX = Math.max(0, Math.min(tileMap.getWidth() - 1, clickedTileX));
        clickedTileY = Math.max(0, Math.min(tileMap.getHeight() - 1, clickedTileY));

        System.out.println("Converti clic (" + mouseX + "," + mouseY + ") en tuile (" +
                clickedTileX + "," + clickedTileY + ")");

        return new int[]{clickedTileX, clickedTileY};
    }
    public Canvas getCanvas() {
        return canvas;
    }

    public void debugTileMap() {
        System.out.println("\n=== DEBUG TILEMAP ===");
        System.out.println("TileMap dimensions: " + tileMap.getWidth() + "x" + tileMap.getHeight());

        int playerTileX = (int)(player.getPosX() / TileMap.format);
        int playerTileY = (int)((player.getPosY() + player.getHeight()) / TileMap.format);
        System.out.println("Player tile position: (" + playerTileX + ", " + playerTileY + ")");

        int radius = 5;
        System.out.println("Tiles around player:");
        for (int y = playerTileY - radius; y <= playerTileY + radius; y++) {
            for (int x = playerTileX - radius; x <= playerTileX + radius; x++) {
                if (x >= 0 && x < tileMap.getWidth() && y >= 0 && y < tileMap.getHeight()) {
                    Tile tile = tileMap.getTile(x, y);
                    String tileStr = "null";
                    boolean solid = false;

                    if (tile != null) {
                        try {
                            Object tileObj = tile.getTile();
                            tileStr = tileObj != null ? tileObj.toString() : "null";
                            if (tileObj instanceof Tiles) {
                                Tiles tileType = (Tiles) tileObj;
                                solid = tileType.getType().getHasCollision();
                            }
                        } catch (Exception e) {
                            tileStr = "error: " + e.getMessage();
                        }
                    }
                    System.out.printf("(%d,%d): %s %s | ", x, y, tileStr, solid ? "SOLID" : "OPEN");
                } else {
                    System.out.print("(OOB) | ");
                }
            }
            System.out.println();
        }
        System.out.println("=== END DEBUG ===\n");

        // Afficher la carte complète pour débogage
        debugPrintFullMap();
    }

    private void debugPrintFullMap() {
        System.out.println("\n=== FULL MAP DEBUG ===");
        for (int y = 0; y < tileMap.getHeight(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < tileMap.getWidth(); x++) {
                if (x == (int)(player.getPosX() / TileMap.format) &&
                        y == (int)((player.getPosY() + player.getHeight()) / TileMap.format)) {
                    line.append("P "); // Position du joueur
                    continue;
                }

                Tile tile = tileMap.getTile(x, y);
                if (tile == null) {
                    line.append(". "); // Vide
                    continue;
                }

                try {
                    Object tileObj = tile.getTile();
                    if (tileObj instanceof Tiles) {
                        Tiles tileType = (Tiles) tileObj;
                        boolean isSolid = tileType.getType().getHasCollision();
                        line.append(isSolid ? "X " : ". ");
                    }
                } catch (Exception e) {
                    line.append("E ");
                }
            }
            System.out.println(line.toString());
        }
        System.out.println("=== END FULL MAP ===\n");
    }

    private boolean isValidPosition(int x, int y) {
        boolean valid = x >= 0 && x < tileMap.getWidth() && y >= 0 && y < tileMap.getHeight();
        if (!valid) {
            System.out.println("Position invalide : (" + x + ", " + y +
                    ") hors limites de la carte (" +
                    tileMap.getWidth() + "x" + tileMap.getHeight() + ")");
        }
        return valid;
    }

    private boolean isSolid(int x, int y) {
        if (!isValidPosition(x, y)) {
            return true; // Hors limites considéré comme solide
        }

        Tile tile = tileMap.getTile(x, y);
        if (tile == null) return false;

        try {
            Object tileObj = tile.getTile();
            if (tileObj instanceof Tiles) {
                Tiles tileType = (Tiles) tileObj;
                return tileType.getType().getHasCollision();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de la tuile à (" + x + ", " + y + "): " + e.getMessage());
        }
        return true;
    }

    public void findPath(int startX, int startY, int goalX, int goalY) {
        int width = tileMap.getWidth();
        int height = tileMap.getHeight();

        if (!isValidPosition(startX, startY)) {
            System.err.println("ERREUR: Position de départ invalide!");
            return;
        }

        if (!isValidPosition(goalX, goalY)) {
            System.err.println("ERREUR: Position d'arrivée invalide!");
            return;
        }

        System.out.println("\n=== DEBUT RECHERCHE CHEMIN ===");
        System.out.println("Recherche de chemin de (" + startX + "," + startY + ") à (" + goalX + "," + goalY + ")");

        if (isSolid(goalX, goalY)) {
            System.out.println("Position d'arrivée est un obstacle!");
            findNearestAccessiblePosition(startX, startY, goalX, goalY);
            return;
        }

        // Rechercher le chemin
        List<Pathfinder.PathPoint> path = pathfinder.findPath(startX, startY, goalX, goalY);

        if (!path.isEmpty()) {
            currentPath = path;
            if (player instanceof Player) {
                ((Player) player).setPath(path);
            }
            System.out.println("Chemin trouvé avec " + path.size() + " points");
            for (Pathfinder.PathPoint p : path) {
                System.out.println("  -> (" + p.x + ", " + p.y +
                        (p.isJumping ? " [saut]" : "") +
                        (p.isFalling ? " [chute]" : ""));
            }
        } else {
            System.out.println("Aucun chemin trouvé vers la destination");
            findNearestAccessiblePosition(startX, startY, goalX, goalY);
        }

        render();
    }

    private void findNearestAccessiblePosition(int startX, int startY, int goalX, int goalY) {
        System.out.println("Recherche d'une position accessible près de (" + goalX + ", " + goalY + ")");
        int range = 10; // Augmentez la portée de recherche
        for (int dy = -range; dy <= range; dy++) {
            for (int dx = -range; dx <= range; dx++) {
                if (dx == 0 && dy == 0) continue; // Ignorer la position d'origine
                int newX = goalX + dx;
                int newY = goalY + dy;

                if (isValidPosition(newX, newY) && !isSolid(newX, newY)) {
                    List<Pathfinder.PathPoint> path = pathfinder.findPath(startX, startY, newX, newY);
                    if (!path.isEmpty()) {
                        currentPath = path;
                        if (player instanceof Player) {
                            ((Player) player).setPath(path);
                        }
                        System.out.println("Chemin alternatif trouvé vers (" + newX + ", " + newY + ")");
                        System.out.println("Distance à l'objectif original: " +
                                Math.sqrt(Math.pow(newX - goalX, 2) + Math.pow(newY - goalY, 2)));
                        return;
                    }
                }
            }
        }
        System.out.println("Aucune position accessible trouvée à proximité");
    }



    public void render() {
        // Clear with transparent background
        gc.clearRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        // Draw background
        gc.setFill(Color.rgb(20, 20, 20, 0.8));
        gc.fillRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        // Get player position in tile coordinates
        int playerTileX = (int)(player.getPosX() / TileMap.format);
        int playerTileY = (int)((player.getPosY() + player.getHeight()) / TileMap.format);

        // Define visible area
        int visibleTiles = 10;
        int startX = Math.max(0, playerTileX - visibleTiles);
        int endX = Math.min(tileMap.getWidth(), playerTileX + visibleTiles + 1);
        int startY = Math.max(0, playerTileY - visibleTiles);
        int endY = Math.min(tileMap.getHeight(), playerTileY + visibleTiles + 1);

        // Calculate tile size
        int visibleWidth = Math.max(1, endX - startX);
        int visibleHeight = Math.max(1, endY - startY);
        float tileSizeX = (float)MINIMAP_SIZE / visibleWidth;
        float tileSizeY = (float)MINIMAP_SIZE / visibleHeight;
        float tileSize = Math.min(tileSizeX, tileSizeY);

        // Calculate offsets
        float offsetX = MINIMAP_SIZE / 2f - (playerTileX - startX) * tileSize;
        float offsetY = MINIMAP_SIZE / 2f - (playerTileY - startY) * tileSize;

        // Draw visible area background
        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(offsetX, offsetY, visibleWidth * tileSize, visibleHeight * tileSize);

        // Draw tiles
        int tilesDrawn = 0;
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Tile tile = tileMap.getTile(x, y);
                if (tile != null) {
                    try {
                        Object tileObj = tile.getTile();
                        if (tileObj instanceof Tiles) {
                            Tiles tileType = (Tiles) tileObj;
                            if (tileType != Tiles.AIR) {
                                Color tileColor = getTileColor(tile);
                                double miniX = (x - startX) * tileSize + offsetX;
                                double miniY = (y - startY) * tileSize + offsetY;

                                // Draw tile with border
                                gc.setFill(tileColor);
                                gc.fillRect(miniX, miniY, tileSize, tileSize);

                                // Draw border for visibility
                                gc.setStroke(Color.rgb(50, 50, 50));
                                gc.setLineWidth(0.2);
                                gc.strokeRect(miniX, miniY, tileSize, tileSize);
                                tilesDrawn++;
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error rendering tile at (" + x + "," + y + "): " + e.getMessage());
                    }
                }
            }
        }

        // Draw path if exists
        if (!currentPath.isEmpty()) {
            double pathPointSize = Math.max(2, tileSize / 2);
            for (Pathfinder.PathPoint point : currentPath) {
                if (point.x >= startX && point.x < endX && point.y >= startY && point.y < endY) {
                    double miniX = (point.x - startX) * tileSize + offsetX + (tileSize - pathPointSize) / 2;
                    double miniY = (point.y - startY) * tileSize + offsetY + (tileSize - pathPointSize) / 2;

                    // Use different colors based on movement type
                    Color pathColor = PATH_COLOR;
                    if (point.isJumping) {
                        pathColor = JUMP_PATH_COLOR;
                    } else if (point.isFalling) {
                        pathColor = FALL_PATH_COLOR;
                    }
                    gc.setFill(pathColor);
                    gc.fillOval(miniX, miniY, pathPointSize, pathPointSize);
                }
            }
        }

        // Draw player position
        gc.setFill(PLAYER_COLOR);
        double playerSize = Math.max(8, tileSize / 2);
        double playerMiniX = (playerTileX - startX) * tileSize + offsetX - playerSize/2;
        double playerMiniY = (playerTileY - startY) * tileSize + offsetY - playerSize/2;
        gc.fillOval(playerMiniX, playerMiniY, playerSize, playerSize);

        // Draw border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        // Debug info
        System.out.println("Minimap rendered:");
        System.out.println("  Player at: (" + playerTileX + ", " + playerTileY + ")");
        System.out.println("  Visible area: (" + startX + ", " + startY + ") to (" + (endX-1) + ", " + (endY-1) + ")");
        System.out.println("  Tiles drawn: " + tilesDrawn);
        if (!currentPath.isEmpty()) {
            System.out.println("  Path length: " + currentPath.size() + " points");
        }
    }

    private Color getTileColor(Tile tile) {
        if (tile == null) return Color.TRANSPARENT;

        try {
            Object tileObj = tile.getTile();
            if (tileObj instanceof Tiles) {
                Tiles tileType = (Tiles) tileObj;
                switch (tileType) {
                    case GRASS: return Color.rgb(34, 139, 34);
                    case DIRT: return Color.rgb(139, 69, 19);
                    case STONE: return Color.rgb(128, 128, 128);
                    case CRAFTING_TABLE: return Color.rgb(139, 69, 19);
                    case TREE: return Color.rgb(0, 100, 0);
                    case FURNACE: return Color.rgb(192, 0, 0);
                    case IRON_ORE: return Color.rgb(192, 192, 192);
                    default: return Color.LIGHTGRAY;
                }
            }
            return Color.LIGHTGRAY;
        } catch (Exception e) {
            System.err.println("Error getting tile color: " + e.getMessage());
            return Color.LIGHTGRAY;
        }
    }

    public void printMap() {
        System.out.println("\n=== FULL MAP VIEW ===");
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                if (x == (int)(player.getPosX() / TileMap.format) &&
                        y == (int)((player.getPosY() + player.getHeight()) / TileMap.format)) {
                    System.out.print("P ");
                    continue;
                }

                Tile tile = tileMap.getTile(x, y);
                if (tile == null) {
                    System.out.print(". ");
                    continue;
                }

                try {
                    Object tileObj = tile.getTile();
                    if (tileObj instanceof Tiles) {
                        Tiles tileType = (Tiles) tileObj;
                        if (isSolid(x, y)) {
                            System.out.print("X ");
                        } else {
                            System.out.print(". ");
                        }
                    }
                } catch (Exception e) {
                    System.out.print("E ");
                }
            }
            System.out.println();
        }
        System.out.println("=== END MAP VIEW ===\n");
    }
}
