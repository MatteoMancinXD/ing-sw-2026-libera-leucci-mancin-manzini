package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

import java.util.EnumSet;
import java.util.Set;



public class SustenanceBuilding extends BuildingCard{

    public SustenanceBuilding(int id, int era, int  foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }

    public SustenanceBuilding () {}
}
