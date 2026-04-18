package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventNoMalusBuilding extends BuildingCard {

    public RitualEventNoMalusBuilding(int id,int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }


    @Override
    public boolean getRitualEventNoPrestigeMalus() {return true;}
}
