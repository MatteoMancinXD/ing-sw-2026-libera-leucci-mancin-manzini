package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.Totem;

import java.io.Serializable;
import java.util.List;

/**
 * Snapshot of the current state of the player
 * @param nickname: player's nickname
 * @param food: food points
 * @param prestige: prestige points
 * @param totem: selected totem
 * @param hunters: list of owned hunter cards
 * @param builders: list of owned builder cards
 * @param harvesters: list of owned harvester cards
 * @param artists: list of owned artist cards
 * @param inventors: list of owned inventor cards
 * @param shamans: list of owned shaman cards
 * @param buildings: list of owned building cards
 * @param totStars: total owned shaman stars
 * @param totDiscount: total discount from builders
 */
public record PlayerSnapshot(
        String nickname,

        int food,
        int prestige,
        Totem totem,

        List<CardSnapshot> hunters,
        List<CardSnapshot> builders,
        List<CardSnapshot> harvesters,
        List<CardSnapshot> artists,
        List<CardSnapshot> inventors,
        List<CardSnapshot> shamans,

        List<CardSnapshot> buildings,

        int totStars,
        int totDiscount
) implements Serializable {}