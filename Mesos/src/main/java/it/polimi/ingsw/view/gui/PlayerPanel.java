package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PlayerPanel builds the right stats panel: player stats (scrollable) + chat (fixed at bottom).
 */
public class PlayerPanel {

    private static final double CHAT_HEIGHT = 280;
    private final ChatView chatView;
    private final BorderPane rightPanel;
    private final VBox sideBar;
    /**
     * @param players   list of players to show stats for
     * @param localNick nickname of the local player (to mark as YOU)
     * @param manager   GuiManager reference passed to ChatView
     * @return BorderPane ready to be set as gameRoot.setRight()
     */
    public PlayerPanel(List<PlayerSnapshot> players, String localNick, GuiManager manager) {
        rightPanel = new BorderPane();
        rightPanel.setPrefWidth(220);
        rightPanel.setStyle("-fx-background-color: #1a1a2e;");

        sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        fillStats(players, localNick);

        // Stats (scrollable)
        ScrollPane statsScroll = new ScrollPane(sideBar);
        statsScroll.setFitToWidth(true);
        statsScroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        // Chat (fixed height at bottom)
        chatView = new ChatView(manager, "Game Chat");
        VBox chatContainer = chatView.getRoot();
        chatContainer.setPrefHeight(CHAT_HEIGHT);
        chatContainer.setMaxHeight(CHAT_HEIGHT);

        rightPanel.setCenter(statsScroll);
        rightPanel.setBottom(chatContainer);


    }

    /**
     * update player stats without touching the chat
     * called on every updateBoard() after the first show()
     */
    public void updateStats(List<PlayerSnapshot> players, String localNick) {
        sideBar.getChildren().clear();
        fillStats(players, localNick);          // refill with updated data
    }

    //  Private helpers

    //called by updateStats() and by constructor
    private void fillStats(List<PlayerSnapshot> players, String localNick) {
        sideBar.getChildren().add(makeLabel("PLAYERS:"));
        sideBar.getChildren().add(makeLabel("---STATS---"));

        for (PlayerSnapshot p : players) {
            String name = p.nickname().equals(localNick)
                    ? "Player: " + p.nickname() + " (YOU)"
                    : "Player: " + p.nickname();


            String buildingsText = p.buildings().stream()
                    .map(b -> b.getClass().getSimpleName())
                    .collect(Collectors.joining("\n"));



            sideBar.getChildren().add(makeLabel(name));
            sideBar.getChildren().add(makeLabel("Food: " + p.food() + "  Prestige: " + p.prestige()));
            sideBar.getChildren().add(makeLabel("Stars: "    + p.totStars()));
            sideBar.getChildren().add(makeLabel("Artists: "    + p.artists().size()));
            sideBar.getChildren().add(makeLabel("Builders: "   + p.builders().size() + " tot bonus: " + p.totDiscount()));
            sideBar.getChildren().add(makeLabel("Harvesters: " + p.harvesters().size()));
            sideBar.getChildren().add(makeLabel("Hunters: "    + p.hunters().size()));
            sideBar.getChildren().add(makeLabel("Shamans: "    + p.shamans().size()));
            sideBar.getChildren().add(makeLabel("Inventors: "  + p.inventors().size()));
            sideBar.getChildren().add(makeLabel("Buildings: "  + p.buildings().size()));


            sideBar.getChildren().add(new Separator());
        }
    }




    private static Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }


    //getters
    public ChatView getChatView()   { return chatView; }
    public BorderPane getRightPanel() { return rightPanel; }

}