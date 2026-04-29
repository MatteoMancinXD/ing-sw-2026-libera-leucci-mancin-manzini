package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForHarvestersBuilding extends BuildingCard {

    public PrestigeForHarvestersBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForHarvestersBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int harvesters = player.getHarvesters().size();
        player.editPrestige(4 * harvesters);
    }

    @Override
    public String getShortString() {
        return String.format("Prestige For Harvesters [cost=%d, pp=%d] - +4 pp per harvester at game end", getFoodCost(), getPrestigeGain());
    }
}
