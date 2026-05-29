package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;

public record TileSnapshot(
        int minPlayers,
        char letter,
        int upperRow,
        int lowerRow,

        int foodBonus,
        boolean status,
        PlayerSnapshot player
) implements Serializable {}
