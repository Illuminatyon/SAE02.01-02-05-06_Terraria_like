package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Map;

public class CraftView {

    private ListView<RecipesEnum> availableRecipesList;
    private Button craftButton;
    private boolean opened;
    private ObjectProperty<RecipesEnum> selectedRecipeProperty;
    private HBox recipeDisplay;

    public CraftView(ListView<RecipesEnum> recipesView, ObservableList<RecipesEnum> availableRecipes,Button craftButton,HBox recipeDisplay) {
        this.availableRecipesList = recipesView;
        this.opened = false;
        this.craftButton = craftButton;
        this.selectedRecipeProperty = new SimpleObjectProperty<>(null);
        this.recipeDisplay = recipeDisplay;
        initCraftView(availableRecipes);
    }

    public void initCraftView(ObservableList<RecipesEnum> availableRecipes) {
        availableRecipesList.setItems(availableRecipes);
        availableRecipesList.setCellFactory(param -> new ItemFormatCell());
        availableRecipesList.setVisible(false);
        availableRecipesList.setMouseTransparent(true);
        craftButton.setVisible(false);
        craftButton.setMouseTransparent(true);
        recipeDisplay.setVisible(false);
        recipeDisplay.setMouseTransparent(true);

        availableRecipesList.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            RecipesEnum selectedRecipe = availableRecipesList.getSelectionModel().getSelectedItem();
            if (selectedRecipe != null) {
                setSelectedRecipe(selectedRecipe);
                displayRecipe();
                System.out.println("Selection changed to: " + selectedRecipe.name());
            }
        });

        Player.getInstance().getCraftingManager().selectedRecipeProperty().bind(this.selectedRecipeProperty);
        craftButton.setOnAction(actionEvent -> {
            Player.getInstance().getCraftingManager().crafts();
        });
    }

    public void setCraftGUIVisible() {
        opened = !opened;
        updateCraftGUIVisibility();
    }

    public void setCraftGUIVisible(boolean opened) {
        this.opened = opened;
        updateCraftGUIVisibility();
    }

    private void updateCraftGUIVisibility() {
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
        craftButton.setVisible(opened);
        craftButton.setMouseTransparent(!opened);
        recipeDisplay.setVisible(opened);
        recipeDisplay.setMouseTransparent(!opened);
    }

    public void displayRecipe() {
        ImageView recipeIcone,picto;
        Label quantity;
        Pane cell;
        int i = 0;

        recipeDisplay.getChildren().clear();
        recipeDisplay.setPrefWidth((getSelectedRecipe().getIngredients().size() * 2 + 1) * 50);
        recipeDisplay.setLayoutX(960 - (recipeDisplay.getPrefWidth() / 2));

        recipeDisplay.setPrefHeight(50);
        recipeDisplay.setMinHeight(50);

        for (Map.Entry<ItemsEnum, Integer> recipe : getSelectedRecipe().getIngredients().entrySet()) {
            recipeIcone = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/items/" + recipe.getKey().getName() + ".png").toExternalForm()));
            recipeIcone.setFitWidth(50);
            recipeIcone.setFitHeight(50);
            quantity = new Label(Integer.toString(recipe.getValue()));
            cell = new Pane(recipeIcone,quantity);
            recipeDisplay.getChildren().add(cell);

            if (i < getSelectedRecipe().getIngredients().size() - 1)
                picto = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/plus.png").toExternalForm()));
            else
                picto = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/HUD/equal.png").toExternalForm()));

            picto.setFitHeight(50);
            picto.setFitWidth(50);
            recipeDisplay.getChildren().add(picto);
            i++;
        }
        recipeIcone = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/items/" + getSelectedRecipe().getCraftResult().getName() + ".png").toExternalForm()));
        recipeIcone.setFitWidth(50);
        recipeIcone.setFitHeight(50);
        quantity = new Label(Integer.toString(getSelectedRecipe().getItemCraftedQuantity()));
        cell = new Pane(recipeIcone,quantity);
        recipeDisplay.getChildren().add(cell);
    }

    public RecipesEnum getSelectedRecipe() {return this.selectedRecipeProperty.getValue();}
    public void setSelectedRecipe(RecipesEnum recipe) {this.selectedRecipeProperty.setValue(recipe);}
    public ObjectProperty<RecipesEnum> selectedRecipeProperty() {return this.selectedRecipeProperty;}
}
