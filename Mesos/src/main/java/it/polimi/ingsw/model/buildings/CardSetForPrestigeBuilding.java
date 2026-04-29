package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

import java.util.stream.IntStream;

public class CardSetForPrestigeBuilding extends BuildingCard {
    public CardSetForPrestigeBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
    }

    public CardSetForPrestigeBuilding() {
    }

    @Override
    public void onGameEnd(Player player) {
        int numHunters = player.getHunters().size();
        int numBuilders = player.getBuilders().size();
        int numHarvesters = player.getHarvesters().size();
        int numArtists = player.getArtists().size();
        int numInventors = player.getInventors().size();
        int numShamans = player.getShamans().size();

        int min = IntStream.of(numHunters, numBuilders, numHarvesters, numArtists, numInventors, numShamans)
                .min().getAsInt();

        player.editPrestige(6 * min);
    }
    @Override
    public String getShortString() {
        return String.format("Card Set For Prestige [cost=%d, pp=%d] - +6 pp per complete set of 6 character types at game end", getFoodCost(), getPrestigeGain());
    }
}



