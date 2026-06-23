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
  * Main game screen, displayed once the single game starts.
  *
  * The scene is built only once on the first show() call via buildScene().
  * All subsequent updates replace only the inner the parts that actually need to change.
  * The scene and the stage are not recreated.
  *
  * This design solves two problems:
  *
  * - Chat persistence — PlayerPanel and ChatView are created once and never
  *   destroyed, so that chat messages survive every board update.
  *
  * - replacing the entire scene while in full-screen mode
  *   caused a complete crash of the gui. Now in buildScene() we memorize the previous window's dimension
  *   before setting the new Scene (the scene is refreshed everytime the server calls updateBoard() )
  *
  *
  * flow:
  * First call:  show() → buildScene()   — builds root, top bar, center, right panel
  * Later calls: show() → refreshTable() — replaces only center (table + hand)
  * Turn change: updateTurnLabel()       — updates label text + calls refreshTable()
  *
  * Subcomponents:
  * - CardRowView   — renders the upper and lower card rows
  * - TrackView     — renders the order tile and track tiles
  * - PlayerHandView— renders the local player's hand
  * - PlayerPanel   — renders player stats (scrollable) and chat (fixed)
 *
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
     * First call: builds the entire scene and mounts it on the Stage.
     * Other calls: updates only the table area and hand, without
     * recreating Scene/Stage or destroying the PlayerPanel.
     */
    public void show(BoardSnapshot board, List<PlayerSnapshot> players) {
        this.lastBoard  = board;
        this.lastPlayers = players;

        playersNicknames.clear();
        for (PlayerSnapshot p : players) playersNicknames.add(p.nickname());

        if (!sceneBuilt) { //first call
            buildScene(board, players);
            sceneBuilt = true;
        } else { //other calls
            refreshTable(board, players);
        }
    }

    /**
     * Updates just TurnLabel and repaint the Board if needed
     * but without rebuilding the scene.
     * When your turn comes TurnLabel says "è il tuo turno" otherwise "turno di:" other player
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

    /** Returns current phase  */
    public String getCurrentPhase() {
        return currentPhase;
    }


    /**
     * Builds the main scene. buildScene is used in GameView's show() method to create
     * the part of the graphics that don't need to be refreshed after the first call (creation of the view)
     *
     * It's used to divide and organize the scene in its different panels.
     *
     * The old scene's dimensions are memorized so that after an updateBoard() the window after setScene() will have
     * the same dimensions as before
     *
     * @param board
     * @param players
     */
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

        // Right: PlayerPanel (created once then updated)
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
     * Updates the table area and the hand WITHOUT touching Stage/Scene/PlayerPanel.
     * Replaces only the child nodes of the centerPane.
     *
     * @param board   the current board state
     * @param players the list of all players in the game
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


    /**
     * Creates the table of the game, rendering the board with the orderTile, the offers' track
     * and the upper and lower rows of cards
     * @param board: main data object containing all information about the state of the table
     * @param players: list of current players
     * @param numPlayers: number of players
     * @param isMyTurn: used to enable or disable the effect of clicking cards and tiles
     * @return
     */
    private ScrollPane buildTableScroll(BoardSnapshot board, List<PlayerSnapshot> players, int numPlayers, boolean isMyTurn) {

        boolean canResolve = isMyTurn && currentPhase.equals("RESOLUTION");
        boolean canExtraPick = isMyTurn && currentPhase.equals("EXTRA_PICK");

        VBox table = new VBox();
        table.setPadding(new Insets(15));
        table.getChildren().addAll(
                CardRowView.build(board.upperRow(), 150, true,  manager,
                        canResolve || canExtraPick),
                TrackView.build(board.order(), board.track(), numPlayers, manager,
                        isMyTurn && currentPhase.equals("PLACEMENT")),
                CardRowView.build(board.lowerRow(), 150, false, manager,
                        canResolve)
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