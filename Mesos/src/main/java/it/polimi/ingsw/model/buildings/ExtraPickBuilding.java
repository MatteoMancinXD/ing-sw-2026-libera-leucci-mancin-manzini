package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class ExtraPickBuilding extends BuildingCard {

    private boolean extraPickAvailable;

    public ExtraPickBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain); // era 3, foodCost 9, prestigeGain 3
        this.extraPickAvailable = false;
    }


    @Override
    public void onRoundEnd(Player player) {
        this.extraPickAvailable = true;
    }


    public boolean isExtraPickAvailable() {
        return extraPickAvailable;
    }


    public void useExtraPick() {
        this.extraPickAvailable = false;
    }
}
