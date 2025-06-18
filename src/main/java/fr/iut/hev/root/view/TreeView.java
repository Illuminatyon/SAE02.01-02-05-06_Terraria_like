package fr.iut.hev.root.view;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.Tree;
import fr.iut.hev.root.model.TreeManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class TreeView {

    private Pane forestView;

public TreeView(Pane forestView,ArrayList<Tree> trees) {
        this.forestView = forestView;
        initTreeView(trees);
    }

    public void initTreeView(ArrayList<Tree> trees) {
        ImageView treeTexture;
        for (Tree tree : trees) {
            treeTexture = new ImageView(getTreeTexture(tree));
            treeTexture.setLayoutX(tree.getX());
            treeTexture.setLayoutY(tree.getY());
            forestView.getChildren().add(treeTexture);
        }
    }

    public Image getTreeTexture(Tree tree) {
        return new Image(getClass().getResource("/fr/iut/hev/root/img/tile/tree_" + tree.getTaille() + ".png").toExternalForm());
    }

    public void deleteSprite(int x,int y) {
        forestView.getChildren().removeIf(node -> node.getLayoutX() == x && node.getLayoutY() == y);
    }
}
