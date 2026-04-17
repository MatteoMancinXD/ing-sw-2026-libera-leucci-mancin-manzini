package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class SustenanceBuildingI extends BuildingCard{
    SustenanceBuildingI(int id,int era,int  foodCost,int prestigeGain){
        super(id,era,foodCost,prestigeGain);
    }


    @Override
    public int getSustenanceEventArtistsFoodBonus(int artists){return artists;}
}
