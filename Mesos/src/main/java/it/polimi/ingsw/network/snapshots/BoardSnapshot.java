package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.OrderTile;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;

public record BoardSnapshot(
        List<CardSnapshot> upperRow,
        List<CardSnapshot> lowerRow,
        List<TileSnapshot> track,
        OrderTileSnapshot order
) implements Serializable {}
