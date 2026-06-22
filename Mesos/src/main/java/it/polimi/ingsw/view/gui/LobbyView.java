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
 *  LobbyView — first screen.
 *  The player enters a nickname, server IP, and chooses the protocol (rmi or socket)
 *  After clicking "Connect", GameSetup opens.
 *
 */
public class LobbyView {

    private final Stage stage;
    private final GuiManager manager;
    private Label messageLabel;

    public LobbyView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    /**
     * Shows the LobbyView with inputs for nickname, communication protocol and serverIP
     */
    public void show() {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
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

        // Bottone connetti
        Button connectButton = new Button("Connetti");
        styleButton(connectButton, "#e0a830", "#1a1a2e");

        // Messaggio di stato
        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#aaaaaa"));
        messageLabel.setWrapText(true);

        connectButton.setOnAction(e -> {
            String nick   = nicknameField.getText().trim();
            String ip     = ipField.getText().trim();
            boolean isRmi = rmiButton.isSelected();

            if (nick.isEmpty()) { showStatusError("Inserisci un nickname."); return; }
            if (ip.isEmpty())   { showStatusError("Inserisci l'IP del server."); return; }

            new Thread(() -> {
                try {
                    manager.connectClient(nick, ip, isRmi);
                    Platform.runLater(() -> {
                        // apre la schermata di setup partita
                        Platform.runLater(() -> {
                            GameSetup gameSetup = new GameSetup(stage, manager);
                            manager.setGameSetup(gameSetup);
                            gameSetup.show();
                        });
                    });
                } catch (Exception ex) {
                    showStatusError("Errore connessione: " + ex.getMessage());
                }
            }).start();
        });

        root.getChildren().addAll(
                title,
                new Separator(),
                nicknameLabel, nicknameField,
                ipLabel, ipField,
                protocolLabel, protocolBox,
                connectButton,
                messageLabel
        );

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
    }

    // Auxiliary methods

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
}