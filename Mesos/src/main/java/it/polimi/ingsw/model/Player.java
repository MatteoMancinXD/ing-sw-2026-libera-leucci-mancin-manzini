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
    //private ArrayList<Card> cards;
    private Map<Character, List<CharacterCard>> characters;
    private ArrayList<BuildingCard> buildings;

    private int totStars;

    public Player(String nickname) {
        this.nickname = nickname;
        this.food = 0;
        this.prestige = 0;
        this.characters = new HashMap<>();

        for (Character character : Character.values()) {
            this.characters.put(character, new ArrayList<>());
        }

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

        if (c instanceof BuildingCard) {         //Quando avremo la classe BuildingCard mettere assignTo per evitare questo instanceof
            BuildingCard bc = (BuildingCard) c;
            this.buyBuilding(bc);
        }
        else {
            c.assignTo(this);
        }

    }

    public void addArtist(ArtistCard c) { characters.get(Character.ARTIST).add(c); }
    public void addBuilder(BuilderCard c) { characters.get(Character.BUILDER).add(c); }
    public void addHarvester(HarvesterCard c) { characters.get(Character.HARVESTER).add(c); }
    public void addHunter(HunterCard c) { characters.get(Character.HUNTER).add(c); }
    public void addInventor(InventorCard c) { characters.get(Character.INVENTOR).add(c); }
    public void addShaman(ShamanCard c) { characters.get(Character.SHAMAN).add(c); }
    //public void addBuilding(BuildingCard c) {buildings.add(c); }

    @SuppressWarnings("unchecked")
    public <T extends CharacterCard> List<T> getCharacterDeck (Character c) {
        //return (List<T>) characters.getOrDefault(c, new ArrayList<>());
        return (List<T>) characters.get(c);
    }

    public ArrayList<BuildingCard> getBuildings(){
        return buildings; 
    }

    
    //prova

    public void buyBuilding(BuildingCard building){
        editFood(-building.getBuildingFoodCost());
        buildings.add(building);
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

}
