package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingIII extends BuildingCard {
    SustenanceBuildingIII(){super(2,7,4);}

    @Override
    public int getSustenanceEventInventorsFoodBonus(int inventors){return inventors;}
}

