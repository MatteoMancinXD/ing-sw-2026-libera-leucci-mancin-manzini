package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerHandView — bottom section showing all cards owned by each player.
 * Local player is shown first, then opponents.
 * Each player gets one horizontal scrollable row.
 */
public class PlayerHandView {

    private static final double CARD_HEIGHT = 90;

    /**
     * Builds the bottom section with one card row per player.
     * @param players   full list of players
     * @param localNick nickname of the local player (shown first)
     * @return VBox ready to be set as gameRoot.setBottom()
     */
    public static VBox build(List<Player> players, String localNick) {
        VBox allRows = new VBox(0);
        allRows.setStyle("-fx-background-color: #12122a; -fx-border-color: #e0a830; -fx-border-width: 1 0 0 0;");

        List<Player> ordered = new ArrayList<>();
        players.stream().filter(p -> p.getNickname().equals(localNick)).findFirst().ifPresent(ordered::add);
        players.stream().filter(p -> !p.getNickname().equals(localNick)).forEach(ordered::add);

        for (Player p : ordered) {
            allRows.getChildren().add(buildPlayerRow(p, localNick));
        }

        return allRows;
    }

    private static HBox buildPlayerRow(Player player, String localNick) {
        HBox row = new HBox(0);
        row.setStyle("-fx-background-color: #12122a;");

        // intestazione con nickname
        boolean isLocal = player.getNickname().equals(localNick);
        String labelText = isLocal
                ? "▶ " + player.getNickname() + " (tu)"
                : "  " + player.getNickname();

        Label nameLabel = new Label(labelText);
        nameLabel.setTextFill(isLocal ? Color.web("#e0a830") : Color.web("#aaaaaa"));
        nameLabel.setPrefWidth(120);
        nameLabel.setMinWidth(120);
        nameLabel.setPadding(new Insets(5, 8, 5, 8));
        nameLabel.setWrapText(false);
        nameLabel.setStyle("-fx-background-color: #1a1a2e; -fx-font-weight: " + (isLocal ? "bold" : "normal") + ";");

        // raccoglie tutte le carte del giocatore
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(player.getArtists());
        allCards.addAll(player.getBuilders());
        allCards.addAll(player.getHarvesters());
        allCards.addAll(player.getHunters());
        allCards.addAll(player.getShamans());
        allCards.addAll(player.getInventors());
        allCards.addAll(player.getBuildings());

        // striscia carte scrollabile
        HBox cardStrip = new HBox(4);
        cardStrip.setPadding(new Insets(4, 8, 4, 8));
        cardStrip.setStyle("-fx-background-color: #12122a;");

        if (allCards.isEmpty()) {
            Label empty = new Label("nessuna carta");
            empty.setTextFill(Color.web("#444466"));
            empty.setPadding(new Insets(4));
            cardStrip.getChildren().add(empty);
        } else {
            for (Card card : allCards) {
                cardStrip.getChildren().add(buildCardNode(card));
            }
        }

        ScrollPane scroll = new ScrollPane(cardStrip);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);
        scroll.setPrefHeight(CARD_HEIGHT + 20);
        scroll.setMaxHeight(CARD_HEIGHT + 20);
        scroll.setStyle("-fx-background: #12122a; -fx-background-color: #12122a; -fx-border-color: transparent;");

        row.getChildren().addAll(nameLabel, scroll);
        HBox.setHgrow(scroll, javafx.scene.layout.Priority.ALWAYS);

        return row;
    }

    private static VBox buildCardNode(Card card) {
        VBox node = new VBox(2);
        node.setStyle("-fx-alignment: center;");

        int id = card.getId();
        String imgPath = String.format("/assets/cards/front/front_%03d.png", id - 1);

        var stream = PlayerHandView.class.getResourceAsStream(imgPath);
        if (stream != null) {
            Image img = new Image(stream);
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(CARD_HEIGHT);
            imgView.setPreserveRatio(true);
            Tooltip.install(imgView, new Tooltip(card.getShortString()));
            node.getChildren().add(imgView);
        }

        return node;
    }
}