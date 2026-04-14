package it.polimi.ingsw.model;

public abstract class Card {
    private final int id;
    private final int era;

    public Card(int id, int era) {
        this.id = id;
        this.era = era;
    }

    public int getEra() {
        return era;
    }

    public int getId() {return id;}

    public void assignTo(Player player) {}
}
