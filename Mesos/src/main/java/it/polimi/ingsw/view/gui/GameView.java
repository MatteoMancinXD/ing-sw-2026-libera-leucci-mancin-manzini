package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * GameView — main game screen.
 * Delegates UI construction to:
 *   CardRowView  — upper and lower card rows
 *   TrackView    — totem placement card + track tiles
 *   PlayerPanel  — player stats + chat
 */
public class GameView {

    private final Stage stage;
    private final GuiManager manager;
    private List<String> playersNicknames = new ArrayList<>();
    private ChatView chatView;

    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(Board board, List<Player> players) {

        // Save nicknames for chat recognition
        playersNicknames.clear();
        for (Player p : players) playersNicknames.add(p.getNickname());

        int numPlayers = players.size();

        // Root
        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar  STILL TO DO
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        Label turnLabel = new Label("In attesa del turno...");   //STILL TO DO
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // --- Center: upper row + track + lower row ---
        VBox table = new VBox();
        ScrollPane tableScroll = new ScrollPane(table);

        //horizontal scroll pane (if needed)
        tableScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Barra orizzontale se serve
        tableScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);     //no height scroll
        tableScroll.setFitToHeight(true);  // block scroll on height
        tableScroll.setFitToWidth(false);  // scroll on width
        tableScroll.setStyle("-fx-background: transparent; -fx-background-color: #1a1a2e; -fx-border-color: transparent;");



        table.setPadding(new Insets(15));
        table.getChildren().addAll(
                CardRowView.build(board.getUpperRow(), 150),
                TrackView.build(board.getTrack(), numPlayers),
                CardRowView.build(board.getLowerRow(), 150)
        );
        gameRoot.setCenter(tableScroll);

        table.setAlignment(Pos.TOP_LEFT);

        // Right: player stats + chat
        gameRoot.setRight(PlayerPanel.build(players, manager.getNickName(), manager));

        // Scene
        Scene gameScene = new Scene(gameRoot, 1100, 700);
        stage.setScene(gameScene);
        stage.centerOnScreen();
    }


    //  Chat

    /**
     * Called by GuiManager.showMessage() for every incoming message.
     * Returns true if the message was a chat message and was displayed.
     */
    public boolean tryShowChatMessage(String message) {
        if (chatView == null || playersNicknames.isEmpty()) return false;

        for (String nick : playersNicknames) {
            if (message.startsWith(nick + ": ")) {
                if (nick.equals(manager.getNickName())) return true; // don't echo own messages
                String text = message.substring(nick.length() + 2);
                chatView.appendMessage(nick, text);
                return true;
            }
        }
        return false;
    }
}