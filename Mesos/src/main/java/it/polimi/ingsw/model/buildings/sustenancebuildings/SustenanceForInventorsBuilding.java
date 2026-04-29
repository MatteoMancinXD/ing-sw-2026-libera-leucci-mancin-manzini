package it.polimi.ingsw.model.buildings.sustenancebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class SustenanceForInventorsBuilding extends BuildingCard {
    public SustenanceForInventorsBuilding(int id,int era,int foodCost,int prestigeGain){
        super(id,era,foodCost,prestigeGain);
    }
    public SustenanceForInventorsBuilding(){}

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getInventors().size();
    }

    @Override
    public String getShortString() {
        return String.format("Sustenance For Inventors [cost=%d, pp=%d] - -1 food per inventor during Sustenance Event", getFoodCost(), getPrestigeGain());
    }
}
