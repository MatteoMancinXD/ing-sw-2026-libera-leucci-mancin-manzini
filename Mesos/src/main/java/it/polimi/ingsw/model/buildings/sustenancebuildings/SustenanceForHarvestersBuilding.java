package it.polimi.ingsw.model.buildings.sustenancebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class SustenanceForHarvestersBuilding extends BuildingCard {
    public  SustenanceForHarvestersBuilding(int id,int era,int foodCost,int PrestigeGain){
        super(id,era,foodCost,PrestigeGain);
    }
    public SustenanceForHarvestersBuilding(){}

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getHarvesters().size();
    }

    @Override
    public String getShortString() {
        return String.format("Sustenance For Harvesters [cost=%d, pp=%d] - -1 food per harvester during Sustenance Event", getFoodCost(), getPrestigeGain());
    }
}
