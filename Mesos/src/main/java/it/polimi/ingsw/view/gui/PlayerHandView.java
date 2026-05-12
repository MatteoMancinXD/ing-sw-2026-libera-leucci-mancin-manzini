package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerHandView — bottom bar showing all cards owned by the local player.
 * Character cards and building cards are shown with their images.
 */
public class PlayerHandView {

    private static final double CARD_HEIGHT = 100;

    /**
     * Builds the bottom bar with all cards of the local player.
     * @param players   full list of players
     * @param localNick nickname of the local player
     * @return ScrollPane ready to be set as gameRoot.setBottom()
     */
    public static ScrollPane build(List<Player> players, String localNick) {
        HBox cardStrip = new HBox(8);
        cardStrip.setPadding(new Insets(10, 15, 10, 15));
        cardStrip.setStyle("-fx-background-color: #12122a;");

        // trova il giocatore locale
        Player localPlayer = players.stream()
                .filter(p -> p.getNickname().equals(localNick))
                .findFirst()
                .orElse(null);

        if (localPlayer == null) {
            ScrollPane empty = new ScrollPane(cardStrip);
            empty.setPrefHeight(CARD_HEIGHT + 30);
            return empty;
        }
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(localPlayer.getArtists());
        allCards.addAll(localPlayer.getBuilders());
        allCards.addAll(localPlayer.getHarvesters());
        allCards.addAll(localPlayer.getHunters());
        allCards.addAll(localPlayer.getShamans());
        allCards.addAll(localPlayer.getInventors());
        allCards.addAll(localPlayer.getBuildings());

        if (allCards.isEmpty()) {
            Label empty = new Label("Nessuna carta in mano");
            empty.setTextFill(Color.web("#666688"));
            empty.setPadding(new Insets(10));
            cardStrip.getChildren().add(empty);
        } else {
            for (Card card : allCards) {
                cardStrip.getChildren().add(buildCardNode(card));
            }
        }


        VBox wrapper = new VBox(4);
        wrapper.setStyle("-fx-background-color: #12122a;");
        Label title = new Label("Le tue carte:");
        title.setTextFill(Color.web("#e0a830"));
        title.setPadding(new Insets(4, 0, 0, 15));
        wrapper.getChildren().addAll(title, cardStrip);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);
        scroll.setPrefHeight(CARD_HEIGHT + 50);
        scroll.setMaxHeight(CARD_HEIGHT + 50);
        scroll.setStyle("-fx-background: #12122a; -fx-background-color: #12122a; -fx-border-color: #e0a830; -fx-border-width: 1 0 0 0;");

        return scroll;
    }

    private static VBox buildCardNode(Card card) {
        VBox node = new VBox(4);
        node.setStyle("-fx-alignment: center;");

        int id = card.getId();
        String imgPath = String.format("/assets/cards/front/front_%03d.png", id - 1);

        var stream = PlayerHandView.class.getResourceAsStream(imgPath);
        if (stream != null) {
            Image img = new Image(stream);
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(CARD_HEIGHT);
            imgView.setPreserveRatio(true);



            node.getChildren().add(imgView);
        }

        return node;
    }
}