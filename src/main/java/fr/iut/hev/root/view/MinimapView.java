package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * MinimapView class for displaying a minimap of the game world
 * Shows a scaled-down version of the map with the player's position
 */
public class MinimapView extends StackPane {
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
