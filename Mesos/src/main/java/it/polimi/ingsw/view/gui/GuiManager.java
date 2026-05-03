package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
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


    //javafx
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mesos");

        // Crea la lobby e la salva come campo per ricevere i callback
        lobbyView = new LobbyView(stage, this);
        lobbyView.show();

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


    //ui methods

    @Override
    public void updateBoard(Board board, List<Player> players) {
        Platform.runLater(() -> showGameScreen(board, players));
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
            // aggiorna la label del turno nella schermata di gioco
        });
    }

    @Override
    public void notifyEndGame(List<String> rankings) {
        Platform.runLater(() -> {
            // mostra schermata finale con classifica
        });
    }

    @Override
    public void showMessage(String message) {
        System.out.println("DEBUG showMessage: " + message);
        Platform.runLater(() -> {
            if (lobbyView != null &&
                    (message.startsWith("Available games:") ||
                            message.contains("Game #") ||
                            message.contains("Game ID"))) {
                lobbyView.updateGamesList(message);
            } else if (lobbyView != null) {
                lobbyView.showStatusOk(message);
            }
        });
    }

    // ------------------------------------------------------------------ //
    //  DA SPOSTARE IN UN ALTRA CLASSE view                                               //
    // ------------------------------------------------------------------ //

    private void showGameScreen(Board board, List<Player> players) {
        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar — turno
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        Label turnLabel = new Label("In attesa del turno...");
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // Centro — griglia board
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button cell = new Button();
                cell.setPrefSize(60, 60);
                cell.setStyle("-fx-background-color: #2a2a4a; -fx-border-color: #e0a830;");
                boardGrid.add(cell, i, j);
            }
        }
        gameRoot.setCenter(boardGrid);

        // Destra — stats giocatori
        VBox sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        sideBar.getChildren().add(makeLabel("GIOCATORI:"));
        sideBar.getChildren().add(makeLabel("---STATS---"));

        for (Player p : players) {
            sideBar.getChildren().add(makeLabel("Player: " + p.getNickname()));
            sideBar.getChildren().add(makeLabel("Food: " + p.getFood() + "  Prestige: " + p.getPrestige()));

            List<Card> everyCard = new ArrayList<>();
            everyCard.addAll(p.getArtists());
            everyCard.addAll(p.getBuilders());
            everyCard.addAll(p.getHarvesters());
            everyCard.addAll(p.getHunters());
            everyCard.addAll(p.getShamans());
            everyCard.addAll(p.getInventors());
            everyCard.addAll(p.getBuildings());
            sideBar.getChildren().add(makeLabel("Carte: " + everyCard.size()));
            sideBar.getChildren().add(new Separator());
        }
        gameRoot.setRight(sideBar);

        Scene gameScene = new Scene(gameRoot, 1000, 700);
        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen();
    }

    //helper ui

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }
}