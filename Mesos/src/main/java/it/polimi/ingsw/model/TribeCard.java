package it.polimi.ingsw.model;

public abstract class TribeCard extends Card{
    private final int minPlayers;

    public TribeCard(int id, int era, int minPlayers) {
        super(id, era);
        this.minPlayers = minPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
}
