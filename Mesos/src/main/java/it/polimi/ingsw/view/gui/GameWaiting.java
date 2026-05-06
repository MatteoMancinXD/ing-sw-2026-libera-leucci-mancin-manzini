package it.polimi.ingsw.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWaiting {
    private final Stage stage;
    private final GuiManager manager;
    private Label messageLabel;

    public GameWaiting(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        messageLabel = makeLabel("It's only you");

        root.getChildren().add(messageLabel);

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
    }

    public void updateWaitingLabel(String message) {
        messageLabel.setText(message);
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }
}
