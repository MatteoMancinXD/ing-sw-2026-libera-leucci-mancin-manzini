package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForHuntersBuilding extends BuildingCard {
    public PrestigeForHuntersBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForHuntersBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int hunters = player.getHunters().size();
        player.editPrestige(3 * hunters);
    }

    @Override
    public String getShortString() {
        return String.format("Prestige For Hunters [cost=%d, pp=%d] - +3 pp per hunter at game end", getFoodCost(), getPrestigeGain());
    }
}
