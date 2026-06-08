package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Totem;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TotemSelectView {
    private final Stage stage;
    private final GuiManager manager;
    private Label messageLabel;
    private Label totemsLabel;
    private TextArea totemsList;
    private Label errorLabel;
    private ComboBox<String> totemCombo;

    public TotemSelectView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(Set<Totem> totems) {
        VBox root = new VBox(14);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text title = new Text("MESOS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));

        messageLabel = makeLabel("It's only you");

        totemsLabel = makeLabel("Available totems:");
        totemsList = new TextArea();
        totemsList.setEditable(false);
        totemsList.setPrefHeight(115);
        totemsList.setPrefWidth(400);
        totemsList.setPromptText("Click \"Update\" to see available totems");
        styleTextArea(totemsList);

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(event -> {
            new Thread(() -> {
                try {
                    manager.requestAvailableTotems();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
        styleButton(updateBtn, "#2a2a4a", "#e0a830");

        Label selectLabel = makeLabel("Select totem:");
        totemCombo = new ComboBox<>();
        totemCombo.setPromptText("Select a totem...");
        totemCombo.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: white;");
        totemCombo.setPrefWidth(200);

        List<String> stringTotems = Arrays.stream(Totem.values()).map(Enum::name).toList();
        totemCombo.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: #2a2a4a;");
                }
            };
            return cell;
        });

        totemCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #2a2a4a;");
            }
        });

        errorLabel = new Label();

        Button selectBtn = new Button("Select");
        selectBtn.setOnAction(event -> {
            String selected = totemCombo.getValue();
            if (selected == null) { showStatusError("Seleziona un totem."); return; }
            final String finalTotem = selected.toUpperCase();
            if (stringTotems.contains(finalTotem)) {
                new Thread(() -> {
                    Platform.runLater(() -> {
                        WaitingView waitingView = new WaitingView(stage, manager);
                        manager.setWaitingView(waitingView);
                        waitingView.show();
                    });
                    manager.selectTotem(finalTotem);
                }).start();
            } else {
                showStatusError("Please enter a valid totem");
            }
        });
        styleButton(selectBtn,"#e0a830", "#2a2a4a");
        showTotems(totems);

        root.getChildren().addAll(
                title,
                new Separator(),
                messageLabel,
                totemsLabel, totemsList,
                updateBtn,
                new Separator(),
                selectLabel, totemCombo,
                selectBtn,
                errorLabel
        );

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

    public void showTotems(Set<Totem> totems) {
        totemsList.clear();
        totemCombo.getItems().clear();
        for (Totem t : totems) {
            totemCombo.getItems().add(t.toString());
        }

        for(Totem t: totems) {
            totemsList.appendText(t.toString() + "\n");
        }
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

    public void showStatusError(String msg) {
        Platform.runLater(() -> {
            errorLabel.setText(msg);
            errorLabel.setTextFill(Color.RED);
        });
    }
}
