package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import javafx.geometry.Insets;
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
 *
 * ARCHITETTURA:
 *   - La scena viene costruita UNA SOLA VOLTA in buildScene().
 *   - show() al primo invoco costruisce la scena; nei successivi aggiorna
 *     solo i sottoalberi che cambiano (table area + hand).
 *   - updateTurnLabel() aggiorna solo la label, senza ricostruire nulla.
 *
 * Questo evita il crash D3D che si verificava perché JavaFX perdeva il
 * contesto GPU durante ricostruzioni rapide della scena a schermo intero.
 */
public class GameView {

    private final Stage stage;
    private final GuiManager manager;

    // Stato persistente tra refresh
    private List<String> playersNicknames = new ArrayList<>();
    private ChatView chatView;
    private PlayerPanel playerPanel;
    private Label turnLabel;
    private BoardSnapshot lastBoard;
    private List<PlayerSnapshot> lastPlayers;
    private String currentPlayerNickname = "";
    private String currentPhase = "";

    // Nodi della scena costruiti una sola volta
    private BorderPane gameRoot;
    private BorderPane centerPane;   // center della scena: tableScroll + handSection
    private boolean sceneBuilt = false;

    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    /**
     * Prima chiamata: costruisce l'intera scena e la monta sullo Stage.
     * Chiamate successive: aggiorna solo table area e hand, senza
     * ricreare Scene/Stage né distruggere il PlayerPanel.
     */
    public void show(BoardSnapshot board, List<PlayerSnapshot> players) {
        this.lastBoard  = board;
        this.lastPlayers = players;

        playersNicknames.clear();
        for (PlayerSnapshot p : players) playersNicknames.add(p.nickname());

        if (!sceneBuilt) {
            buildScene(board, players);
            sceneBuilt = true;
        } else {
            refreshTable(board, players);
        }
    }

    /**
     * Aggiorna solo la label del turno e, se serve, ridipinge la board
     * (ma senza ricostruire la scena).
     */
    public void updateTurnLabel(String nickname, String phase, int round, int era) {
        this.currentPlayerNickname = nickname;
        this.currentPhase = phase;

        if (turnLabel != null) {
            StringBuilder text = new StringBuilder("Era: " + era + " Round: " + round + " | ");
            if (nickname.equals(manager.getNickName())) {
                text.append("È il tuo turno | Fase: ").append(phase);
                turnLabel.setTextFill(Color.web("#00ff88"));
            } else {
                text.append("Turno di: ").append(nickname).append(" | Fase: ").append(phase);
                turnLabel.setTextFill(Color.web("#e0a830"));
            }
            turnLabel.setText(text.toString());
        }

        // Aggiorna solo la table area (bottoni interattivi), non la scena intera
        if (lastBoard != null && lastPlayers != null && sceneBuilt) {
            refreshTable(lastBoard, lastPlayers);
        }
    }

    public boolean tryShowChatMessage(String sender, String message) {
        if (chatView == null || playersNicknames.isEmpty()) return false;
        if (sender.equals(manager.getNickName())) return true;
        chatView.appendMessage(sender, message);
        return true;
    }

    private void buildScene(BoardSnapshot board, List<PlayerSnapshot> players) {
        int numPlayers   = players.size();
        boolean isMyTurn = manager.getNickName().equals(currentPlayerNickname);

        // Root
        gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        turnLabel = new Label("In attesa del turno...");
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // Center: table scrollabile + hand fissa in basso
        centerPane = new BorderPane();
        centerPane.setStyle("-fx-background-color: #1a1a2e;");
        centerPane.setCenter(buildTableScroll(board, players, numPlayers, isMyTurn));
        centerPane.setBottom(PlayerHandView.build(players, manager.getNickName()));
        gameRoot.setCenter(centerPane);

        // Right: PlayerPanel (creato una sola volta)
        playerPanel = new PlayerPanel(players, manager.getNickName(), manager);
        chatView    = playerPanel.getChatView();
        gameRoot.setRight(playerPanel.getRightPanel());

        // Monta la scena sullo Stage preservando dimensioni/posizione
        Scene scene = new Scene(gameRoot, 1100, 700);
        boolean wasMaximized = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(),     y = stage.getY();

        stage.setScene(scene);

        if (wasMaximized) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(w);
            stage.setHeight(h);
            stage.setX(x);
            stage.setY(y);
        }
    }


    /**
     * Aggiorna la table area e la hand SENZA toccare Stage/Scene/PlayerPanel.
     * Sostituisce solo i nodi figli del centerPane.
     */
    private void refreshTable(BoardSnapshot board, List<PlayerSnapshot> players) {
        int numPlayers   = players.size();
        boolean isMyTurn = manager.getNickName().equals(currentPlayerNickname);

        centerPane.setCenter(buildTableScroll(board, players, numPlayers, isMyTurn));
        centerPane.setBottom(PlayerHandView.build(players, manager.getNickName()));

        // Aggiorna solo le statistiche nel pannello destro (non lo ricrea)
        if (playerPanel != null) {
            playerPanel.updateStats(players, manager.getNickName());
        }
    }


    private ScrollPane buildTableScroll(BoardSnapshot board, List<PlayerSnapshot> players,
                                        int numPlayers, boolean isMyTurn) {
        VBox table = new VBox();
        table.setPadding(new Insets(15));
        table.getChildren().addAll(
                CardRowView.build(board.upperRow(), 150, true,  manager,
                        isMyTurn && currentPhase.equals("RESOLUTION")),
                TrackView.build(board.order(), board.track(), numPlayers, manager,
                        isMyTurn && currentPhase.equals("PLACEMENT")),
                CardRowView.build(board.lowerRow(), 150, false, manager,
                        isMyTurn && currentPhase.equals("RESOLUTION"))
        );

        ScrollPane scroll = new ScrollPane(table);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(false);
        scroll.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: #1a1a2e;" +
                        "-fx-border-color: transparent;"
        );
        return scroll;
    }
}