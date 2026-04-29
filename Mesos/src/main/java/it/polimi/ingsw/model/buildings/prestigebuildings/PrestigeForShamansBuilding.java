package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForShamansBuilding extends BuildingCard {
    public PrestigeForShamansBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForShamansBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int shamans = player.getShamans().size();
        player.editPrestige(4 * shamans);
    }
    @Override
    public String getShortString() {
        return String.format("Prestige For Shamans [cost=%d, pp=%d] - +4 pp per shaman at game end", getFoodCost(), getPrestigeGain());
    }
}
