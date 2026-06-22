package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;


/**
 * GuiManager is JavaFX's entry point.
 * It allows the GUI to drive among the scenes and keeps shared references(Stage, NetworkClient, nickname).
 *
 */
public class GuiManager extends Application implements ui {


    private Stage primaryStage;
    private NetworkClient client;
    private String nickname;
    private LobbyView lobbyView;
    private GameView gameView;
    private GameSetup gameSetup;
    private TotemSelectView totemSelectView;
    private WaitingView waitingView;

    //javafx
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mesos");


        // creates the lobby and saves it as a parameter to receive the callbacks
        lobbyView = new LobbyView(stage, this);
        lobbyView.show();
        //creates gameView
        gameView = new GameView(stage, this);

        stage.show();
    }


    /**
     * Calls the client (RMI or Socket) if it's not connected
     * this class is called by LobbyView before every action regarding the network
     */
    public void connectClient(String nickname, String ip, boolean isRmi) throws Exception {
        if (client != null) return;   // già connesso

        this.nickname = nickname;

        if (isRmi) { //RMI
            RmiClient rmiClient = new RmiClient(this, nickname);
            rmiClient.startConnection(ip, 1099);
            this.client = rmiClient;
        } else { //Socket
            SocketClient socketClient = new SocketClient(nickname);
            socketClient.setUserInterface(this);
            socketClient.startConnection(ip, 5000);
            Thread.sleep(500);  //thread waits for socket to be ready
            this.client = socketClient;
        }
    }

/**
 * LobbyView calls these methods, then GuiManager passes them to NetworkClient
 * so that the ui and the network are divided
 * in LobbyView we use (for ex):  manager.createGame("player1",2)
 *
*/
    public void createGame(String nickname, int numPlayers) throws Exception {
        client.createGame(nickname, numPlayers);
    }

    public void joinGame(String nickname, int gameId) throws Exception {
        client.joinGame(nickname, gameId);
    }

    public void requestAvailableGames() throws Exception {
        client.requestAvailableGames();
    }


    /**

     Sends a draw request to the server.
     The server autonomously handles the logic based on the current phase.
     @param row true = upper row, false = lower row
     @param index position of the card in the row
     The logic behind the "EXTRA_PICK" phase is handled in Game class in model
     */
    public void drawCard(boolean row, int index) throws Exception {
        System.out.println("DEBUG GUI drawCard row=" + row + " index=" + index);
        client.askToDrawCard(row,index);
    }

    /**
     * Called from GameView to place the player's totem on a certain tile
     * @param index: index of the tile in the offers' track
     */
    public void placeTotem(int index) throws Exception {
        System.out.println("DEBUG GUI placeTotem index=" + index);
        client.askToPlaceTotem(index);
    }

    //ui methods

    /**
     * Allows to update the graphic rendering of the game to reflect the current state
     * @param board: state of the board
     * @param players: current players
     */
    @Override
    public void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players) {
        Platform.runLater(() -> gameView.show(board, players));
    }

    /**
     * Displays an error received from server
     * @param errorMessage: error received
     */
    @Override
    public void showError(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }

    /**
     * Updates the turn management of the game, eventually allowing the player
     * to place the totem/draw cards
     * @param currentPlayerNickname: nickname of the active player
     * @param gamePhase: phase of the game (placing/resolution/extra pick)
     * @param round: round of the game
     * @param era: current era
     */
    @Override
    public void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era) {
        Platform.runLater(() -> {
            if (gameView != null) gameView.updateTurnLabel(currentPlayerNickname, gamePhase, round, era);        });
    }

    /**
     * Notifies that the game is concluded and proceeds to show current and global rankings
     * @param rankings: rankings for the current game
     * @param globalRanks: historical rankings
     */
    @Override
    public void notifyEndGame(List<String> rankings, List<LeaderboardEntryBean> globalRanks) {
        System.out.println("DEBUG notifyEndGame: " + rankings);
        Platform.runLater(() -> {
            EndGameView endGameView = new EndGameView(primaryStage, this);
            endGameView.show(rankings, globalRanks);
        });
    }

    /**
     * Generic method invoked to show a message received from the server
     * @param message: message to be shown
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
        Platform.runLater(() -> {

            // if the message starts with one of the following strings then it's from the lobby
            if (message.startsWith("Available games:") ||
                    message.contains("Game #") ||
                    message.contains("Game ID") ||
                    message.contains("No available games")) {
                if (gameSetup != null) gameSetup.updateGamesList(message);
                return;
            }


            // message that updates the lobby status
            if (lobbyView != null) lobbyView.showStatusOk(message);

            // update totem's color selection menu
            if (totemSelectView != null) totemSelectView.updateWaitingLabel(message);
        });
    }

    /**
     * Method invoked to show a message in the players' chat
     * @param sender: nickname of the author of the message
     * @param message: content of the message
     */
    @Override
    public void showChatMessage(String sender, String message) {
        Platform.runLater(() -> {
            gameView.tryShowChatMessage(sender, message);
        });
    }

    /**
     * Called from TotemSelectView, shows the set of available totems
     * @param totems: set of available totems
     */
    @Override
    public void showAvailableTotems(Set<Totem> totems) {
        Platform.runLater(() -> {
            totemSelectView.showTotems(totems);
        });
    }

    @Override
    public void onTotemSelected() {}

    /**
     * Hands a first set of available totems right after the player officially
     * joins a game
     * @param totems: available totems
     */
    @Override
    public void onGameParticipation(Set<Totem> totems) {
        System.out.println("onGameParticipation() called");
        Platform.runLater(() -> {
            TotemSelectView totemSelect = new TotemSelectView(primaryStage, this);
            setTotemSelectView(totemSelect);
            totemSelect.show(totems);
        });
    }

    /**
     * Sends a message in the chat. The chat is available once the game is started.
     * The communication is handled by the server while the chat itself is available only
     * during the game and between the players' in the same game. See ChatView class in gui
     * @param message
     * @throws Exception
     */
    public void sendChatMessage(String message) throws Exception {
        client.sendChatMessage(message);
    }


    //getter
    public String getNickName(){
        return this.nickname;
    }

    public void setGameSetup(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    public void setTotemSelectView(TotemSelectView totemSelectView) {
        this.totemSelectView = totemSelectView;
    }

    public void requestAvailableTotems() {
        client.requestAvailableTotems();
    }

    public void selectTotem(String strTotem) {
        Totem totem = Totem.valueOf(strTotem.toUpperCase());
        client.askToSelectTotem(totem);
    }

    public void setWaitingView(WaitingView waitingView) {
        this.waitingView = waitingView;
    }
}