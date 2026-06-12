package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;
import java.util.List;

/**
 * Snapshot of the current state of the order tile
 * @param modifiers: list of food/prestige modifiers for each slot
 * @param players: list of players currently sitting on the order tile
 */
public record OrderTileSnapshot(
       int[] modifiers,
       List<PlayerSnapshot> players
) implements Serializable {}
