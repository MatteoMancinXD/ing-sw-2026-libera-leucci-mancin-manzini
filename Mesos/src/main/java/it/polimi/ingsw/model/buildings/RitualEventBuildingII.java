package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventBuildingII extends BuildingCard {

    public RitualEventBuildingII(int id,int era, int foodCost, int prestigeGain) {

        super(id,era,foodCost,prestigeGain);
    }

    @Override
    public int getRitualEventDoublePrestigeBonus() { return 2;}
}
