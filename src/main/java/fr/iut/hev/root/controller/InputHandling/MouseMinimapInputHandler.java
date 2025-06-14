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
            handleMinimapClick(mouseEvent);
        }
    }

    /**
     * Gère un clic sur la minimap
     * @param mouseEvent L'événement de clic de souris
     */
    private void handleMinimapClick(MouseEvent mouseEvent) {
        // Obtenez la position du joueur en coordonnées de tuiles
        int playerTileX = (int) (player.getPosX() / TileMap.format) + 30; // Ajoutez un décalage de 30 tuiles vers la droite
        int playerTileY = (int) ((player.getPosY() + player.getHeight()) / TileMap.format) + 16; // Ajoutez un décalage vertical

        // Calculez la taille des tuiles et les décalages
        int visibleTilesX = 15;
        int visibleTilesY = 15;
        float tileSize = (float) MinimapView.MINIMAP_SIZE / (Math.max(visibleTilesX * 2, visibleTilesY * 2));
        float offsetX = MinimapView.MINIMAP_SIZE / 2f - (playerTileX - visibleTilesX / 2) * tileSize;
        float offsetY = MinimapView.MINIMAP_SIZE / 2f - (playerTileY - visibleTilesY / 2) * tileSize;

        // Convertissez les coordonnées de la souris en coordonnées de tuiles
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        int targetTileX = (int) ((mouseX - offsetX) / tileSize);
        int targetTileY = (int) ((mouseY - offsetY) / tileSize);

        // Vérifiez si la cible est dans les limites
        if (targetTileX >= 0 && targetTileX < tileMap.getWidth() &&
                targetTileY >= 0 && targetTileY < tileMap.getHeight()) {
            // Définissez le point cible et trouvez le chemin
            minimapView.findPath(playerTileX, playerTileY, targetTileX, targetTileY);

            // Rendu de la minimap mise à jour avec le chemin
            minimapView.render();

            // Ici, vous pourriez aussi envoyer un événement ou un signal pour que le joueur se déplace automatiquement
            // suivant le chemin trouvé, si c'est ce que vous souhaitez
        } else {
            System.out.println("Cible hors limites: (" + targetTileX + ", " + targetTileY + ")");
        }
    }
}
