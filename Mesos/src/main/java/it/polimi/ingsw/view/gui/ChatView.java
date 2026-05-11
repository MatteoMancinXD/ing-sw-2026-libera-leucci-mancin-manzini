
package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatView {

    private final GuiManager manager;
    private final String title;

    //ui
    private final VBox root;
    private final VBox messagesBox;
    private final ScrollPane scrollPane;  // scroll automatico verso il basso
    private final TextField inputField;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    //constructor
    public ChatView(GuiManager manager, String title) {
        this.manager = manager;
        this.title = title;

        // --- Titolo ---
        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.web("#e0a830"));
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 14));

        // --- Area messaggi ---
        messagesBox = new VBox(4);
        messagesBox.setPadding(new Insets(8));

        scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle(
                "-fx-background: #12122a;" +
                        "-fx-background-color: #12122a;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 4;"
        );
        // Scroll automatico verso il basso quando arriva un nuovo messaggio
        messagesBox.heightProperty().addListener(
                (obs, oldVal, newVal) -> scrollPane.setVvalue(1.0)
        );

        // --- Input + bottone invio ---
        inputField = new TextField();
        inputField.setPromptText("Scrivi un messaggio...");
        inputField.setStyle(
                "-fx-background-color: #2a2a4a;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #666688;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 6;"
        );

        Button sendButton = new Button("Invia");
        sendButton.setStyle(
                "-fx-background-color: #e0a830;" +
                        "-fx-text-fill: #1a1a2e;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 6 14;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );

        // Invio con tasto Enter oppure clic sul bottone
        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleSend();
        });
        sendButton.setOnAction(e -> handleSend());

        HBox inputRow = new HBox(8, inputField, sendButton);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        // --- Root ---
        root = new VBox(6, titleLabel, scrollPane, inputRow);
        root.setPadding(new Insets(10));
        root.setStyle(
                "-fx-background-color: #1a1a2e;" +
                        "-fx-border-color: #e0a830;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
    }

    // ------------------------------------------------------------------ //
    //  API pubblica                                                        //
    // ------------------------------------------------------------------ //

    /**
     * Restituisce il nodo radice da embeddare nel layout della scena padre.
     * Esempio:
     *   sideBar.getChildren().add(chatView.getRoot());
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Aggiunge un messaggio ricevuto dal server alla chat.
     * Chiamato da GuiManager quando arriva un messaggio.
     * Thread-safe: usa Platform.runLater() internamente.
     *
     * @param from    nickname del mittente
     * @param message testo del messaggio
     */
    public void appendMessage(String from, String message) {
        Platform.runLater(() -> {
            String time = LocalTime.now().format(TIME_FMT);

            // Timestamp
            Text timeText = new Text("[" + time + "] ");
            timeText.setFill(Color.web("#888888"));
            timeText.setFont(Font.font(11));

            // Nickname mittente
            Text nameText = new Text(from + ": ");
            nameText.setFill(Color.web("#e0a830"));
            nameText.setFont(Font.font(null, FontWeight.BOLD, 12));

            // Testo messaggio
            Text msgText = new Text(message);
            msgText.setFill(Color.WHITE);
            msgText.setFont(Font.font(12));

            TextFlow row = new TextFlow(timeText, nameText, msgText);
            row.setPrefWidth(Double.MAX_VALUE);

            messagesBox.getChildren().add(row);
        });
    }

    /**
     * Overload: accetta una stringa già formattata "NICKNAME: messaggio"
     * utile se il server manda i messaggi già in quel formato.
     */
    public void appendMessage(String rawMessage) {
        Platform.runLater(() -> {
            String time = LocalTime.now().format(TIME_FMT);

            Text timeText = new Text("[" + time + "] ");
            timeText.setFill(Color.web("#888888"));

            Text msgText = new Text(rawMessage);
            msgText.setFill(Color.WHITE);
            msgText.setFont(Font.font(12));

            TextFlow row = new TextFlow(timeText, msgText);
            messagesBox.getChildren().add(row);
        });
    }

    // ------------------------------------------------------------------ //
    //  Invio messaggio                                                     //
    // ------------------------------------------------------------------ //

    private void handleSend() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        inputField.clear();

        new Thread(() -> {
            try {
                manager.sendChatMessage(text);
                // Mostra subito il tuo messaggio localmente senza aspettare il server
                appendMessage("You", text);
            } catch (Exception ex) {
                Text sysText = new Text("Errore invio: " + ex.getMessage());
            }
        }).start();
    }
}
