package fr.iut.hev.root.view;

import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;

public class CraftView {

    private ListView availableRecipesList;
    private boolean opened;

    public CraftView(ListView recipesView, ObservableList<RecipesEnum> availableRecipes) {
        this.availableRecipesList = recipesView;
        this.opened = false;
        initCraftView(availableRecipes);
    }

    public void initCraftView(ObservableList<RecipesEnum> availableRecipes) {
        availableRecipesList.setItems(availableRecipes);
        availableRecipesList.setCellFactory(param -> new ItemFormatCell());
        availableRecipesList.setVisible(false);
        availableRecipesList.setPadding(new Insets(5,5,5,5));
    }

    public void setCraftGUIVisible() {
        opened = !opened;
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
    }

    public void setCraftGUIVisible(boolean opened) {
        this.opened = opened;
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
    }
}
