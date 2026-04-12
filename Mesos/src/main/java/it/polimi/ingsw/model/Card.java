package it.polimi.ingsw.model;

public abstract class Card {
    private final int era;

    public Card(int era) {
        this.era = era;
    }

    public int getEra() {
        return era;
    }

    public void assignTo(Player player) {}
}
