package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingII extends BuildingCard {
    SustenanceBuildingII(){super(1, 4, 4);}

    @Override
    public int getSustenanceEventHarvestersFoodBonus(int harvesters){return harvesters;}
}
