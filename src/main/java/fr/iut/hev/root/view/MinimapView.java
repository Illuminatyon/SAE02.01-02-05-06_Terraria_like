package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * MinimapView class for displaying a minimap of the game world
 * Shows a scaled-down version of the map with the player's position
 */
public class MinimapView extends StackPane {
    // Inner class for representing points in the pathfinding algorithm
    private static class PathPoint {
        int x, y;
        double g; // Cost from start to this point
        double h; // Heuristic (estimated cost from this point to goal)
        PathPoint parent;
        boolean isJumping; // Whether this point was reached by jumping
        boolean isFalling; // Whether this point was reached by falling
        int jumpHeight; // Current jump height (for tracking jump progress)

        PathPoint(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = 0;
            this.h = 0;
            this.parent = null;
            this.isJumping = false;
            this.isFalling = false;
            this.jumpHeight = 0;
        }

        double getF() {
            return g + h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathPoint pathPoint = (PathPoint) o;
            return x == pathPoint.x && y == pathPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final TileMap tileMap;
    private final Player player;
    private static final int MINIMAP_SIZE = 150;
    private static final Color PLAYER_COLOR = Color.RED;
    private static final Color AIR_COLOR = Color.TRANSPARENT;
    private static final Color GRASS_COLOR = Color.GREEN;
    private static final Color DIRT_COLOR = Color.BROWN;
    private static final Color STONE_COLOR = Color.GRAY;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.BLUE;
    private static final Color JUMP_PATH_COLOR = Color.CYAN;
    private static final Color FALL_PATH_COLOR = Color.ORANGE;

    // Player movement constants
    private static final int MAX_JUMP_HEIGHT = 5; // Maximum jump height in tiles
    private static final int MAX_HORIZONTAL_MOVE = 1; // Maximum horizontal movement in tiles

    private List<PathPoint> currentPath = new ArrayList<>();
    private PathPoint targetPoint = null;

    /**
     * Constructor for MinimapView
     * @param tileMap The game's tile map
     * @param player The player entity
     */
    public MinimapView(TileMap tileMap, Player player) {
        this.tileMap = tileMap;
        this.player = player;

        // Create canvas for drawing the minimap
        this.canvas = new Canvas(MINIMAP_SIZE, MINIMAP_SIZE);
        this.gc = canvas.getGraphicsContext2D();

        // Set up the minimap appearance
        this.setMaxSize(MINIMAP_SIZE, MINIMAP_SIZE);
        this.setMinSize(MINIMAP_SIZE, MINIMAP_SIZE);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: black; -fx-border-width: 2;");

        // Add the canvas to the stack pane
        this.getChildren().add(canvas);

        // Initial render
        render();

        // Add listener to player position to update minimap when player moves
        player.posXProperty().addListener((obs, oldVal, newVal) -> render());
        player.posYProperty().addListener((obs, oldVal, newVal) -> render());

        // Add mouse click listener for pathfinding
        canvas.setOnMouseClicked(this::handleMouseClick);
    }

    /**
     * Handle mouse clicks on the minimap
     * @param event The mouse event
     */
    private void handleMouseClick(MouseEvent event) {
        // Get player position in tile coordinates
        int playerTileX = player.getPosX() / TileMap.format + 30; // Add 30 tiles offset to the right
        int playerTileY = (player.getPosY() + player.getHeight() + TileMap.format * 16) / TileMap.format;

        // Calculate scale factors and offsets (same as in render method)
        int visibleTilesX = 15;
        int visibleTilesY = 15;
        float tileSize = (float) MINIMAP_SIZE / (Math.max(visibleTilesX * 2, visibleTilesY * 2));
        float offsetX = MINIMAP_SIZE / 2f - playerTileX * tileSize;
        float offsetY = MINIMAP_SIZE / 2f - playerTileY * tileSize;

        // Convert mouse coordinates to tile coordinates
        double mouseX = event.getX();
        double mouseY = event.getY();
        int targetTileX = (int) ((mouseX - offsetX) / tileSize);
        int targetTileY = (int) ((mouseY - offsetY) / tileSize);

        // Check if the target is within bounds
        if (targetTileX >= 0 && targetTileX < tileMap.getWidth() && 
            targetTileY >= 0 && targetTileY < tileMap.getHeight()) {

            // Set the target point and find the path
            targetPoint = new PathPoint(targetTileX, targetTileY);
            findPath(playerTileX, playerTileY, targetTileX, targetTileY);

            // Render the updated minimap with the path
            render();
        }
    }

    /**
     * Check if a tile is solid (has collision)
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if the tile is solid, false otherwise
     */
    private boolean isSolid(int x, int y) {
        // Check if coordinates are within bounds
        if (x < 0 || x >= tileMap.getWidth() || y < 0 || y >= tileMap.getHeight()) {
            return true; // Out of bounds is considered solid
        }

        // Get the tile and check if it has collision
        Tile tile = tileMap.getTile(x, y);
        return tile != null && tile.getTile().getType().getHasCollision();
    }

    /**
     * Check if a tile has ground beneath it (for standing)
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if there is ground beneath, false otherwise
     */
    private boolean hasGroundBeneath(int x, int y) {
        return isSolid(x, y + 1);
    }

    /**
     * Check if the player can jump from one point to another
     * @param from The starting point
     * @param toX The destination x coordinate
     * @param toY The destination y coordinate
     * @return True if the jump is possible, false otherwise
     */
    private boolean canJump(PathPoint from, int toX, int toY) {
        // Can only jump if standing on ground
        if (!hasGroundBeneath(from.x, from.y) && !from.isJumping) {
            return false;
        }

        // Check if the jump is within the maximum jump height
        int jumpHeight = from.jumpHeight;
        int heightDifference = from.y - toY;

        // If already jumping, check if we've reached max jump height
        if (from.isJumping) {
            if (jumpHeight >= MAX_JUMP_HEIGHT) {
                return false;
            }
            // Can only jump up to MAX_JUMP_HEIGHT tiles
            if (heightDifference > 1) {
                return false;
            }
        } else {
            // Starting a new jump
            if (heightDifference > 1) {
                return false;
            }
        }

        // Check horizontal distance (can only move MAX_HORIZONTAL_MOVE tiles horizontally while jumping)
        int horizontalDistance = Math.abs(from.x - toX);
        if (horizontalDistance > MAX_HORIZONTAL_MOVE) {
            return false;
        }

        // Check if the destination is not solid
        return !isSolid(toX, toY);
    }

    /**
     * Check if the player can fall from one point to another
     * @param from The starting point
     * @param toX The destination x coordinate
     * @param toY The destination y coordinate
     * @return True if the fall is possible, false otherwise
     */
    private boolean canFall(PathPoint from, int toX, int toY) {
        // Can only fall if there's no ground beneath
        if (hasGroundBeneath(from.x, from.y)) {
            return false;
        }

        // Can only fall downward
        if (toY <= from.y) {
            return false;
        }

        // Check horizontal distance (can only move MAX_HORIZONTAL_MOVE tiles horizontally while falling)
        int horizontalDistance = Math.abs(from.x - toX);
        if (horizontalDistance > MAX_HORIZONTAL_MOVE) {
            return false;
        }

        // Check if the destination is not solid
        return !isSolid(toX, toY);
    }

    /**
     * Check if the player can walk from one point to another
     * @param from The starting point
     * @param toX The destination x coordinate
     * @param toY The destination y coordinate
     * @return True if the walk is possible, false otherwise
     */
    private boolean canWalk(PathPoint from, int toX, int toY) {
        // Can only walk horizontally
        if (toY != from.y) {
            return false;
        }

        // Can only walk if there's ground beneath both points
        if (!hasGroundBeneath(from.x, from.y) || !hasGroundBeneath(toX, toY)) {
            return false;
        }

        // Check horizontal distance (can only move MAX_HORIZONTAL_MOVE tiles horizontally)
        int horizontalDistance = Math.abs(from.x - toX);
        if (horizontalDistance > MAX_HORIZONTAL_MOVE) {
            return false;
        }

        // Check if the destination is not solid
        return !isSolid(toX, toY);
    }

    /**
     * Find the optimal path from start to goal using A* algorithm
     * Enhanced to consider player movement capabilities and gravity
     * @param startX The starting X coordinate
     * @param startY The starting Y coordinate
     * @param goalX The goal X coordinate
     * @param goalY The goal Y coordinate
     */
    private void findPath(int startX, int startY, int goalX, int goalY) {
        // Clear the current path
        currentPath.clear();

        // Create start and goal points
        PathPoint start = new PathPoint(startX, startY);
        PathPoint goal = new PathPoint(goalX, goalY);

        // Initialize open and closed sets
        PriorityQueue<PathPoint> openSet = new PriorityQueue<>(Comparator.comparingDouble(PathPoint::getF));
        Set<PathPoint> closedSet = new HashSet<>();

        // Add start point to open set
        start.g = 0;
        start.h = calculateHeuristic(start, goal);
        openSet.add(start);

        // A* algorithm
        while (!openSet.isEmpty()) {
            // Get the point with the lowest f score
            PathPoint current = openSet.poll();

            // If we've reached the goal, reconstruct the path
            if (current.x == goal.x && current.y == goal.y) {
                reconstructPath(current);
                return;
            }

            // Add current point to closed set
            closedSet.add(current);

            // Try walking (horizontal movement)
            for (int dx = -MAX_HORIZONTAL_MOVE; dx <= MAX_HORIZONTAL_MOVE; dx++) {
                if (dx == 0) continue; // Skip staying in place

                int nx = current.x + dx;
                int ny = current.y;

                if (canWalk(current, nx, ny)) {
                    processNeighbor(current, nx, ny, openSet, closedSet, goal, false, false, 0);
                }
            }

            // Try jumping (upward movement)
            for (int dx = -MAX_HORIZONTAL_MOVE; dx <= MAX_HORIZONTAL_MOVE; dx++) {
                // Jump up one tile
                int dy = -1;

                int nx = current.x + dx;
                int ny = current.y + dy;

                if (canJump(current, nx, ny)) {
                    int newJumpHeight = current.isJumping ? current.jumpHeight + 1 : 1;
                    processNeighbor(current, nx, ny, openSet, closedSet, goal, true, false, newJumpHeight);
                }
            }

            // Try falling (downward movement due to gravity)
            for (int dx = -MAX_HORIZONTAL_MOVE; dx <= MAX_HORIZONTAL_MOVE; dx++) {
                int nx = current.x + dx;
                int ny = current.y + 1;

                if (canFall(current, nx, ny)) {
                    processNeighbor(current, nx, ny, openSet, closedSet, goal, false, true, 0);
                }
            }
        }

        // If we get here, there's no path to the goal
        currentPath.clear();
    }

    /**
     * Process a neighbor point in the A* algorithm
     */
    private void processNeighbor(PathPoint current, int nx, int ny, PriorityQueue<PathPoint> openSet, 
                                Set<PathPoint> closedSet, PathPoint goal, boolean isJumping, boolean isFalling, int jumpHeight) {
        // Create a new point for the neighbor
        PathPoint neighbor = new PathPoint(nx, ny);
        neighbor.isJumping = isJumping;
        neighbor.isFalling = isFalling;
        neighbor.jumpHeight = jumpHeight;

        // Skip if the neighbor is in the closed set
        if (closedSet.contains(neighbor)) {
            return;
        }

        // Calculate the tentative g score (cost to reach this neighbor)
        double moveCost = 1.0;
        if (isJumping) moveCost = 1.5; // Jumping costs more
        if (isFalling) moveCost = 0.5; // Falling costs less (gravity helps)

        double tentativeG = current.g + moveCost;

        // Check if the neighbor is in the open set
        boolean inOpenSet = false;
        for (PathPoint p : openSet) {
            if (p.equals(neighbor)) {
                inOpenSet = true;
                neighbor = p;
                break;
            }
        }

        if (!inOpenSet || tentativeG < neighbor.g) {
            // Update the neighbor's scores
            neighbor.parent = current;
            neighbor.g = tentativeG;
            neighbor.h = calculateHeuristic(neighbor, goal);
            neighbor.isJumping = isJumping;
            neighbor.isFalling = isFalling;
            neighbor.jumpHeight = jumpHeight;

            // Add the neighbor to the open set if it's not already there
            if (!inOpenSet) {
                openSet.add(neighbor);
            }
        }
    }

    /**
     * Calculate the heuristic (estimated cost) from a point to the goal
     * @param point The point to calculate the heuristic for
     * @param goal The goal point
     * @return The heuristic value
     */
    private double calculateHeuristic(PathPoint point, PathPoint goal) {
        // Use Euclidean distance as the heuristic
        return Math.sqrt(Math.pow(point.x - goal.x, 2) + Math.pow(point.y - goal.y, 2));
    }

    /**
     * Reconstruct the path from the goal to the start
     * @param goal The goal point
     */
    private void reconstructPath(PathPoint goal) {
        currentPath.clear();

        PathPoint current = goal;
        while (current != null) {
            currentPath.add(current);
            current = current.parent;
        }

        // Reverse the path so it goes from start to goal
        Collections.reverse(currentPath);
    }

    /**
     * Render the minimap
     * Shows a portion of the map centered around the player
     */
    public void render() {
        // Clear the canvas
        gc.clearRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        // Get player position in tile coordinates
        int playerTileX = player.getPosX() / TileMap.format + 30; // Add 30 tiles offset to the right
        // Adjust Y position to account for player height (feet position) and add additional offset (16 tiles)
        int playerTileY = (player.getPosY() + player.getHeight() + TileMap.format * 16) / TileMap.format;

        // Define the visible area around the player (in tiles)
        int visibleTilesX = 15; // Number of tiles visible horizontally from center
        int visibleTilesY = 15; // Number of tiles visible vertically from center

        // Calculate scale factors for the visible area
        float tileSize = (float) MINIMAP_SIZE / (Math.max(visibleTilesX * 2, visibleTilesY * 2));

        // Calculate the offset to center the player
        float offsetX = MINIMAP_SIZE / 2f - playerTileX * tileSize;
        float offsetY = MINIMAP_SIZE / 2f - playerTileY * tileSize;

        // Draw the map (only the visible area around the player)
        int startX = Math.max(0, playerTileX - visibleTilesX);
        int endX = Math.min(tileMap.getWidth(), playerTileX + visibleTilesX);
        int startY = Math.max(0, playerTileY - visibleTilesY);
        int endY = Math.min(tileMap.getHeight(), playerTileY + visibleTilesY);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                Tile tile = tileMap.getTile(x, y);
                if (tile != null) {
                    // Set color based on tile type
                    Color tileColor = getTileColor(tile);

                    // Skip drawing air tiles
                    if (tileColor != AIR_COLOR) {
                        gc.setFill(tileColor);

                        // Calculate position and size on minimap
                        double miniX = x * tileSize + offsetX;
                        double miniY = y * tileSize + offsetY;

                        // Draw the tile
                        gc.fillRect(miniX, miniY, tileSize, tileSize);
                    }
                }
            }
        }

