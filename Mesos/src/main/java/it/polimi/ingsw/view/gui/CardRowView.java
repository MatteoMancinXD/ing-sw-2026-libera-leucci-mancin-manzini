package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.snapshots.CardSnapshot;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * CardRowView builds an HBox containing card buttons for either the upper or lower row.
 */
public class CardRowView {

    /**
     * @param cards  list of cards to display
     * @param height display height of each card image
     * @return HBox containing all card buttons
     */
    public static HBox build(List<CardSnapshot> cards, double height, boolean isUpperRow, GuiManager manager, boolean enabled) {
        HBox row = new HBox(cards.size());

        for (CardSnapshot card : cards) {
            Button btn = new Button();
            btn.setDisable(!enabled);
            int id = card.id();
            //in resources, find front folder, in it there are more png images called for ex. front_000
            String imgPath = String.format("/assets/cards/front/front_%03d.png", id-1); //those last 3 digits are the id
            //so %03d means if the id=42 -> front_041.png (0-based indexing for sprites)

            var stream = CardRowView.class.getResourceAsStream(imgPath);
            if (stream != null) {   //if image's path is found then ...
                Image img = new Image(stream); //stream contains imgPath
                ImageView imgView = new ImageView(img); //then creates the image
                imgView.setFitHeight(height);
                imgView.setPreserveRatio(true);
                btn.setGraphic(imgView);
            } else {            //if the image is not found print a button with image's id
                btn.setText("#" + id);
                btn.setPrefSize(100, height);
                btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: white; -fx-border-color: #e0a830;");
            }

            row.getChildren().add(btn);
            int index = cards.indexOf(card);
            btn.setOnAction(e -> {
                try { manager.drawCard(isUpperRow, index); }
                catch (Exception ex) { System.err.println("Errore draw: " + ex.getMessage()); }
            });
        }

        return row;
    }
}