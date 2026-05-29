package it.polimi.ingsw.network.snapshots;

import it.polimi.ingsw.model.Totem;

import java.io.Serializable;
import java.util.List;

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