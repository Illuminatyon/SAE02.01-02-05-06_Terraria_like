package fr.iut.hev.root.view;

import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ItemFormatCell extends ListCell<RecipesEnum> {

    @Override
    protected void updateItem(RecipesEnum recipesEnum, boolean empty) {
        super.updateItem(recipesEnum, empty);

        setText(null);
        if (empty || recipesEnum == null) {
            setGraphic(null);
        }
        else {
            try {
                // Create the image view for the item
                String path = "/fr/iut/hev/root/img/items/" + recipesEnum.getCraftResult().getName() + ".png";
                ImageView itemImage = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
                itemImage.setFitWidth(60);
                itemImage.setFitHeight(60);

                // Create a label for the item name
                Label nameLabel = new Label(recipesEnum.getCraftResult().getName());
                nameLabel.setTextFill(Color.WHITE);
                nameLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
                nameLabel.setTranslateY(25);

                // Create a stack pane to hold the image and label
                StackPane content = new StackPane(itemImage, nameLabel);
                content.setMinSize(70, 70);
                content.setPrefSize(70, 70);

                // Create a pane to hold everything with proper sizing
                Pane cellPane = new Pane();
                cellPane.setMinSize(70, 70);
                cellPane.setPrefSize(70, 70);

                // Add a background rectangle to make the cell more visible
                Rectangle background = new Rectangle(0, 0, 70, 70);
                background.setFill(Paint.valueOf("#00000000")); // Fully transparent
                background.setArcWidth(0);
                background.setArcHeight(0);

                // Add a highlight effect for the selected state
                Rectangle highlight = new Rectangle(0, 0, 70, 70);
                highlight.setFill(Paint.valueOf("#4d4dff80")); // Semi-transparent blue
                highlight.setArcWidth(0);
                highlight.setArcHeight(0);
                highlight.setVisible(false);

                cellPane.getChildren().addAll(background, highlight, content);

                // Make sure the cell is not mouse transparent
                cellPane.setMouseTransparent(false);
                content.setMouseTransparent(false);
                itemImage.setMouseTransparent(false);
                background.setMouseTransparent(false);
                highlight.setMouseTransparent(false);
                setMouseTransparent(false);

                // Add a click handler to the cell pane
                cellPane.setOnMouseClicked(event -> {
                    getListView().getSelectionModel().select(getIndex());
                    System.out.println("Clicked on recipe: " + recipesEnum.name() + " at index " + getIndex());
                    event.consume();
                });

                // Add hover effects
                cellPane.setOnMouseEntered(event -> {
                    highlight.setVisible(true);
                });

                cellPane.setOnMouseExited(event -> {
                    if (!isSelected()) {
                        highlight.setVisible(false);
                    }
                });

                // Update highlight based on selection state
                selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    highlight.setVisible(isNowSelected);
                });

                setGraphic(cellPane);
            } catch (Exception e) {
                System.err.println("Error creating cell for recipe: " + recipesEnum.name());
                e.printStackTrace();
                setGraphic(null);
            }
        }
    }
}
