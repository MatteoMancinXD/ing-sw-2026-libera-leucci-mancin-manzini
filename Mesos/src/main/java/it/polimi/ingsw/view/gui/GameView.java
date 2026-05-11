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
    private Label turnLabel;
    private Board lastBoard;
    private List<Player> lastPlayers;
    private String currentPlayerNickname = "";
    private String currentPhase = "";

    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(Board board, List<Player> players) {

        this.lastBoard = board;
        this.lastPlayers = players;
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
        if (turnLabel == null) {
            turnLabel = new Label("In attesa del turno...");
            turnLabel.setTextFill(Color.web("#e0a830"));
        }
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
        boolean isMyTurn = manager.getNickName().equals(currentPlayerNickname);

        table.getChildren().addAll(
                CardRowView.build(board.getUpperRow(), 150, true, manager, isMyTurn && currentPhase.equals("RESOLUTION")),
                TrackView.build(board.getTrack(), numPlayers, manager, isMyTurn && currentPhase.equals("PLACEMENT")),
                CardRowView.build(board.getLowerRow(), 150, false, manager, isMyTurn && currentPhase.equals("RESOLUTION"))
        );
        gameRoot.setCenter(tableScroll);

        table.setAlignment(Pos.TOP_LEFT);

        // Right: player stats + chat
        PlayerPanel playerPanel = new PlayerPanel(players, manager.getNickName(), manager);
        this.chatView = playerPanel.getChatView();
        gameRoot.setRight(playerPanel.getRightPanel());


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
    public void updateTurnLabel(String nickname, String phase) {
        this.currentPlayerNickname = nickname;
        this.currentPhase = phase;
        if (turnLabel == null) return;
        String myNick = manager.getNickName();
        if (nickname.equals(myNick)) {
            turnLabel.setText("È il tuo turno! Fase: " + phase);
            turnLabel.setTextFill(Color.web("#00ff88"));
        } else {
            turnLabel.setText("Turno di: " + nickname + " | Fase: " + phase);
            turnLabel.setTextFill(Color.web("#e0a830"));
        }
        if (lastBoard != null && lastPlayers != null) {
            show(lastBoard, lastPlayers);
        }

    }
}