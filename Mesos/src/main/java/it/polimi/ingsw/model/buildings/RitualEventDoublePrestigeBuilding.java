package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventDoublePrestigeBuilding extends BuildingCard {

    public RitualEventDoublePrestigeBuilding(int id, int era, int foodCost, int prestigeGain) {

        super(id,era,foodCost,prestigeGain);
    }


    @Override
    public boolean getRitualEventDoublePrestigeBonus() { return true;}
}
