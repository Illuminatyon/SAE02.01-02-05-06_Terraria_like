package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.TileTypes;
import fr.iut.hev.root.view.GlobalView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.iut.hev.root.model.TileMap.format;

public class MouseInputHandler implements EventHandler<MouseEvent> {

    private TileMap tileMap;
    private GlobalView worldView;
    private Player player;
    private int x;
    private int y;
    private boolean mouseClickIsPressed;
    private boolean mouseClickIsReleased;
    private MouseEvent mouseEvent;

    public MouseInputHandler(TileMap tileMap,GlobalView worldView,Player player) {
        this.tileMap = tileMap;
        this.worldView = worldView;
        this.player = player;
        this.mouseClickIsPressed = false;
        this.mouseClickIsReleased = false;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        x = (int)mouseEvent.getX() / format;
        y = (int)mouseEvent.getY() / format;
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            this.mouseClickIsPressed = true;
            this.mouseEvent = mouseEvent;
        }
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            mouseClickIsPressed = false;
            mouseClickIsReleased = true;
        }
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
            this.mouseEvent = mouseEvent;
        }
    }

    public void clickPressedHandler() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickPressed();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickPressed();
        }
    }

    public void clickReleasedHandler() {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            onLeftClickReleased();
        }
        else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            onRightClickReleased();
        }
    }

    public void onLeftClickPressed() {
        if (false) { //condition lorsqu'on aura l'inventaire pour vérifier l'objet dans la main

        }
        else if (checkInReach() && checkTileNotEmpty() /*&& () ici condition pout vérif s'il y a un block entre le joueur et le block visé*/) {
            System.out.println("in reach");
            player.updateBreaksBlock(x,y,tileMap);
            worldView.updateTile(x,y,tileMap.getTile(x,y));
        }
    }

    public void onRightClickPressed() {
        System.out.println("right click triggered");
    }

    public void onLeftClickReleased() {
        if (checkTileNotEmpty()) {
            tileMap.getTile(x,y).resetHealth();
            worldView.updateTile(x,y,tileMap.getTile(x,y));
        }
        mouseClickIsReleased = false;
    }

    public void onRightClickReleased() {
        System.out.println("right click released");
    }

    public boolean checkTileNotEmpty() {
        return tileMap.getTile(x,y).getTile().getType() != TileTypes.AIR;
    }

    public boolean getMouseClickIsPressed() {return this.mouseClickIsPressed;}

    public boolean getMouseClickIsReleased() {return this.mouseClickIsReleased;}

    public boolean checkInReach() {
        System.out.println((int)mouseEvent.getX());
        System.out.println((player.getPosX() + (tileMap.getWidth() * TileMap.format) / 2 + player.getWidth() / 2));
        System.out.println((int)mouseEvent.getY());
        System.out.println(player.getPosY() + (tileMap.getHeight() * TileMap.format) / 2 + player.getHeight() / 2);
        System.out.println(((int)mouseEvent.getX() - (player.getPosX() + (tileMap.getWidth() * TileMap.format) / 2 + player.getWidth() / 2))*2 + ((int)mouseEvent.getY() - (player.getPosY() + (tileMap.getHeight() * TileMap.format) / 2 + player.getHeight() / 2))*2);
        return (((int)mouseEvent.getX() - player.getPosX())*2 + ((int)mouseEvent.getY() - player.getPosY())*2) <= player.getReach()*32;
    }

    /*public boolean checkIfBlockOnTheWay() {
        int xB = x - (x/32)*32 + 16,yB = y - (y/32)*32 + 16;

        while ()
    }*/
    //début de truc pour checker si il y a un block entre le joueur et le bloc qu'il veut casser
}
