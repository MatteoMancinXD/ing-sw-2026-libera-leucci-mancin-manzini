package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventBuildingIII extends BuildingCard {

    public RitualEventBuildingIII(){
        super(2,6,4);
    }

    //it can't work like this because in this way you could add 3 stars each
    public int getRitualEventBonusStars(){
        return 3;
    }

}
