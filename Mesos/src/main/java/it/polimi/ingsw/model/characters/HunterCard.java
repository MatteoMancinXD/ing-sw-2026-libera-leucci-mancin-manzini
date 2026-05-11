package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;

public class HunterCard extends CharacterCard {
    private boolean getsFood;

    public HunterCard(int id, int era, int minPlayers, boolean getsFood) {
        super(id, era, minPlayers);
        this.getsFood = getsFood;
    }
    public HunterCard() {}

    public void setGetsFood(boolean getsFood) {
        this.getsFood = getsFood;
    }

    public boolean getGetsFood() {
        return getsFood;
    }

    @Override
    public void assignTo(Player player) {
        if(getsFood){
            int numHunters = player.getHunters().size();
            player.editFood(numHunters);
        }
        player.addHunter(this);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementHunters();
    }

    @Override
    public String getShortString() {
        String food = "noFoodBonus";
        if (getsFood) {food = "FoodBous";}
        return String.format("Hunter: era=%d, %s", getEra(), food);
    }
}
