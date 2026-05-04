package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.ui;

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

/**
 * LobbyView implements the ui of the Lobby.
 * Its constructor has a parameter manager of type GuiManager that allows to communicate with the network
 */
public class LobbyView {


    private final Stage stage;
    private final GuiManager manager;   // unico punto per azioni di rete

    //ui
    private Label messageLabel;
    private TextArea gamesListArea;

    //constructor
    public LobbyView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }


    //lobby scene
    public void show() {
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
        RadioButton rmiButton    = makeRadio("RMI",    protocolGroup, true);
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
        styleTextArea(gamesListArea);

        Button refreshButton = new Button("Aggiorna lista");
        styleButton(refreshButton, "#2a2a4a", "#e0a830");
        refreshButton.setOnAction(e -> {
            String ip       = ipField.getText().trim();
            String nick     = nicknameField.getText().trim();
            boolean isRmi   = rmiButton.isSelected();

            if (nick.isEmpty()) { showStatusError("Inserisci un nickname prima."); return; }

            new Thread(() -> {
                try {
                    manager.connectClient(nick, ip, isRmi); //manager allows to divide the network from the ui logic
                    manager.requestAvailableGames();
                    showStatusOk("Lista aggiornata.");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        // Numero giocatori (nuova partita)
        Label numPlayersLabel = makeLabel("Numero di giocatori (nuova partita)");
        Spinner<Integer> numPlayersSpinner = new Spinner<>(2, 5, 2);
        numPlayersSpinner.setMaxWidth(100);
        numPlayersSpinner.setStyle("-fx-background-color: #2a2a4a;");

        // Game ID (join)
        Label gameIdLabel = makeLabel("Game ID (per unirsi a partita esistente)");
        TextField gameIdField = makeTextField("es. 1");

        // Bottoni principali
        Button createButton = new Button("Crea Partita");
        Button joinButton   = new Button("Unisciti");
        styleButton(createButton, "#e0a830", "#1a1a2e");
        styleButton(joinButton,   "#2a2a4a", "#e0a830");

        // Messaggio di stato
        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#aaaaaa"));
        messageLabel.setWrapText(true);


        //buttons
        createButton.setOnAction(e -> {
            String nick    = nicknameField.getText().trim();
            String ip      = ipField.getText().trim();
            int numPlayers = numPlayersSpinner.getValue();
            boolean isRmi  = rmiButton.isSelected();

            if (nick.isEmpty()) { showStatusError("Inserisci un nickname."); return; }

            new Thread(() -> {
                try {
                    manager.connectClient(nick, ip, isRmi);
                    manager.createGame(nick, numPlayers);
                    showStatusOk("Richiesta di creazione inviata...");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        joinButton.setOnAction(e -> {
            String nick       = nicknameField.getText().trim();
            String ip         = ipField.getText().trim();
            String gameIdText = gameIdField.getText().trim();
            boolean isRmi     = rmiButton.isSelected();

            if (nick.isEmpty())       { showStatusError("Inserisci un nickname.");  return; }
            if (gameIdText.isEmpty()) { showStatusError("Inserisci un Game ID."); return; }

            new Thread(() -> {
                try {
                    int gameId = Integer.parseInt(gameIdText);
                    manager.connectClient(nick, ip, isRmi);
                    manager.joinGame(nick, gameId);
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
        stage.setScene(scene);
    }



    public void updateGamesList(String text) {
        Platform.runLater(() -> {
            if (gamesListArea != null) gamesListArea.setText(text);
        });
    }


    //ui

    public void showStatusError(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.RED);
        });
    }

    public void showStatusOk(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.web("#aaaaaa"));
        });
    }



    //ui helper

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
        button.setOnMouseExited(e ->  button.setOpacity(1.0));
    }

    private void styleTextArea(TextArea area) {
        area.setStyle(
                "-fx-background-color: #2a2a4a;" +
                        "-fx-text-fill: white;" +
                        "-fx-control-inner-background: #2a2a4a;" +
                        "-fx-prompt-text-fill: #666688;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;"
        );
    }
}