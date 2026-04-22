package it.polimi.ingsw.model.buildings.sustenancebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class SustenanceForHarvestersBuilding extends BuildingCard {
    public  SustenanceForHarvestersBuilding(int id,int era,int foodCost,int PrestigeGain){
        super(id,era,foodCost,PrestigeGain);
    }
    public SustenanceForHarvestersBuilding(){}

    @Override
    public int getSustenanceEventHarvestersFoodBonus(Player p){
        return p.getHarvesters().size();
    }
}
