package it.polimi.ingsw.network.snapshots;

import java.io.Serializable;

/**
 * Snapshot containing the info of the card
 * @param id: card id
 * @param desc: text description of the card
 */
public record CardSnapshot(
        int id,
        String desc
) implements Serializable {}
