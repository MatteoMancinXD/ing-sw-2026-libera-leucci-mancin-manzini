package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class PaintingsEventBuilding extends BuildingCard {

    public PaintingsEventBuilding(int id,int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }


    @Override
    public int getPaintingsEventFoodBonus(int artists){return artists;} //it gets 1 food per artist in PaintingsEvent
}
