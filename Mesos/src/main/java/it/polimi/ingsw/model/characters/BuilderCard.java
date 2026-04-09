package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;

public class BuilderCard extends CharacterCard {
    private final int discount;
    private final int pps;

    public BuilderCard(int era, int minPlayers, Character type, int pps, int discount) {
        super(era, minPlayers, type);
        this.pps = pps;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public int getPps() {
        return pps;
    }
}
