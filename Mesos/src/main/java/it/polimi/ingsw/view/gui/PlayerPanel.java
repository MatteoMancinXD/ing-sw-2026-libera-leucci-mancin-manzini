package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

import java.util.List;

/**
 * PlayerPanel builds the right panel: player stats (scrollable) + chat (fixed at bottom).
 */
public class PlayerPanel {

    private static final double CHAT_HEIGHT = 280;

    /**
     * @param players   list of players to show stats for
     * @param localNick nickname of the local player (to mark as YOU)
     * @param manager   GuiManager reference passed to ChatView
     * @return BorderPane ready to be set as gameRoot.setRight()
     */
    public static BorderPane build(List<Player> players, String localNick, GuiManager manager) {
        BorderPane rightPanel = new BorderPane();
        rightPanel.setPrefWidth(220);
        rightPanel.setStyle("-fx-background-color: #1a1a2e;");

        // --- Stats (scrollable) ---
        VBox sideBar = buildStatsBox(players, localNick);
        ScrollPane statsScroll = new ScrollPane(sideBar);
        statsScroll.setFitToWidth(true);
        statsScroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        // --- Chat (fixed height at bottom) ---
        ChatView chatView = new ChatView(manager, "Game Chat");
        VBox chatContainer = chatView.getRoot();
        chatContainer.setPrefHeight(CHAT_HEIGHT);
        chatContainer.setMaxHeight(CHAT_HEIGHT);

        rightPanel.setCenter(statsScroll);
        rightPanel.setBottom(chatContainer);

        return rightPanel;
    }

    //  Private helpers

    private static VBox buildStatsBox(List<Player> players, String localNick) {
        VBox sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        sideBar.getChildren().add(makeLabel("PLAYERS:"));
        sideBar.getChildren().add(makeLabel("---STATS---"));

        for (Player p : players) {
            String name = p.getNickname().equals(localNick)
                    ? "Player: " + p.getNickname() + " (YOU)"
                    : "Player: " + p.getNickname();

            sideBar.getChildren().add(makeLabel(name));
            sideBar.getChildren().add(makeLabel("Food: " + p.getFood() + "  Prestige: " + p.getPrestige()));
            sideBar.getChildren().add(makeLabel("Artists: "    + p.getArtists().size()));
            sideBar.getChildren().add(makeLabel("Builders: "   + p.getBuilders().size()));
            sideBar.getChildren().add(makeLabel("Harvesters: " + p.getHarvesters().size()));
            sideBar.getChildren().add(makeLabel("Hunters: "    + p.getHunters().size()));
            sideBar.getChildren().add(makeLabel("Shamans: "    + p.getShamans().size()));
            sideBar.getChildren().add(makeLabel("Inventors: "  + p.getInventors().size()));
            sideBar.getChildren().add(makeLabel("Buildings: "  + p.getBuildings().size()));
            sideBar.getChildren().add(new Separator());
        }

        return sideBar;
    }

    private static Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }
}