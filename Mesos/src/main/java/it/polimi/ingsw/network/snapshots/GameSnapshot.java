package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.GamePhase;

import java.io.Serializable;
import java.util.List;

/**
 * Snapshot of the current state of the game
 * @param round: current round
 * @param era: current era
 * @param numPlayers: number of total players
 * @param currentPlayerIndex: index of the current player in the player's list
 * @param players: list of current players
 * @param phase: current phase of the game
 * @param board: current state of the board
 */
public record GameSnapshot(
        int round,
        int era,

        int numPlayers,
        int currentPlayerIndex,
        List<PlayerSnapshot> players,

        GamePhase phase,
        BoardSnapshot board
) implements Serializable {}
