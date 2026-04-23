package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;

import java.util.*;

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

    private List<HunterCard> hunters;
    private List<BuilderCard> builders;
    private List<HarvesterCard> harvesters;
    private List<ArtistCard> artists;
    private List<InventorCard> inventors;
    private List<ShamanCard> shamans;

    private ArrayList<BuildingCard> buildings;

    private int totStars;

    public Player(String nickname) {
        this.nickname = nickname;
        this.food = 0;
        this.prestige = 0;

        this.hunters = new ArrayList<>();
        this.builders = new ArrayList<>();
        this.harvesters = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.inventors = new ArrayList<>();
        this.shamans = new ArrayList<>();

        this.buildings = new ArrayList<>();

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

    public void drawCard(Card c){
        c.assignTo(this);
    }

    public void addArtist(ArtistCard c) { artists.add(c); }
    public void addBuilder(BuilderCard c) { builders.add(c); }
    public void addHarvester(HarvesterCard c) { harvesters.add(c); }
    public void addHunter(HunterCard c) { hunters.add(c); }
    public void addInventor(InventorCard c) { inventors.add(c); }
    public void addShaman(ShamanCard c) { shamans.add(c); }
    public void addBuilding(BuildingCard c) { buildings.add(c); }

    public List<HunterCard> getHunters() { return new ArrayList<>(hunters); }
    public List<BuilderCard> getBuilders() { return new ArrayList<>(builders); }
    public List<HarvesterCard> getHarvesters() { return new ArrayList<>(harvesters); }
    public List<ArtistCard> getArtists() { return new ArrayList<>(artists); }
    public List<InventorCard> getInventors() { return new ArrayList<>(inventors); }
    public List<ShamanCard> getShamans() { return new ArrayList<>(shamans); }

    public List<BuildingCard> getBuildings(){ return new ArrayList<>(buildings); }

    public void buyBuilding(BuildingCard building){
        int cost = building.getFoodCost();
        int discount = 0;
        for(BuilderCard c : this.builders){
            discount += c.getDiscount();
        }

        // if discount <= cost the player loses less food, otherwise doesn't pay any
        if(discount <= cost){
            editFood(-(cost - discount));
        }

        building.onPurchase(this);
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

    public void editStars(int amount) {
        this.totStars = this.totStars + amount;
    }

}
