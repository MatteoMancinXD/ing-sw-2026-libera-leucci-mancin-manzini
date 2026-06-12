package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.OrderTile;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;

/**
 * Snapshot of the state of the board
 * @param upperRow: cards in the upper row of the board
 * @param lowerRow: cards in the lower row of the bord
 * @param track: tiles composing the offer track
 * @param order: order tile corresponding to the number of players
 */
public record BoardSnapshot(
        List<CardSnapshot> upperRow,
        List<CardSnapshot> lowerRow,
        List<TileSnapshot> track,
        OrderTileSnapshot order
) implements Serializable {}
