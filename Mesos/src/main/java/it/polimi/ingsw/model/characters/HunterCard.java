package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;

public class HunterCard extends CharacterCard {
    private final boolean getsFood;

    public HunterCard(int era, int minPlayers, Character type, boolean getsFood) {
        super(era, minPlayers, type);
        this.getsFood = getsFood;
    }

    public boolean GetsFood() {
        return getsFood;
    }
}
