package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a player within the game.
 * This class acts as a data entity in the Model, maintaining the player's current state.
 * It keeps track of personal information, available resources (food), prestige points,
 * accumulated shaman stars, and the collection of cards they have acquired during the game.
 *
 * @author Matteo Mancin
 */
public class Player {

    private String nickname;
    private int food;
    private int prestige;
    //private ArrayList<Card> cards;
    private ArrayList<ArtistCard> artists;
    private ArrayList<BuilderCard> builders;
    private ArrayList<HarvesterCard> harvesters;
    private ArrayList<HunterCard> hunters;
    private ArrayList<InventorCard> inventors;
    private ArrayList<ShamanCard> shamans;
    private ArrayList<BuildingCard> buildings;

    private int totStars;

    public Player(String nickname) {
        this.nickname = nickname;
        this.food = 0;
        this.prestige = 0;
        this.artists = new ArrayList<>();
        this.builders = new ArrayList<>();
        this.harvesters = new ArrayList<>();
        this.hunters = new ArrayList<>();
        this.inventors = new ArrayList<>();
        this.shamans = new ArrayList<>();


        this.totStars = 0;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setFood(int food) {
        this.food = food;
    }
    public int getFood() {
        return food;
    }
    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }
    public int getPrestige() {
        return prestige;
    }

    public void setTotStars(int totStars) {
        this.totStars = totStars;
    }
    public int getTotStars() {
        return totStars;
    }

    public void drawCard(Card c){ //da vedere se usare liste diverse per ogni caracheter o quantomeno dei contatori

        c.assignTo(this);

    }

    public void addArtist(ArtistCard c) { artists.add(c); }
    public void addBuilder(BuilderCard c) { builders.add(c); }
    public void addHarvester(HarvesterCard c) { harvesters.add(c); }
    public void addHunter(HunterCard c) { hunters.add(c); }
    public void addInventor(InventorCard c) { inventors.add(c); }
    public void addShaman(ShamanCard c) { shamans.add(c); }
    //public void addBuilding(BuildingCard c) {buildings.add(c); }

    //prova

    public void buyBuilding(BuildingCard building){
        editFood(building.getCost());
        buildings.add(building);
    }

    public void editFood(int amount) throws IllegalArgumentException{
        if (amount + food < 0) {
            throw new IllegalArgumentException("Food cannot go below zero!");
        }
        this.food = this.food + amount;
    }

    public void editPrestige(int amount) {
        this.prestige = this.prestige + amount;
    }

}