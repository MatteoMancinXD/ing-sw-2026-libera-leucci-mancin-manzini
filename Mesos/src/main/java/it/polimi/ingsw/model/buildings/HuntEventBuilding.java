package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class HuntEventBuilding extends BuildingCard {

    public HuntEventBuilding(int id,int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain); // era 2, foodCost 7, prestigeGain 2
    }


    //During HuntEvent gives an extra +1 prestige +1 food for each Hunter
    @Override
    public int getHuntEventFoodBonus(int hunters){return hunters;}

}