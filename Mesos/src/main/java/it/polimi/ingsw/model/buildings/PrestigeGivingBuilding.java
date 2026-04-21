package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;

public class PrestigeGivingBuilding extends BuildingCard {
    public PrestigeGivingBuilding(int id, int era, int foodCost, int prestigeGain) {
            super(id,era,foodCost,prestigeGain);
        }
        //no override on any methods, this building just gives 25 prestige points
    public PrestigeGivingBuilding () {}

}
