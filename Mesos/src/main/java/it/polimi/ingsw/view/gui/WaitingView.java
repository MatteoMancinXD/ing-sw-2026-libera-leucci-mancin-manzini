package it.polimi.ingsw.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaitingView {
    private final Stage stage;
    private final GuiManager manager;

    public WaitingView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show() {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text title = new Text("MESOS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));

        Label waitingLabel = makeLabel("Waiting for other players to select a totem...");

        root.getChildren().addAll(
                title,
                waitingLabel
        );

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

}
