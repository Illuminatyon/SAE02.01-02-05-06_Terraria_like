package fr.iut.hev.root.view;

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
        // Configure the ListView
        availableRecipesList.setItems(availableRecipes);
        availableRecipesList.setCellFactory(param -> new ItemFormatCell());
        availableRecipesList.setVisible(false);
        availableRecipesList.setMouseTransparent(true);

        // Configure the selection model
        //availableRecipesList.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);

        // Configure the other UI elements
        craftButton.setVisible(false);
        craftButton.setMouseTransparent(true);
        recipeDisplay.setVisible(false);
        recipeDisplay.setMouseTransparent(true);

        // Ensure the ListView has proper focus and selection behavior
        //availableRecipesList.setFocusTraversable(true);

        // Add explicit click handler to ensure selection works
        /*availableRecipesList.setOnMouseClicked(event -> {
            RecipesEnum selectedRecipe = availableRecipesList.getSelectionModel().getSelectedItem();
            if (selectedRecipe != null) {
                setSelectedRecipe(selectedRecipe);
                displayRecipe();
                System.out.println("Selected recipe: " + selectedRecipe.name());
            }
        });*/

        // Add selection change listener
        availableRecipesList.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            RecipesEnum selectedRecipe = availableRecipesList.getSelectionModel().getSelectedItem();
            if (selectedRecipe != null) {
                setSelectedRecipe(selectedRecipe);
                displayRecipe();
                System.out.println("Selection changed to: " + selectedRecipe.name());
            }
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
        // Update visibility and mouse transparency
        availableRecipesList.setVisible(opened);
        availableRecipesList.setMouseTransparent(!opened);
        craftButton.setVisible(opened);
        craftButton.setMouseTransparent(!opened);
        recipeDisplay.setVisible(opened);
        recipeDisplay.setMouseTransparent(!opened);

        // Ensure the ListView gets focus and is properly configured when shown
        /*if (opened) {
            // Reset the selection model to ensure it's properly initialized
            availableRecipesList.getSelectionModel().clearSelection();

            // Make sure the ListView is in front of other elements
            availableRecipesList.toFront();

            // Request focus to ensure keyboard navigation works
            availableRecipesList.requestFocus();

            System.out.println("Craft GUI opened with " + availableRecipesList.getItems().size() + " recipes");
            for (RecipesEnum recipe : availableRecipesList.getItems()) {
                System.out.println(" - " + recipe.name());
            }
        }*/
    }

    public void displayRecipe() {
        ImageView recipeIcone,picto;
        Label quantity;
        Pane cell;
        int i = 0;

        recipeDisplay.getChildren().clear();
        recipeDisplay.setPrefWidth((getSelectedRecipe().getIngredients().size() * 2 + 1) * 50);
        // Center the recipe display based on the parent width (700 is half of the scene width from Main.java)
        recipeDisplay.setLayoutX(700 - (recipeDisplay.getPrefWidth() / 2));

        // Ensure the recipe display has enough height for the content
        recipeDisplay.setPrefHeight(50);
        recipeDisplay.setMinHeight(50);

        for (Map.Entry<ItemsEnum, Integer> recipe : getSelectedRecipe().getIngredients().entrySet()) {
            recipeIcone = new ImageView(new Image(getClass().getResource("/fr/iut/hev/root/img/items/" + recipe.getKey().getName() + ".png").toExternalForm()));
            recipeIcone.setFitWidth(50);
            recipeIcone.setFitHeight(50);
            quantity = new Label(Integer.toString(recipe.getValue()));
            quantity.setTextFill(Color.WHITE);
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
        quantity.setTextFill(Color.WHITE);
        cell = new Pane(recipeIcone,quantity);
        recipeDisplay.getChildren().add(cell);
    }

    public RecipesEnum getSelectedRecipe() {return this.selectedRecipeProperty.getValue();}
    public void setSelectedRecipe(RecipesEnum recipe) {this.selectedRecipeProperty.setValue(recipe);}
    public ObjectProperty<RecipesEnum> selectedRecipeProperty() {return this.selectedRecipeProperty;}
}
