package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Tile;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class TileView extends Pane {
    private static final double TILE_HEIGHT = 150;
    private static final String TILE_STYLE  = "-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;";

    public TileView(Tile t) {
        String tilePath = "/assets/tiles/tile_" + t.getLetter() + ".png";
        Image tileImage = new Image(Objects.requireNonNull(TileView.class.getResourceAsStream(tilePath)));

        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setLayoutX(0);
        tileImageView.setLayoutY(0);
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
}
