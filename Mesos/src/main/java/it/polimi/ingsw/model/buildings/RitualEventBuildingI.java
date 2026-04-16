package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventBuildingI extends BuildingCard {

    public RitualEventBuildingI() {
        super(1,5,2);
    }

    @Override
    public boolean getRitualEventNoPrestigeMalus() {return true;}
}
