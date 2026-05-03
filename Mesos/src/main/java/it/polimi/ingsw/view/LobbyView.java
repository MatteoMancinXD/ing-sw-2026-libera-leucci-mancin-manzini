package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LobbyView extends Application implements ui {

    private NetworkClient client;
    private Stage primaryStage;
    private Label messageLabel;
    private TextArea gamesListArea;

    private String nickname;
    private int numPlayers;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mesos");
        showLobbyScreen();
        stage.show();
    }

    private void showLobbyScreen() {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Titolo
        Text title = new Text("MESOS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));


        // Nickname
        Label nicknameLabel = makeLabel("Nickname");
        TextField nicknameField = makeTextField("Inserisci il tuo nickname");

        // IP Server
        Label ipLabel = makeLabel("IP Server");
        TextField ipField = makeTextField("127.0.0.1");
        ipField.setText("127.0.0.1");

        // Protocollo
        Label protocolLabel = makeLabel("Protocollo");
        ToggleGroup protocolGroup = new ToggleGroup();
        RadioButton rmiButton = makeRadio("RMI", protocolGroup, true);
        RadioButton socketButton = makeRadio("Socket", protocolGroup, false);
        HBox protocolBox = new HBox(20, rmiButton, socketButton);
        protocolBox.setAlignment(Pos.CENTER);

        // Lista partite disponibili
        Label gamesLabel = makeLabel("Partite disponibili");
        gamesListArea = new TextArea();
        gamesListArea.setEditable(false);
        gamesListArea.setPrefHeight(80);
        gamesListArea.setMaxWidth(400);
        gamesListArea.setPromptText("Clicca 'Aggiorna' per vedere le partite disponibili");
        gamesListArea.setStyle(
                "-fx-background-color: #2a2a4a;" +
                        "-fx-text-fill: white;" +
                        "-fx-control-inner-background: #2a2a4a;" +
                        "-fx-prompt-text-fill: #666688;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;"
        );

        Button refreshButton = new Button("Aggiorna lista");
        styleButton(refreshButton, "#2a2a4a", "#e0a830");
        refreshButton.setOnAction(e -> {
            String ip = ipField.getText().trim();
            String nickname = nicknameField.getText().trim();
            boolean isRmi = rmiButton.isSelected();

            if (nickname.isEmpty()) { showStatusError("Inserisci un nickname prima."); return; }

            new Thread(() -> {
                try {
                    connectClient(nickname, ip, isRmi);
                    client.requestAvailableGames();
                    showStatusOk("Lista aggiornata.");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        // Numero giocatori
        Label numPlayersLabel = makeLabel("Numero di giocatori (nuova partita)");
        Spinner<Integer> numPlayersSpinner = new Spinner<>(2, 5, 2);
        numPlayersSpinner.setMaxWidth(100);
        numPlayersSpinner.setStyle("-fx-background-color: #2a2a4a;");

        // Game ID
        Label gameIdLabel = makeLabel("Game ID (per unirsi a partita esistente)");
        TextField gameIdField = makeTextField("es. 1");

        // Bottoni principali
        Button createButton = new Button("Crea Partita");
        Button joinButton = new Button("Unisciti");
        styleButton(createButton, "#e0a830", "#1a1a2e");
        styleButton(joinButton, "#2a2a4a", "#e0a830");

        // Messaggio di stato
        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#aaaaaa"));
        messageLabel.setWrapText(true);

        createButton.setOnAction(e -> {
            this.nickname = nicknameField.getText().trim();
            String ip = ipField.getText().trim();
            this.numPlayers = numPlayersSpinner.getValue();
            boolean isRmi = rmiButton.isSelected();

            if (nickname.isEmpty()) { showStatusError("Inserisci un nickname."); return; }

            new Thread(() -> {
                try {
                    connectClient(nickname, ip, isRmi);
                    client.createGame(nickname, numPlayers);
                    showStatusOk("Richiesta di creazione inviata...");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        joinButton.setOnAction(e -> {
            nickname = nicknameField.getText().trim();
            String ip = ipField.getText().trim();
            String gameIdText = gameIdField.getText().trim();
            boolean isRmi = rmiButton.isSelected();

            if (nickname.isEmpty()) { showStatusError("Inserisci un nickname."); return; }
            if (gameIdText.isEmpty()) { showStatusError("Inserisci un Game ID."); return; }

            new Thread(() -> {
                try {
                    int gameId = Integer.parseInt(gameIdText);
                    connectClient(nickname, ip, isRmi);
                    client.joinGame(nickname, gameId);
                    showStatusOk("Richiesta di join inviata...");
                } catch (NumberFormatException ex) {
                    showStatusError("Game ID non valido.");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        HBox buttonBox = new HBox(20, createButton, joinButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                new Separator(),
                nicknameLabel, nicknameField,
                ipLabel, ipField,
                protocolLabel, protocolBox,
                new Separator(),
                gamesLabel, gamesListArea, refreshButton,
                new Separator(),
                numPlayersLabel, numPlayersSpinner,
                gameIdLabel, gameIdField,
                buttonBox,
                messageLabel
        );

        Scene scene = new Scene(root, 500, 800);
        primaryStage.setScene(scene);
    }

    private void showGameScreen(Board board, List<Player> players) {
        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // --- Parte Superiore: Info Turno ---
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        Label turnLabel = new Label("In attesa del turno...");
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // --- Parte Centrale: La Board (Griglia) ---
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        // Esempio: disegna una griglia vuota per ora
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button cell = new Button();
                cell.setPrefSize(60, 60);
                cell.setStyle("-fx-background-color: #2a2a4a; -fx-border-color: #e0a830;");
                boardGrid.add(cell, i, j);
            }
        }
        gameRoot.setCenter(boardGrid);

        // --- Parte Destra: Lista Giocatori ---
        VBox sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        sideBar.getChildren().add(makeLabel("GIOCATORI:\n"));

        sideBar.getChildren().add(makeLabel("---STATS---\n"));
        for (Player p : players) {
                sideBar.getChildren().add(makeLabel("player: "+ p.getNickname() +"has:\n"));
                sideBar.getChildren().add(makeLabel("Food: " + p.getFood() + ", Prestige: " + p.getPrestige() + ", Your cards: \n"));
                List<Card> everyCard = new ArrayList<>();
                everyCard.addAll(p.getArtists());
                everyCard.addAll(p.getBuilders());
                everyCard.addAll(p.getHarvesters());
                everyCard.addAll(p.getHunters());
                everyCard.addAll(p.getShamans());
                everyCard.addAll(p.getInventors());
                everyCard.addAll(p.getBuildings());
                gameRoot.setRight(sideBar);

        }

        // --- CAMBIO SCENA ---
        Scene gameScene = new Scene(gameRoot, 1000, 700); // Più grande della lobby
        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen(); // Opzionale, per centrare la nuova finestra
    }

    private void connectClient(String nickname, String ip, boolean isRmi) throws Exception {
        if (client != null) return;

        if (isRmi) {
            RmiClient rmiClient = new RmiClient(this, nickname);
            rmiClient.startConnection(ip, 1099);
            this.client = rmiClient;
        } else {
            SocketClient socketClient = new SocketClient(nickname);
            socketClient.setUserInterface(this);
            socketClient.startConnection(ip, 5000);
            Thread.sleep(500); // attendi che la connessione sia stabilita
            this.client = socketClient;
        }
    }

    // --- helpers UI ---

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    private TextField makeTextField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setMaxWidth(400);
        f.setStyle(
                "-fx-background-color: #2a2a4a;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #666688;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 8;"
        );
        return f;
    }

    private RadioButton makeRadio(String text, ToggleGroup group, boolean selected) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(group);
        rb.setSelected(selected);
        rb.setTextFill(Color.WHITE);
        return rb;
    }

    private void styleButton(Button button, String bg, String text) {
        button.setStyle(
                "-fx-background-color: " + bg + ";" +
                        "-fx-text-fill: " + text + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setOpacity(0.85));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }

    private void showStatusError(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.RED);
        });
    }

    private void showStatusOk(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.web("#aaaaaa"));
        });
    }

    // --- metodi di ui ---

    @Override
    public void updateBoard(Board board, List<Player> players) {
        Platform.runLater(() -> {

                showGameScreen(board,players); // Cambia la scena

            // Qui aggiornerai i componenti della board (es. pedine, carte)
            //updateUIElements(board, players);
        });
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
            // da implementare nella schermata di gioco
        });
    }

    @Override
    public void notifyEndGame(List<String> rankings) {
        Platform.runLater(() -> {
            // da implementare nella schermata di gioco
        });
    }

    @Override
    public void showMessage(String message) {
        System.out.println("DEBUG showMessage: " + message); // aggiungi questa riga
        Platform.runLater(() -> {
            if (message.startsWith("Available games:") || message.contains("Game #") || message.contains("Game ID")) {
                if (gamesListArea != null) {
                    gamesListArea.setText(message);
                }
            } else {
                if (messageLabel != null) {
                    messageLabel.setText(message);
                    messageLabel.setTextFill(Color.web("#aaaaaa"));
                }
            }
        });
    }
}