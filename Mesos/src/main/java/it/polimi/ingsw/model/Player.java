package it.polimi.ingsw;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Player {

    private String nickname;
    private int food;
    private int prestige;
    private ArrayList<Card> cards;
    private int totStars;

    public Player(String nickname) {
        this.nickname = nickname;
        this.food = 0;
        this.prestige = 0;
        this.cards = new ArrayList<>();
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
        cards.add(c);
    }
    public void buyBuilding(BuildingCard building){
        editFood(building.getCost());
        cards.add(building);
    }

    public void editFood(int amount) throws IllegalArgumentException{
        if (amount > food) {
            throw new IllegalArgumentException("Food cannot go below zero!");
        }
        this.food = this.food + amount;
    }

    public void editPrestige(int amount) {
        this.prestige = this.prestige + amount;
    }

}