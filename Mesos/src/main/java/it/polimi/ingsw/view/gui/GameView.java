package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
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

import java.util.ArrayList;
import java.util.List;

public class GameView {

    private final Stage stage;
    private final GuiManager manager;   // unico punto per azioni di rete

    //ui
    private Label messageLabel;
    private TextArea gamesListArea;

    //constructor
    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }
    public void show(Board board, List<Player> players) {
        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar — turno
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        Label turnLabel = new Label("In attesa del turno...");
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // Centro — griglia board
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button cell = new Button();
                cell.setPrefSize(60, 60);
                cell.setStyle("-fx-background-color: #2a2a4a; -fx-border-color: #e0a830;");
                boardGrid.add(cell, i, j);
            }
        }
        gameRoot.setCenter(boardGrid);

        // Destra — stats giocatori
        VBox sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        sideBar.getChildren().add(makeLabel("GIOCATORI:"));
        sideBar.getChildren().add(makeLabel("---STATS---"));

        for (Player p : players) {
            sideBar.getChildren().add(makeLabel("Player: " + p.getNickname()));
            sideBar.getChildren().add(makeLabel("Food: " + p.getFood() + "  Prestige: " + p.getPrestige()));

            List<Card> everyCard = new ArrayList<>();
            everyCard.addAll(p.getArtists());
            everyCard.addAll(p.getBuilders());
            everyCard.addAll(p.getHarvesters());
            everyCard.addAll(p.getHunters());
            everyCard.addAll(p.getShamans());
            everyCard.addAll(p.getInventors());
            everyCard.addAll(p.getBuildings());
            sideBar.getChildren().add(makeLabel("Carte: " + everyCard.size()));
            sideBar.getChildren().add(new Separator());
        }
        gameRoot.setRight(sideBar);

        Scene gameScene = new Scene(gameRoot, 1000, 700);
        stage.setScene(gameScene);
        stage.centerOnScreen();
    }

    //helper ui

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }
}
