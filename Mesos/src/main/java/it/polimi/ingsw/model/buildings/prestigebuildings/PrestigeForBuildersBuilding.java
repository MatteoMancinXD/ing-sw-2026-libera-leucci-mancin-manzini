package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForBuildersBuilding extends BuildingCard {
    public PrestigeForBuildersBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForBuildersBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int builders = player.getBuilders().size();
        player.editPrestige(4 * builders);
    }

    @Override
    public String getShortString() {
        return String.format("Prestige For Builders [cost=%d, pp=%d] - +4 pp per builder at game end", getFoodCost(), getPrestigeGain());
    }
}
