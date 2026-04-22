package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForInventorsBuilding extends BuildingCard {

    public PrestigeForInventorsBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForInventorsBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int inventors =  player.getInventors().size();
        player.editPrestige(2 * inventors);
    }
}
