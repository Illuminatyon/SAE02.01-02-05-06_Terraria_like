package fr.iut.hev.root.thread;

import fr.iut.hev.root.controller.InputHandling.MouseInputHandler;
import fr.iut.hev.root.model.enums.TileTypes;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.iut.hev.root.model.TileMap.format;

public class ReleaseClickthread extends Thread {

    private MouseInputHandler mouseInputHandler;
    private volatile MouseEvent mouseEvent;
    private volatile boolean mouseClickIsPressed;
    private boolean clickRelevent;
    private int x;
    private int y;
    public ReleaseClickthread(MouseInputHandler mouseInputHandler,MouseEvent mouseEvent,boolean mouseClickIsPressed) {
        this.mouseInputHandler = mouseInputHandler;
        this.mouseEvent = mouseEvent;
        this.mouseClickIsPressed = mouseClickIsPressed;
        this.clickRelevent = false;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        System.out.println("entered thread");
        if (!(mouseEvent == null))
            checkClickRelevent(mouseEvent.getX(),mouseEvent.getY());
        while (mouseClickIsPressed && clickRelevent) {
            System.out.println("entered while loop");
            x = (int)mouseEvent.getX();
            y = (int)mouseEvent.getY();

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickPressed();
            }
            checkClickRelevent(x,y);
        }
    }

    public void onLeftClickPressed() {
        if (false) { //condition lorsqu'on aura l'inventaire pour vérifier l'objet dans la main

        }
        else if (true) {
            int xModel = x/format,yModel = y/format;
            mouseInputHandler.getPlayer().updateBreaksBlock(xModel,yModel,mouseInputHandler.getTileMap());
            mouseInputHandler.getWorldView().updateTile(xModel,yModel,mouseInputHandler.getTileMap().getTile(xModel,yModel));
        }
    }

    public void checkClickRelevent(double x,double y) {
        this.clickRelevent = (mouseInputHandler.getTileMap().getTile((int)x / format, (int)y / format).getTile().getType() != TileTypes.AIR);
    }
}
