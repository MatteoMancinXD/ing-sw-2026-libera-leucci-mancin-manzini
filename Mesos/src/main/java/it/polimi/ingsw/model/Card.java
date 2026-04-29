package it.polimi.ingsw.model;

import java.io.Serializable;

public abstract class Card implements Serializable {
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

    public void notifyBuildings(Player player) {};

    public int getFoodCost() { return 0; }

    public abstract String getShortString();
}
