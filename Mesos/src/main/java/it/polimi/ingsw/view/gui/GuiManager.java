package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;


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
    private GameWaiting gameWaiting;

    //javafx
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mesos");


        // Crea la lobby e la salva come campo per ricevere i callback
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

        if (isRmi) {
            RmiClient rmiClient = new RmiClient(this, nickname);
            rmiClient.startConnection(ip, 1099);
            this.client = rmiClient;
        } else {
            SocketClient socketClient = new SocketClient(nickname);
            socketClient.setUserInterface(this);
            socketClient.startConnection(ip, 5000);
            Thread.sleep(500);
            this.client = socketClient;
        }
    }


    //LobbyView calls these methods, then GuiManeger passes them to NetworkClient
    //so that the ui and the network are divided
    //in LobbyView we use:  manager.createGame("player1",2)

    public void createGame(String nickname, int numPlayers) throws Exception {
        client.createGame(nickname, numPlayers);
    }

    public void joinGame(String nickname, int gameId) throws Exception {
        client.joinGame(nickname, gameId);
    }

    public void requestAvailableGames() throws Exception {
        client.requestAvailableGames();
    }

    public void drawCard(boolean row, int index) throws Exception {
        System.out.println("DEBUG GUI drawCard row=" + row + " index=" + index);
        client.askToDrawCard(row, index);
    }

    public void placeTotem(int index) throws Exception {
        System.out.println("DEBUG GUI placeTotem index=" + index);
        client.askToPlaceTotem(index);
    }

    //ui methods

    @Override
    public void updateBoard(Board board, List<Player> players) {
        Platform.runLater(() -> gameView.show(board, players));
    }

    @Override
    public void showError(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }

    @Override
    public void notifyTurn(String currentPlayerNickname, String gamePhase) {
        Platform.runLater(() -> {
            if (gameView != null) gameView.updateTurnLabel(currentPlayerNickname, gamePhase);        });
    }

    @Override
    public void notifyEndGame(List<String> rankings) {
        Platform.runLater(() -> {
            EndGameView endGameView = new EndGameView(primaryStage, this);
            endGameView.show(rankings);        });
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
        Platform.runLater(() -> {

            // 1. Lista partite → lobby
            if (message.startsWith("Available games:") ||
                    message.contains("Game #") ||
                    message.contains("Game ID")) {
                if (gameSetup != null) gameSetup.updateGamesList(message);
                return;
            }

            // 2. Messaggio di chat → formato "nickname: testo"
            //    GameView sa chi sono i giocatori e decide se è chat
            if (gameView != null && gameView.tryShowChatMessage(message)) {
                return;  // GameView ha riconosciuto e mostrato il messaggio
            }

            // 3. Tutto il resto → messaggio di stato nella lobby
            if (lobbyView != null) lobbyView.showStatusOk(message);

            // 4. Aggiornamento schermata di caricamento
            if (gameWaiting != null) gameWaiting.updateWaitingLabel(message);
        });
    }


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

    public void setGameWaiting(GameWaiting gameWaiting) {
        this.gameWaiting = gameWaiting;
    }


}