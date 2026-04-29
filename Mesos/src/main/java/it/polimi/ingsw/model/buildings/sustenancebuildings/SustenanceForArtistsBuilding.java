package it.polimi.ingsw.model.buildings.sustenancebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.SustenanceBuilding;


public class SustenanceForArtistsBuilding extends BuildingCard {
    public SustenanceForArtistsBuilding(int id,int era,int foodCost,int prestigeGain){
        super(id,era,foodCost,prestigeGain);
    }
    public SustenanceForArtistsBuilding(){}

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getArtists().size();
    }

    @Override
    public String getShortString() {
        return String.format("Sustenance For Artists [cost=%d, pp=%d] - -1 food per artist during Sustenance Event", getFoodCost(), getPrestigeGain());
    }
}
