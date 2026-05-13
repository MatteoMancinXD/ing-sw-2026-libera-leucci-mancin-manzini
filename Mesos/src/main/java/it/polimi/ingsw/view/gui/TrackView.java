package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.OrderTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;



import java.util.List;
import java.util.Objects;

/**
 * TrackView builds the HBox containing the totem placement card and all track tiles.
 */
public class TrackView {

    private static final double TILE_HEIGHT = 150;
    private static final String TILE_STYLE  = "-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;";
    private static final double TOTEM_HEIGHT = 45;
    private static final double TOTEM_OFFX = 34;
    private static final double PLACE_HEIGHT = 20;

    private static final Map<String, String> playerColors = new HashMap<>();
    private static final String[] COLORS = {"#e0a830", "#00ff88", "#ff4444", "#44aaff", "#ff44ff"};

    private static final Map<Integer, Integer> order_offset_y = Map.ofEntries(
            Map.entry(2, 10),
            Map.entry(3, 0),
            Map.entry(4, 0),
            Map.entry(5, 0)
    );

    /**
     * @param tiles      list of tiles from the board
     * @param numPlayers number of players (determines which totem card to show)
     * @return HBox containing totem card + all tile buttons
     */
    public static HBox build(OrderTile order, List<Tile> tiles, int numPlayers, GuiManager manager, boolean enabled) {
        HBox totemAndTrack = new HBox(tiles.size() + 2);
        totemAndTrack.setPadding(new Insets(30, 0, 30, 50));

        // --- Totem placement card ---
        addTotemCard(totemAndTrack, order, numPlayers);

        // --- Track tiles ---
        for (Tile tile : tiles) {
            int idx = tiles.indexOf(tile);
            addTileCard(totemAndTrack, tile, idx, manager, enabled);        }

        return totemAndTrack;
    }

    //  Private helpers

