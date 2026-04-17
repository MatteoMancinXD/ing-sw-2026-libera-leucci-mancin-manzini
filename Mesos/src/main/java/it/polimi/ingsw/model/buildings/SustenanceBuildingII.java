package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingII extends BuildingCard {
    SustenanceBuildingII(int id,int era,int  foodCost,int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }


    @Override
    public int getSustenanceEventHarvestersFoodBonus(int harvesters){return harvesters;}
}
