package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class PaintingsEventBuilding extends BuildingCard {

    public PaintingsEventBuilding() {super(2, 5, 6);}

    @Override
    public int getPaintingsEventFoodBonus(int artists){return artists;} //it gets 1 food per artist in PaintingsEvent
}