    private static void addTotemCard(HBox container, OrderTile order, int numPlayers) {
        String path = "/assets/board/order/order_" + numPlayers + ".png";

        /*
        switch (numPlayers) {
            case 2 -> { path = "/assets/board/rear/rear_1.png";   viewport = new Rectangle2D(1065, 100, 330, 480); }
            case 3 -> { path = "/assets/board/front/front_1.png"; viewport = new Rectangle2D(50,   58, 140, 226); }
            case 4 -> { path = "/assets/board/rear/rear_0.png";   viewport = new Rectangle2D(1065, 100, 330, 480); }
            case 5 -> { path = "/assets/board/front/front_0.png"; viewport = new Rectangle2D(50,   58, 140, 226); }
            default -> { return; }
        }
        */

        try {
            Image sheet = new Image(Objects.requireNonNull(
                    TrackView.class.getResourceAsStream(path)));
            ImageView imgView = new ImageView(sheet);

            imgView.setFitHeight(TILE_HEIGHT);
            imgView.setPreserveRatio(true);

            StackPane contentStack = new StackPane();
            contentStack.getChildren().add(imgView);

            Pane overlayPane = new Pane();
            double offset_y = order_offset_y.get(numPlayers);

            for(int i = 0; i < order.getPlayers().size(); i++) {
                Player p = order.getPlayers().get(i);
                if(p == null) { continue; }

                String totemPath = "/assets/totems/totem_" + p.getTotem().toString() + ".png";
                Image totemSheet = new Image(Objects.requireNonNull(TrackView.class.getResourceAsStream(totemPath)));
                ImageView totemView = new ImageView(totemSheet);

                totemView.setFitHeight(TOTEM_HEIGHT);
                totemView.setPreserveRatio(true);

                overlayPane.getChildren().add(totemView);
                totemView.setLayoutX(TOTEM_OFFX);
                totemView.setLayoutY(offset_y + i * PLACE_HEIGHT);
            }

            contentStack.getChildren().add(overlayPane);

            Button btn = new Button();

            btn.setGraphic(contentStack);
            btn.setStyle(TILE_STYLE);

            container.getChildren().add(btn);
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + path);
            e.printStackTrace();
        }
    }

    private static void addTileCard(HBox container, Tile tile, int index, GuiManager manager, boolean enabled) {
        String path = "/assets/board/tiles/tile_" + tile.getLetter() + ".png";

        /*
        switch (letter) {
            case 'A' -> viewport = new Rectangle2D(465,  65,  330, 480);
            case 'B' -> viewport = new Rectangle2D(760,  65,  330, 480);
            case 'C' -> viewport = new Rectangle2D(1060, 65,  330, 480);
            case 'D' -> viewport = new Rectangle2D(80,   590, 330, 480);
            case 'E' -> viewport = new Rectangle2D(470,  590, 330, 480);
            case 'F' -> viewport = new Rectangle2D(760,  590, 330, 480);
            case 'G' -> viewport = new Rectangle2D(1065, 590, 330, 480);
            default  -> { return; }
        }
        */
        try {
            Image sheet = new Image(Objects.requireNonNull(
                    TrackView.class.getResourceAsStream(path)));
            ImageView imgView = new ImageView(sheet);

            imgView.setFitHeight(TILE_HEIGHT);
            imgView.setPreserveRatio(true);

            Button btn = new Button();

            StackPane contentStack = new StackPane();
            contentStack.getChildren().add(imgView);

            if(tile.getStatus()) {
                String totemPath = "/assets/totems/totem_" + tile.getPlayer().getTotem().toString() + ".png";
                Image totemSheet = new Image(Objects.requireNonNull(TrackView.class.getResourceAsStream(totemPath)));
                ImageView totemView = new ImageView(totemSheet);

                totemView.setFitHeight(TOTEM_HEIGHT);
                totemView.setPreserveRatio(true);

                Pane overlayPane = new Pane();
                overlayPane.getChildren().add(totemView);
                totemView.setLayoutX(TOTEM_OFFX);
                totemView.setLayoutY(0);

                contentStack.getChildren().add(overlayPane);
            }

            btn.setDisable(!enabled);

            btn.setOnAction(e -> {
                try { manager.placeTotem(index); }
                catch (Exception ex) { System.err.println("Errore totem: " + ex.getMessage()); }
            });

            btn.setGraphic(contentStack);
            btn.setStyle(TILE_STYLE);

            container.getChildren().add(btn);
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + path);

        }

        /*
        if(btn != null) {
            btn.setDisable(!enabled);

            if(tile.getStatus()) {
                String color = playerColor(tile.getPlayer().getNickname());
                btn.setStyle("-fx-border-color: " + color + "; -fx-border-width: 4; -fx-border-radius: 4; -fx-background-color: transparent; -fx-padding: 0;");
            }

            btn.setOnAction(e -> {
                try { manager.placeTotem(index); }
                catch (Exception ex) { System.err.println("Errore totem: " + ex.getMessage()); }
            });
        }
        */

        /*
        if (btn != null) btn.setDisable(!enabled);
        if (btn != null && tile.getStatus()) {
            String color = playerColor(tile.getPlayer().getNickname());
            btn.setStyle("-fx-border-color: " + color + "; -fx-border-width: 4; -fx-border-radius: 4; -fx-background-color: transparent; -fx-padding: 0;");
        }
        if (btn != null) btn.setOnAction(e -> {
            try { manager.placeTotem(index); }
            catch (Exception ex) { System.err.println("Errore totem: " + ex.getMessage()); }
        });
         */
    }

    private static Button addImageButton(HBox container, String path) {
        try {
            Image sheet = new Image(Objects.requireNonNull(
                    TrackView.class.getResourceAsStream(path)));
            ImageView imgView = new ImageView(sheet);

            imgView.setFitHeight(TILE_HEIGHT);
            imgView.setPreserveRatio(true);

            Button btn = new Button();
            btn.setGraphic(imgView);
            btn.setStyle(TILE_STYLE);

            container.getChildren().add(btn);
            return btn;
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + path);
            return null;
        }
    }
    private static String playerColor(String nickname) {
        if (!playerColors.containsKey(nickname)) {
            int index = playerColors.size() % COLORS.length;
            playerColors.put(nickname, COLORS[index]);
        }
        return playerColors.get(nickname);
    }
}