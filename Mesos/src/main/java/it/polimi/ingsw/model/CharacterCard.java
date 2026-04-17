package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;

public abstract class CharacterCard extends TribeCard{
    private final Character type;

    public CharacterCard(int id, int era, int minPlayers, Character type) {
        super(id, era, minPlayers);
        this.type = type;
    }

    public Character getType() {
        return type;
    }
}
