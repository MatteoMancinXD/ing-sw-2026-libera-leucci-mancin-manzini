package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
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
    private PlayerPanel playerPanel;
    private Label turnLabel;
    private BoardSnapshot lastBoard;
    private List<PlayerSnapshot> lastPlayers;
    private String currentPlayerNickname = "";
    private String currentPhase = "";

    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(BoardSnapshot board, List<PlayerSnapshot> players) {

        this.lastBoard = board;
        this.lastPlayers = players;
        // Save nicknames for chat recognition
        playersNicknames.clear();
        for (PlayerSnapshot p : players) playersNicknames.add(p.nickname());

        int numPlayers = players.size();
        boolean isMyTurn = manager.getNickName().equals(currentPlayerNickname);

        // Root
        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar
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


        table.getChildren().addAll(
                CardRowView.build(board.upperRow(), 150, true, manager, isMyTurn && currentPhase.equals("RESOLUTION")),
                TrackView.build(board.order(), board.track(), numPlayers, manager, isMyTurn && currentPhase.equals("PLACEMENT")),
                CardRowView.build(board.lowerRow(), 150, false, manager, isMyTurn && currentPhase.equals("RESOLUTION"))
        );
        VBox handSection = PlayerHandView.build(players, manager.getNickName());

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(tableScroll, handSection);
        splitPane.setDividerPositions(0.65);
        splitPane.setStyle("-fx-background-color: #1a1a2e;");




        // PlayerPanel is created only once to preserve chat history.
        // then only the stats are updated.
        if (playerPanel == null) {
            playerPanel = new PlayerPanel(players, manager.getNickName(), manager);
            chatView = playerPanel.getChatView();
        } else {
            playerPanel.updateStats(players, manager.getNickName());
        }
        gameRoot.setCenter(splitPane);
        gameRoot.setRight(playerPanel.getRightPanel());





        // Scene
        Scene gameScene = new Scene(gameRoot, 1100, 700);
        // salva dimensioni e stato corrente
        boolean wasMaximized = stage.isMaximized();
        double prevWidth  = stage.getWidth();
        double prevHeight = stage.getHeight();
        double prevX      = stage.getX();
        double prevY      = stage.getY();
        stage.setScene(gameScene);
        if (wasMaximized) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(prevWidth);
            stage.setHeight(prevHeight);
            stage.setX(prevX);
            stage.setY(prevY);
        }
    }


    //  Chat

    /**
     * Called by GuiManager.showMessage() for every incoming message.
     * Returns true if the message was a chat message and was displayed.
     */
    public boolean tryShowChatMessage(String sender, String message) {
        if (chatView == null || playersNicknames.isEmpty()) {
            return false;
        }

        if (sender.equals(manager.getNickName())) {
            return true;    // don't echo your own messages
        }

        chatView.appendMessage(sender, message);
        return true;

    }
    public void updateTurnLabel(String nickname, String phase) {
        this.currentPlayerNickname = nickname;
        this.currentPhase = phase;

        if (turnLabel != null) {
            if (nickname.equals(manager.getNickName())) {
                turnLabel.setText("È il tuo turno! Fase: " + phase);
                turnLabel.setTextFill(Color.web("#00ff88"));
            } else {
                turnLabel.setText("Turno di: " + nickname + " | Fase: " + phase);
                turnLabel.setTextFill(Color.web("#e0a830"));
            }
        }

        // Refresh board to update interactive state of buttons
        if (lastBoard != null && lastPlayers != null) {
            show(lastBoard, lastPlayers);
        }


    }
}