package it.polimi.ingsw.model;

public abstract class CharacterCard extends TribeCard{
    private final Character type;

    public CharacterCard(int era, int minPlayers, Character type) {
        super(era, minPlayers);
        this.type = type;
    }

    public Character getType() {
        return type;
    }
}
