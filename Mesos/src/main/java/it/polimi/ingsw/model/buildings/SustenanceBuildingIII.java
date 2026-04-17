package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingIII extends BuildingCard {
    SustenanceBuildingIII(int id,int era,int foodCost,int prestigeGain){super(id,era,foodCost,prestigeGain);}


    @Override
    public int getSustenanceEventInventorsFoodBonus(int inventors){return inventors;}
}

