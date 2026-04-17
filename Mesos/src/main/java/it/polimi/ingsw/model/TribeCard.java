package it.polimi.ingsw.model;

public abstract class TribeCard extends Card{
    private final int minPlayers;

    public TribeCard(int era, int minPlayers) {
        super(era);
        this.minPlayers = minPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
}
