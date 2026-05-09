package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Tile;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Objects;

/**
 * TrackView builds the HBox containing the totem placement card and all track tiles.
 */
public class TrackView {

    private static final double TILE_HEIGHT = 150;
    private static final String TILE_STYLE  = "-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;";

    /**
     * @param tiles      list of tiles from the board
     * @param numPlayers number of players (determines which totem card to show)
     * @return HBox containing totem card + all tile buttons
     */
    public static HBox build(List<Tile> tiles, int numPlayers) {
        HBox totemAndTrack = new HBox(tiles.size() + 2);
        totemAndTrack.setPadding(new Insets(30, 0, 30, 50));

        // --- Totem placement card ---
        addTotemCard(totemAndTrack, numPlayers);

        // --- Track tiles ---
        for (Tile tile : tiles) {
            addTileCard(totemAndTrack, tile.getLetter());
        }

        return totemAndTrack;
    }

    //  Private helpers

    private static void addTotemCard(HBox container, int numPlayers) {
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

        addImageButton(container, path);
    }

    private static void addTileCard(HBox container, char letter) {
        String path = "/assets/board/tiles/tile_" + letter + ".png";

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

        addImageButton(container, path);
    }

    private static void addImageButton(HBox container, String path) {
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
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + path);
        }
    }
}