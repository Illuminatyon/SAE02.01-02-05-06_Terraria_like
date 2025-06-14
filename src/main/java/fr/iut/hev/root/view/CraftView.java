package fr.iut.hev.root.view;

import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CraftView {

    private ListView<RecipesEnum> availableRecipesList;
    private Button craftButton;
    private boolean opened;
    private ObjectProperty<RecipesEnum> selectedRecipeProperty;

    public CraftView(ListView<RecipesEnum> recipesView, ObservableList<RecipesEnum> availableRecipes,Button craftButton) {
        this.availableRecipesList = recipesView;
        this.opened = false;
        this.craftButton = craftButton;
        this.selectedRecipeProperty = new SimpleObjectProperty<>(null);
        initCraftView(availableRecipes);
    }

    public void initCraftView(ObservableList<RecipesEnum> availableRecipes) {
        availableRecipesList.setItems(availableRecipes);
        availableRecipesList.setCellFactory(param -> new ItemFormatCell());
        availableRecipesList.setVisible(false);
        availableRecipesList.setMouseTransparent(true);
        craftButton.setVisible(false);
        craftButton.setMouseTransparent(true);

        availableRecipesList.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            RecipesEnum selectedRecipe = availableRecipesList.getSelectionModel().getSelectedItem();
            if (selectedRecipe != null)
                setSelectedRecipe(selectedRecipe);
        });
    }

    public void setCraftGUIVisible() {
        opened = !opened;
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
        craftButton.setVisible(opened);
        craftButton.setMouseTransparent(!opened);
    }

    public void setCraftGUIVisible(boolean opened) {
        this.opened = opened;
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
        craftButton.setVisible(opened);
        craftButton.setMouseTransparent(!opened);
    }

    public RecipesEnum getSelectedRecipe() {return this.selectedRecipeProperty.getValue();}
    public void setSelectedRecipe(RecipesEnum recipe) {this.selectedRecipeProperty.setValue(recipe);}
    public ObjectProperty<RecipesEnum> selectedRecipeProperty() {return this.selectedRecipeProperty;}
}
