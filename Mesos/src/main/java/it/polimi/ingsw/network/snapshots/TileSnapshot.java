package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;

/**
 * Snapshot of the current state of the tile
 * @param minPlayers: minimum number of players to use the tile
 * @param letter: letter of the tile
 * @param upperRow: number of cards to be drawn from upper row
 * @param lowerRow: number of cards to be drawn from lower row
 * @param foodBonus: eventual food bonus given
 * @param status: true if the tile is occupied by a player, false otherwise
 * @param player: snapshot of the occupying player if tile is occupied, null otherwise
 */
public record TileSnapshot(
        int minPlayers,
        char letter,
        int upperRow,
        int lowerRow,

        int foodBonus,
        boolean status,
        PlayerSnapshot player
) implements Serializable {}
