package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;

public record CardSnapshot(
        int id,
        String desc
) implements Serializable {}
