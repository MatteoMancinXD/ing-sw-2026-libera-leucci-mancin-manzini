package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class PrestigeForArtistsBuilding extends BuildingCard {
    public PrestigeForArtistsBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public PrestigeForArtistsBuilding() {}

    @Override
    public void onGameEnd(Player player) {
        int artists = player.getArtists().size();
        player.editPrestige(4 * artists);
    }
}
