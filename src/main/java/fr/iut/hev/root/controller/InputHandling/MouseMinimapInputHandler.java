package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.view.MinimapView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Gestionnaire des événements souris pour la minimap
 * Convertit les clics sur la minimap en coordonnées de la carte et trouve des chemins
 */
public class MouseMinimapInputHandler implements EventHandler<MouseEvent> {
    private final MinimapView minimapView;
    private final Player player;
    private final TileMap tileMap;

    /**
     * Constructeur pour MouseMinimapInputHandler
     * @param minimapView La vue de la minimap
     * @param player Le joueur pour obtenir sa position
     * @param tileMap La carte du jeu pour vérifier les limites
     */
    public MouseMinimapInputHandler(MinimapView minimapView, Player player, TileMap tileMap) {
        this.minimapView = minimapView;
        this.player = player;
        this.tileMap = tileMap;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // Vérifier que c'est un clic (pressed et released)
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
            // Ne traiter le clic que si la minimap est agrandie (mode Tab)
            if (minimapView.isEnlarged()) {
                System.out.println("Clic sur la minimap en mode agrandi (Tab)");
                handleMinimapClick(mouseEvent);
            } else {
                System.out.println("Clic sur la minimap ignoré - appuyez sur Tab pour activer le GPS");
            }
        }
    }

    /**
     * Gère un clic sur la minimap
     * @param mouseEvent L'événement de clic de souris
     */
    private void handleMinimapClick(MouseEvent mouseEvent) {
        // Obtenir la position du joueur en tuiles en utilisant la méthode de MinimapView
        int[] playerTilePos = minimapView.getPlayerTilePosition();
        int playerTileX = playerTilePos[0];
        int playerTileY = playerTilePos[1];

        // Utiliser la méthode existante pour convertir les coordonnées
        int[] targetCoords = minimapView.convertClickToWorldCoordinates(mouseEvent.getX(), mouseEvent.getY());
        int targetTileX = targetCoords[0];
        int targetTileY = targetCoords[1];

        // Vérifier si la cible est dans les limites
        if (minimapView.isValidPosition(targetTileX, targetTileY)) {
            // Vérifier si la position cible est un obstacle
            if (minimapView.isSolid(targetTileX, targetTileY)) {
                // Trouver une position accessible à proximité
                int[] adjustedTarget = findAccessiblePositionNear(targetTileX, targetTileY, playerTileX, playerTileY);
                targetTileX = adjustedTarget[0];
                targetTileY = adjustedTarget[1];
            }

            // Trouver un chemin vers la position cible
            minimapView.findPath(playerTileX, playerTileY, targetTileX, targetTileY);
            minimapView.render();

            /*
            // Mettre à jour le chemin du joueur
            if (player instanceof Player) {
                ((Player) player).setPath(minimapView.getCurrentPath());
            }
             */
        } else {
            System.out.println("Cible hors limites: (" + targetTileX + ", " + targetTileY + ")");
        }
    }

    private int[] findAccessiblePositionNear(int x, int y, int playerX, int playerY) {
        // Rechercher une position accessible à proximité
        int range = 3;
        for (int dy = -range; dy <= range; dy++) {
            for (int dx = -range; dx <= range; dx++) {
                if (dx == 0 && dy == 0) continue;
                int newX = x + dx;
                int newY = y + dy;
                if (minimapView.isValidPosition(newX, newY) && !minimapView.isSolid(newX, newY)) {
                    return new int[]{newX, newY};
                }
            }
        }
        // Si aucune position accessible n'est trouvée, retourner la position originale
        return new int[]{x, y};
    }
}
