package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.GamePhase;

import java.io.Serializable;
import java.util.List;

public record GameSnapshot(
        int round,
        int era,

        int numPlayers,
        int currentPlayerIndex,
        List<PlayerSnapshot> players,

        GamePhase phase,
        BoardSnapshot board
) implements Serializable {}
