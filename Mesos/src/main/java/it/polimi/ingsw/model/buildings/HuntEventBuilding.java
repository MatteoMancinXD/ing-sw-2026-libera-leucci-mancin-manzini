package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class HuntEventBuilding extends BuildingCard {

    public HuntEventBuilding() {
        super(2, 7, 2); // era, foodCost, prestigeGain
    }

    //During HuntEvent gives an extra +1 prestige +1 food for each Hunter
    @Override
    public int getHuntEventFoodBonus(int hunters){return hunters;}

}