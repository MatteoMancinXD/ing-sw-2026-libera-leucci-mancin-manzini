package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.db.DatabaseManagerDAO;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private final DatabaseManagerDAO databaseManagerDAO;

    public EndGameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
        this.databaseManagerDAO = DatabaseManagerDAO.getInstance();
    }

    /**
     * Shows this game's rankings along with historical rankings
     * @param rankings: current game's results
     * @param globalLeaderboard: historical results
     */
    public void show(List<String> rankings, List<LeaderboardEntryBean> globalLeaderboard) {
        int numPlayers = rankings.size();

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");


        // Titolo
        Text title = new Text("GAME OVER");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 40));
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

        VBox rankingBox = new VBox(8);
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

        //historical ranking (from DB)
        Separator separator = new Separator();
        separator.setMaxWidth(300);

        Label globalRankingTitle = new Label("Classifica Globale Storica (" + numPlayers + " giocatori):");
        globalRankingTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        globalRankingTitle.setTextFill(Color.web("#e0a830"));

        VBox globalRankingBox = new VBox(6);
        globalRankingBox.setAlignment(Pos.TOP_CENTER);
        globalRankingBox.setPadding(new Insets(10));



        if (globalLeaderboard.isEmpty()) {
            Label emptyLabel = new Label("No historical data");
            emptyLabel.setFont(Font.font("Georgia", 12));
            emptyLabel.setTextFill(Color.GRAY);
            globalRankingBox.getChildren().add(emptyLabel);
        } else {

            for (int i = 0; i < globalLeaderboard.size(); i++) {
                LeaderboardEntryBean entry = globalLeaderboard.get(i);


                String globalEntryText = (i + 1) + ". " + entry.getNickname() + " — Punti Totali: " + entry.getScore();

                Label globalRankLabel = new Label(globalEntryText);
                globalRankLabel.setFont(Font.font("Georgia", 13));
                globalRankLabel.setTextFill(Color.web("#d1d1e0"));
                globalRankingBox.getChildren().add(globalRankLabel);
            }
        }

        //historical ranking's scrollpane
        ScrollPane globalScrollPane = new ScrollPane(globalRankingBox);
        globalScrollPane.setFitToWidth(true);
        globalScrollPane.setPrefHeight(250);
        globalScrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e; -fx-control-inner-background: #1a1a2e;");



        root.getChildren().addAll(
                title,
                resultText,
                rankingTitle,
                rankingBox,
                separator,
                globalRankingTitle,
                globalScrollPane
        );




        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");


        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}