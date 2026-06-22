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
     * Shows the LobbyView (graphics) with inputs for nickname, communication protocol and serverIP
     */
    public void show() {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Title
        Text title = new Text("MESOS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));


        // Nickname
        Label nicknameLabel = makeLabel("Nickname");
        TextField nicknameField = makeTextField("Inserisci il tuo nickname"); //promptText

        // IP Server
        //you set this string as default text: "127.0.0.1", then if you enter a blank string the promptText reminds it you again
        Label ipLabel = makeLabel("IP Server");
        TextField ipField = makeTextField("127.0.0.1");
        ipField.setText("127.0.0.1");

        // RMI or Socket
        Label protocolLabel = makeLabel("Protocollo");
        ToggleGroup protocolGroup = new ToggleGroup();
        RadioButton rmiButton    = makeRadio("RMI",    protocolGroup, true); //true as default
        RadioButton socketButton = makeRadio("Socket", protocolGroup, false);
        HBox protocolBox = new HBox(20, rmiButton, socketButton);
        protocolBox.setAlignment(Pos.CENTER);

        // connection button
        Button connectButton = new Button("Connetti");
        styleButton(connectButton, "#e0a830", "#1a1a2e");

        // state message
        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#aaaaaa"));
        messageLabel.setWrapText(true); //line break true

        connectButton.setOnAction(e -> {
            String nick   = nicknameField.getText().trim(); //trim() removes blank spaces at the beginning and at the end of a string
            String ip     = ipField.getText().trim();
            boolean isRmi = rmiButton.isSelected();

            if (nick.isEmpty()) { showStatusError("Inserisci un nickname."); return; }
            if (ip.isEmpty())   { showStatusError("Inserisci l'IP del server."); return; }

            new Thread(() -> {
                try {
                    manager.connectClient(nick, ip, isRmi);
                    Platform.runLater(() -> {
                        // uses the javafx thread to put in cue: gameSetup's showing
                        Platform.runLater(() -> {
                            GameSetup gameSetup = new GameSetup(stage, manager); //gameSetup is a private var in guiManager (dividing logic from graphic)
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


    /**
     * implements error text as a red colored label
     * @param msg message
     */
    public void showStatusError(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.RED);
        });
    }

    /**
     * "converts" the text from GuiManager (that came from server) in a Label (graphic element).
     * This choice was implemented to divide the graphics from the logic
     * @param msg message
     */
    public void showStatusOk(String msg) {
        Platform.runLater(() -> {
            messageLabel.setText(msg);
            messageLabel.setTextFill(Color.web("#aaaaaa"));
        });
    }



    //helper methods to avoid code duplication

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    private TextField makeTextField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt); //suggested text that you see before writing inside a TextField
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
        rb.setToggleGroup(group); //makes a group (in each group only one element can be selected at the same time)
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
        button.setOnMouseEntered(e -> button.setOpacity(0.85)); //changes button color on click start
        button.setOnMouseExited(e ->  button.setOpacity(1.0)); //changes button color on click end
    }
}