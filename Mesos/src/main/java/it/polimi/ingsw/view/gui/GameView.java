package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import java.util.Objects;

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

        // totem placing tile
        HBox totemAndTrack = new HBox(trackSize+2); // Spacing
        //totemPlacer.setAlignment(Pos.CENTER_LEFT);
        totemAndTrack.setPadding(new Insets(30, 0, 30, 50));

        String path = null;
        Rectangle2D viewport = null;

        switch (numPlayers) {
            case 2 -> {
                path = "/assets/board/rear/rear_1.png";
                viewport = new Rectangle2D(1070, 100, 330, 480);
            }
            case 4 -> {
                path = "/assets/board/rear/rear_0.png";
                viewport = new Rectangle2D(1070, 100, 330, 480);
            }
            case 3 -> {
                path = "/assets/board/front/front_1.png";
                viewport = new Rectangle2D(70, 100, 330, 480);
            }
            case 5 -> {
                path = "/assets/board/front/front_0.png";
                viewport = new Rectangle2D(70, 100, 330, 480);
            }
        }

        if (path != null) {
            try {
                Image sheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                ImageView totemCard = new ImageView(sheet);
                totemCard.setViewport(viewport);
                totemCard.setFitHeight(150);
                totemCard.setPreserveRatio(true);

                Button totemButton = new Button();
                totemButton.setGraphic(totemCard);



                //bottone trasparente no animazioni
                //totemButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

                totemAndTrack.getChildren().add(totemButton);
            } catch (Exception e) {
                System.err.println("Errore nel caricamento dell'immagine: " + path);
            }
        }

     //   HBox track =  new HBox(trackSize + 1);

        //track.setAlignment(Pos.CENTER);
        //totemPlacer.setPadding(new Insets(30, 0, 30, 50));

        for(int i = 0; i < trackSize; i++) {
            Button card = new Button();
            String tilePath = null;
            Rectangle2D tileViewport = null;

            char letter = board.getTrack().get(i).getLetter();
                switch(letter) {
                    case 'A' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(450, 65, 330, 480);
                    }
                    case 'B' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(760, 65, 330, 480);
                    }
                    case 'C' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(1060, 65, 330, 480);
                    }
                    case 'D' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(65,  590, 330, 480);
                    }
                    case 'E' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(460, 590, 330, 480);
                    }
                    case 'F' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(760, 590, 330, 480);
                    }
                    case 'G' -> {
                        tilePath = "/assets/board/front/front_0.png";
                        tileViewport = new Rectangle2D(1075, 590, 330, 480);
                    }
                }

            if (tilePath != null) {
                try {
                    Image sheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream(tilePath)));
                    ImageView tileCard = new ImageView(sheet);
                    tileCard.setViewport(tileViewport);
                    tileCard.setFitHeight(150);
                    tileCard.setPreserveRatio(true);

                    Button tileButton = new Button();
                    tileButton.setGraphic(tileCard);



                    //bottone trasparente no animazioni
                    //tileButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

                    totemAndTrack.getChildren().add(tileButton);
                } catch (Exception e) {
                    System.err.println("Errore nel caricamento dell'immagine: " + path);
                }
            }
        }


        HBox lowerRow = new HBox(lowerRowSize);

        for(int i = 0; i < lowerRowSize; i++) {
            Button card = new Button();

            int id = board.getLowerRow().get(i).getId();
            String imgPath = String.format("/assets/cards/front/front_%03d.png", id);
            Image img = new Image(imgPath);
            ImageView imgView = new ImageView(img);

            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);
            card.setGraphic(imgView);

            lowerRow.getChildren().add(card);
        }

        table.getChildren().addAll(upperRow,totemAndTrack, lowerRow);

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