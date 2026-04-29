package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;

public class RitualEventDoublePrestigeBuilding extends BuildingCard {

    public RitualEventDoublePrestigeBuilding(int id, int era, int foodCost, int prestigeGain) {

        super(id,era,foodCost,prestigeGain);
    }
    public RitualEventDoublePrestigeBuilding() {}

    @Override
    public int getRitualEventDoublePrestigeBonus() { return 2; }

    @Override
    public String getShortString() {
        return String.format("Ritual Event Double Prestige [cost=%d, pp=%d] - double pp if majority in Ritual Event", getFoodCost(), getPrestigeGain());
    }
}
