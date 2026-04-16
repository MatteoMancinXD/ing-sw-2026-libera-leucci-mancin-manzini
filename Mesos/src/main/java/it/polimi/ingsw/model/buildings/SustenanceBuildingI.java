package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingI extends BuildingCard{
    SustenanceBuildingI(){super(1, 5, 3);}

    @Override
    public int getSustenanceEventArtistsFoodBonus(int artists){return artists;}
}
