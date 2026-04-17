package it.polimi.ingsw.model;

public abstract class Card {
    private int id;
    private int era;

    public Card(int id, int era) {
        this.id = id;
        this.era = era;
    }


    protected Card() {}

    public void setEra(int era) {
        this.era = era;
    }
    public int getEra() {
        return era;
    }

    public int getId() {return id;}

    public void assignTo(Player player) {}
}
