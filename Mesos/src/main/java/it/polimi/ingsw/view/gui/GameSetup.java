package it.polimi.ingsw.view.gui;

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
 * GameSetup — seconda schermata.
 * Appare dopo la connessione al server.
 * Il giocatore può vedere le partite disponibili, crearne una nuova o unirsi a una esistente.
 */
public class GameSetup {

    private final Stage stage;
    private final GuiManager manager;
    private Label messageLabel;
    private TextArea gamesListArea;

    public GameSetup(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show() {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Titolo
        Text title = new Text("MESOS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));

        Text subtitle = new Text("Connesso come: " + manager.getNickName());
        subtitle.setFont(Font.font("Georgia", 14));
        subtitle.setFill(Color.web("#aaaaaa"));

        // Lista partite disponibili
        Label gamesLabel = makeLabel("Partite disponibili");
        gamesListArea = new TextArea();
        gamesListArea.setEditable(false);
        gamesListArea.setPrefHeight(100);
        gamesListArea.setMaxWidth(400);
        gamesListArea.setPromptText("Clicca 'Aggiorna' per vedere le partite disponibili");
        styleTextArea(gamesListArea);

        Button refreshButton = new Button("Aggiorna lista");
        styleButton(refreshButton, "#2a2a4a", "#e0a830");
        refreshButton.setOnAction(e -> {
            new Thread(() -> {
                try {
                    manager.requestAvailableGames();
                    showStatusOk("Lista aggiornata.");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        // Crea nuova partita
        Label numPlayersLabel = makeLabel("Numero di giocatori (nuova partita)");
        Spinner<Integer> numPlayersSpinner = new Spinner<>(2, 5, 2);
        numPlayersSpinner.setMaxWidth(100);
        numPlayersSpinner.setStyle("-fx-background-color: #2a2a4a;");

        Button createButton = new Button("Crea Partita");
        styleButton(createButton, "#e0a830", "#1a1a2e");

        createButton.setOnAction(e -> {
            int numPlayers = numPlayersSpinner.getValue();
            new Thread(() -> {
                try {
                    manager.createGame(manager.getNickName(), numPlayers);
                    showStatusOk("Richiesta di creazione inviata...");
                    Platform.runLater(() -> {
                        WaitingView gameWaiting = new WaitingView(stage, manager);
                        manager.setWaitingView(gameWaiting);
                        gameWaiting.show();
                    });
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        // Unisciti a partita esistente
        Label gameIdLabel = makeLabel("Game ID (per unirsi a partita esistente)");
        TextField gameIdField = makeTextField("es. 1");

        Button joinButton = new Button("Unisciti");
        styleButton(joinButton, "#2a2a4a", "#e0a830");

        joinButton.setOnAction(e -> {
            String gameIdText = gameIdField.getText().trim();
            if (gameIdText.isEmpty()) { showStatusError("Inserisci un Game ID."); return; }

            new Thread(() -> {
                try {
                    int gameId = Integer.parseInt(gameIdText);
                    manager.joinGame(manager.getNickName(), gameId);
                    showStatusOk("Richiesta di join inviata...");
                    Platform.runLater(() -> {
                        WaitingView gameWaiting = new WaitingView(stage, manager);
                        manager.setWaitingView(gameWaiting);
                        gameWaiting.show();
                    });
                } catch (NumberFormatException ex) {
                    showStatusError("Game ID non valido.");
                } catch (Exception ex) {
                    showStatusError("Errore: " + ex.getMessage());
                }
            }).start();
        });

        // Messaggio di stato
        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#aaaaaa"));
        messageLabel.setWrapText(true);

        HBox createBox = new HBox(20, numPlayersSpinner, createButton);
        createBox.setAlignment(Pos.CENTER);

        HBox joinBox = new HBox(20, gameIdField, joinButton);
        joinBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title, subtitle,
                new Separator(),
                gamesLabel, gamesListArea, refreshButton,
                new Separator(),
                numPlayersLabel, createBox,
                gameIdLabel, joinBox,
                messageLabel
        );

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
    }



    public void updateGamesList(String text) {
        Platform.runLater(() -> {
            if (gamesListArea != null) {
                if (text.startsWith("Available games:")) {
                    gamesListArea.clear();          // svuota solo quando inizia una nuova lista
                    gamesListArea.appendText(text + "\n");
                } else if (text.startsWith("Game #")) {
                    gamesListArea.appendText(text + "\n");  // aggiunge senza sovrascrivere (setText sovrascrive)
                }
            }
        });
    }

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

    // --- helpers ---

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    private TextField makeTextField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setMaxWidth(200);
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