        // Draw the path if it exists
        if (!currentPath.isEmpty()) {
            double pathPointSize = Math.max(2, tileSize / 2);

            for (PathPoint point : currentPath) {
                // Calculate position on minimap
                double miniX = point.x * tileSize + offsetX + (tileSize - pathPointSize) / 2;
                double miniY = point.y * tileSize + offsetY + (tileSize - pathPointSize) / 2;

                // Set color based on movement type
                if (point.isJumping) {
                    gc.setFill(JUMP_PATH_COLOR);
                } else if (point.isFalling) {
                    gc.setFill(FALL_PATH_COLOR);
                } else {
                    gc.setFill(PATH_COLOR);
                }

                // Draw the path point
                gc.fillOval(miniX, miniY, pathPointSize, pathPointSize);
            }
        }

        // Draw the player at the correct position on the minimap
        gc.setFill(PLAYER_COLOR);
        double playerSize = 6;

        // Calculate the player's position on the minimap
        double playerMiniX = playerTileX * tileSize + offsetX - playerSize/2;
        double playerMiniY = playerTileY * tileSize + offsetY - playerSize/2;

        gc.fillOval(playerMiniX, playerMiniY, playerSize, playerSize);
    }

    /**
     * Get the color for a tile based on its type
     * @param tile The tile to get the color for
     * @return The color for the tile
     */
    private Color getTileColor(Tile tile) {
        if (tile.getTile().getType() == TileTypes.AIR) {
            return AIR_COLOR;
        } else {
            // Display all block types in green
            return GRASS_COLOR;
        }
    }
}
