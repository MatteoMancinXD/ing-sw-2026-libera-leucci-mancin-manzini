package it.polimi.ingsw.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * EndGameView — schermata di fine partita.
 * Mostra se hai vinto o perso e la classifica finale.
 */
public class EndGameView {

    private final Stage stage;
    private final GuiManager manager;

    public EndGameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(List<String> rankings) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Titolo
        Text title = new Text("GAME OVER");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        title.setFill(Color.web("#e0a830"));

        // Hai vinto o perso?
        String myNick = manager.getNickName();
        boolean won = !rankings.isEmpty() && rankings.get(0).contains(myNick);

        Text resultText = new Text(won ? "HAI VINTO! 🏆" : "HAI PERSO");
        resultText.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        resultText.setFill(won ? Color.web("#00ff88") : Color.web("#ff4444"));

        // Classifica
        Label rankingTitle = new Label("Classifica finale:");
        rankingTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        rankingTitle.setTextFill(Color.web("#e0a830"));

        VBox rankingBox = new VBox(10);
        rankingBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < rankings.size(); i++) {
            String entry = (i + 1) + ".  " + rankings.get(i);
            Label rankLabel = new Label(entry);
            rankLabel.setFont(Font.font("Georgia", 16));

            if (i == 0) {
                rankLabel.setTextFill(Color.web("#e0a830"));
            } else if (i == 1) {
                rankLabel.setTextFill(Color.web("#aaaaaa"));
            } else {
                rankLabel.setTextFill(Color.WHITE);
            }

            rankingBox.getChildren().add(rankLabel);
        }

        root.getChildren().addAll(
                title,
                resultText,
                new Separator(),
                rankingTitle,
                rankingBox
        );

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}