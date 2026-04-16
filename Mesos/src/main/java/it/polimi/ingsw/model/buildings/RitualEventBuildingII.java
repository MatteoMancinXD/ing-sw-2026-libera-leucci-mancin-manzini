package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventBuildingII extends BuildingCard {

    public RitualEventBuildingII() {
        super(2,7,0);
    }

    @Override
    public int getRitualEventDoublePrestigeBonus() { return 2;}
}
