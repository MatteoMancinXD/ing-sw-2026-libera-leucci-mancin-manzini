package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.snapshots.CardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

    private static final double CARD_HEIGHT = 80;
    // Altezza totale fissa della sezione: carta + label + padding + scrollbar
    private static final double SECTION_HEIGHT = CARD_HEIGHT + 20 + 24 + 14; // ~138

    /**
     * Builds the bottom section with one card row per player.
     * @param players   full list of players
     * @param localNick nickname of the local player (shown first)
     * @return HBox ready to be used in the SplitPane
     */
    public static HBox build(List<PlayerSnapshot> players, String localNick) {
        HBox allColumns = new HBox(0);
        allColumns.setStyle("-fx-background-color: #12122a; -fx-border-color: #e0a830; -fx-border-width: 1 0 0 0;");
        // Altezza fissa e rigida: impedisce che il SplitPane schiacci la sezione
        allColumns.setMinHeight(SECTION_HEIGHT);
        allColumns.setPrefHeight(SECTION_HEIGHT);
        allColumns.setMaxHeight(SECTION_HEIGHT);

        List<PlayerSnapshot> ordered = new ArrayList<>();
        players.stream().filter(p -> p.nickname().equals(localNick)).findFirst().ifPresent(ordered::add);
        players.stream().filter(p -> !p.nickname().equals(localNick)).forEach(ordered::add);

        for (int i = 0; i < ordered.size(); i++) {
            PlayerSnapshot p = ordered.get(i);
            VBox col = buildPlayerColumn(p, localNick);
            HBox.setHgrow(col, Priority.ALWAYS);

            // Divisore verticale tra colonne (non dopo l'ultima)
            if (i < ordered.size() - 1) {
                col.setStyle(
                        col.getStyle() +
                                "-fx-border-color: #e0a830;" +
                                "-fx-border-width: 0 1 0 0;"
                );
            }

            allColumns.getChildren().add(col);
        }

        return allColumns;
    }

    private static VBox buildPlayerColumn(PlayerSnapshot player, String localNick) {
        VBox col = new VBox(0);
        col.setStyle("-fx-background-color: #12122a;");
        // La colonna prende tutta l'altezza disponibile dal genitore HBox
        col.setMaxHeight(Double.MAX_VALUE);

        // --- Intestazione con nickname ---
        boolean isLocal = player.nickname().equals(localNick);
        String labelText = isLocal
                ? "▶ " + player.nickname() + " (You)"
                : "  " + player.nickname();

        Label nameLabel = new Label(labelText);
        nameLabel.setTextFill(isLocal ? Color.web("#e0a830") : Color.web("#aaaaaa"));
        nameLabel.setPrefWidth(120);
        nameLabel.setMinWidth(120);
        nameLabel.setPadding(new Insets(5, 8, 5, 8));
        nameLabel.setWrapText(false);
        nameLabel.setStyle("-fx-background-color: #1a1a2e; -fx-font-weight: " + (isLocal ? "bold" : "normal") + ";");
        // Altezza fissa per la label: evita che si espanda o collassi
        nameLabel.setMinHeight(24);
        nameLabel.setPrefHeight(24);
        nameLabel.setMaxHeight(24);

        // --- Raccoglie tutte le carte del giocatore ---
        List<CardSnapshot> allCards = new ArrayList<>();
        allCards.addAll(player.artists());
        allCards.addAll(player.builders());
        allCards.addAll(player.harvesters());
        allCards.addAll(player.hunters());
        allCards.addAll(player.shamans());
        allCards.addAll(player.inventors());
        allCards.addAll(player.buildings());

        // --- Striscia carte ---
        HBox cardStrip = new HBox(4);
        cardStrip.setPadding(new Insets(4, 8, 4, 8));
        cardStrip.setAlignment(Pos.CENTER_LEFT);
        cardStrip.setStyle("-fx-background-color: #12122a;");

        if (allCards.isEmpty()) {
            Label empty = new Label("nessuna carta");
            empty.setTextFill(Color.web("#444466"));
            empty.setPadding(new Insets(4));
            cardStrip.getChildren().add(empty);
        } else {
            for (CardSnapshot card : allCards) {
                cardStrip.getChildren().add(buildCardNode(card));
            }
        }

        // --- ScrollPane ---
        // NON usare setFitToHeight(true): lascia che la ScrollPane abbia
        // un'altezza definita esplicitamente, altrimenti collassa a 0.
        double scrollHeight = CARD_HEIGHT + 20; // carta + padding + scrollbar
        ScrollPane scroll = new ScrollPane(cardStrip);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);   // adatta l'altezza del contenuto alla ScrollPane
        scroll.setFitToWidth(false);   // lascia scorrere in orizzontale
        scroll.setMinHeight(scrollHeight);
        scroll.setPrefHeight(scrollHeight);
        scroll.setMaxHeight(scrollHeight);
        scroll.setStyle("-fx-background: #12122a; -fx-background-color: #12122a; -fx-border-color: transparent;");
        // NON usare VBox.setVgrow qui: l'altezza è già fissa
        VBox.setVgrow(scroll, Priority.NEVER);

        col.getChildren().addAll(nameLabel, scroll);

        return col;
    }

    private static VBox buildCardNode(CardSnapshot card) {
        VBox node = new VBox(2);
        node.setStyle("-fx-alignment: center;");

        int id = card.id();
        String imgPath = String.format("/assets/cards/front/front_%03d.png", id - 1);

        var stream = PlayerHandView.class.getResourceAsStream(imgPath);
        if (stream != null) {
            Image img = new Image(stream);
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(CARD_HEIGHT);
            imgView.setPreserveRatio(true);
            Tooltip.install(imgView, new Tooltip(card.desc()));
            node.getChildren().add(imgView);
        }

        return node;
    }
}