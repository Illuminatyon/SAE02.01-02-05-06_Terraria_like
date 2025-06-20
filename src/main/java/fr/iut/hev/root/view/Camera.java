package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.AggressiveMob;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.view.actor.PlayerView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;

public class Camera {
    private final Player player;
    private final TilePane landTileMap;
    private final TilePane backgroundTileMap;
    private final Pane globalPane;
    private final PlayerView playerView;
    private final LootView lootView;
    private ArrayList<Actor> aliveActors;

    private final double smoothFactor;

    private double currentCamX = 0;
    private double currentCamY = 0;

    public Camera(Player player, TilePane landTileMap, TilePane backgroundTileMap,
                        Pane globalPane, PlayerView playerView, LootView lootView, double smoothFactor, ArrayList<Actor> aliveActors) {
        this.player = player;
        this.landTileMap = landTileMap;
        this.backgroundTileMap = backgroundTileMap;
        this.globalPane = globalPane;
        this.playerView = playerView;
        this.lootView = lootView;
        this.smoothFactor = smoothFactor;
        this.aliveActors = aliveActors;
    }
    private void updateCameraPosition() {
        if (player == null || landTileMap == null || backgroundTileMap == null) {
            return;
        }

        double screenWidth = globalPane.getWidth();
        double screenHeight = globalPane.getHeight();

        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        double targetCamX = (screenWidth / 2) - playerCenterX;
        double targetCamY = (screenHeight / 2) - playerCenterY;

        landTileMap.setTranslateX(currentCamX);
        landTileMap.setTranslateY(currentCamY);
        backgroundTileMap.setTranslateX(currentCamX);
        backgroundTileMap.setTranslateY(currentCamY);

        for (Actor actor : aliveActors) {
            if (actor == null) {
                continue;
            }

            if (actor.equals(player)) {
                if (playerView != null && playerView.getActorSprite() != null) {
                    playerView.getActorSprite().setLayoutX(actor.getPosX() + currentCamX);
                    playerView.getActorSprite().setLayoutY(actor.getPosY() + currentCamY);
                }
            } else {
                actor.getActorView().getActorSprite().setLayoutX(actor.getPosX()+currentCamX );
                actor.getActorView().getActorSprite().setLayoutX(actor.getPosX()+currentCamY );
            }
        }
        if (lootView != null) {
            lootView.updateLootPositions(currentCamX, currentCamY);
        }

        /*if (pnjView != null && pnjView.getActorSprite() != null && pnjView.getPhrase() != null) {
            double pnjX = pnjView.getActorSprite().getLayoutX();
            double pnjY = pnjView.getActorSprite().getLayoutY();
            pnjView.getPhrase().setLayoutX(pnjX + 30);
            pnjView.getPhrase().setLayoutY(pnjY - 30);
        }*/
    }

    public void update() {
        if (player == null || landTileMap == null || backgroundTileMap == null) {
            return;
        }

        double screenWidth = globalPane.getWidth();
        double screenHeight = globalPane.getHeight();

        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        double targetCamX = (screenWidth / 2) - playerCenterX;
        double targetCamY = (screenHeight / 2) - playerCenterY;

        currentCamX += (targetCamX - currentCamX) * smoothFactor;
        currentCamY += (targetCamY - currentCamY) * smoothFactor;

        landTileMap.setTranslateX(currentCamX);
        landTileMap.setTranslateY(currentCamY);
        backgroundTileMap.setTranslateX(currentCamX);
        backgroundTileMap.setTranslateY(currentCamY);

        if (playerView != null) {
            playerView.getActorSprite().setLayoutX(player.getPosX() + currentCamX);
            playerView.getActorSprite().setLayoutY(player.getPosY() + currentCamY);
        }

        if (lootView != null) {
            lootView.updateLootPositions(currentCamX, currentCamY);
        }
    }

    public double getCurrentCamX() {
        return currentCamX;
    }

    public double getCurrentCamY() {
        return currentCamY;
    }
}
