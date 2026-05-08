package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameView {

    private final Stage stage;
    private final GuiManager manager;
    private List<String> playersNicknames = new ArrayList<>();

    private VBox sideBar;
    private ChatView chatView;

    public GameView(Stage stage, GuiManager manager) {
        this.stage = stage;
        this.manager = manager;
    }

    public void show(Board board, List<Player> players) {

        playersNicknames.clear();
        for (Player p : players) {
            playersNicknames.add(p.getNickname());
        }

        int numPlayers = players.size();
        int upperRowSize = board.getUpperRow().size();
        int lowerRowSize = board.getLowerRow().size();
        int trackSize = board.getTrack().size();




        BorderPane gameRoot = new BorderPane();
        gameRoot.setStyle("-fx-background-color: #1a1a2e;");

        // Top bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        Label turnLabel = new Label("In attesa del turno...");
        turnLabel.setTextFill(Color.web("#e0a830"));
        topBar.getChildren().add(turnLabel);
        gameRoot.setTop(topBar);

        // Centro — griglia board
        VBox table = new VBox();
        table.setPadding(new Insets(15));

        HBox upperRow = new HBox(upperRowSize);
        for(int i = 0; i < upperRowSize; i++) {
            Button card = new Button();

            int id = board.getUpperRow().get(i).getId();
            String imgPath = String.format("/assets/cards/front/front_%03d.png", id);
            Image img = new Image(imgPath);
            ImageView imgView = new ImageView(img);

            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);
            card.setGraphic(imgView);

            upperRow.getChildren().add(card);
        }
        HBox track =  new HBox(trackSize + 1);
        HBox lowerRow = new HBox(lowerRowSize);

        table.getChildren().addAll(upperRow, track, lowerRow);

        gameRoot.setCenter(table);

        // -------------------------------------------------------
        // Destra: BorderPane — stats in alto (scroll), chat in basso (fissa)
        // -------------------------------------------------------
        BorderPane rightPanel = new BorderPane();
        rightPanel.setPrefWidth(220);
        rightPanel.setStyle("-fx-background-color: #1a1a2e;");

        // Stats
        sideBar = new VBox(10);
        sideBar.setPadding(new Insets(15));
        sideBar.getChildren().add(makeLabel("PLAYERS:"));
        sideBar.getChildren().add(makeLabel("---STATS---"));

        for (Player p : players) {
            if (p.getNickname().equals(manager.getNickName())) {
                sideBar.getChildren().add(makeLabel("Player: " + p.getNickname() + " (YOU)"));
            } else {
                sideBar.getChildren().add(makeLabel("Player: " + p.getNickname()));
            }
            sideBar.getChildren().add(makeLabel("Food: " + p.getFood() + "  Prestige: " + p.getPrestige()));
            sideBar.getChildren().add(makeLabel("Artists: "    + p.getArtists().size()));
            sideBar.getChildren().add(makeLabel("Builders: "   + p.getBuilders().size()));
            sideBar.getChildren().add(makeLabel("Harvesters: " + p.getHarvesters().size()));
            sideBar.getChildren().add(makeLabel("Hunters: "    + p.getHunters().size()));
            sideBar.getChildren().add(makeLabel("Shamans: "    + p.getShamans().size()));
            sideBar.getChildren().add(makeLabel("Inventors: "  + p.getInventors().size()));
            sideBar.getChildren().add(makeLabel("Buildings: "  + p.getBuildings().size()));
            sideBar.getChildren().add(new Separator());
        }

        // ScrollPane sulle stats — la chat non viene mai spinta fuori
        ScrollPane statsScroll = new ScrollPane(sideBar);
        statsScroll.setFitToWidth(true);
        statsScroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        // Chat con altezza fissa, sempre visibile in fondo
        chatView = new ChatView(manager, "Game Chat");
        VBox chatContainer = chatView.getRoot();
        chatContainer.setPrefHeight(280);
        chatContainer.setMaxHeight(280);

        rightPanel.setCenter(statsScroll);    // stats crescono/scrollano
        rightPanel.setBottom(chatContainer);  // chat sempre in basso

        gameRoot.setRight(rightPanel);

        Scene gameScene = new Scene(gameRoot, 1100, 700);
        stage.setScene(gameScene);
        stage.centerOnScreen();
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    public boolean tryShowChatMessage(String message) {

        if (chatView == null || playersNicknames.isEmpty()) return false;

        // Controlla se il messaggio inizia con "nickname: "
        for (String nick : playersNicknames) {
            if (message.startsWith(nick + ": ")) {
                String text = message.substring(nick.length() + 2); // removes "nickname: "
                if(nick.equals(manager.getNickName())) return true; //if you sent the message don't reprint your message again
                chatView.appendMessage(nick, text); //if the message comes from someone else then print it
                return true;
            }
        }
        return false;
    }

}