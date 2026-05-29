package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;
import java.util.List;

public record OrderTileSnapshot(
       int[] modifiers,
       List<PlayerSnapshot> players
) implements Serializable {}
