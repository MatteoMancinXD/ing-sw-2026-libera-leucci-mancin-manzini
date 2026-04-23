package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.BuilderCard;

public class PrestigeDoublingBuilderValueBuilding extends BuildingCard {
    public PrestigeDoublingBuilderValueBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }

    public PrestigeDoublingBuilderValueBuilding() {}

    @Override
    public void onGameEnd(Player player){
        for(BuilderCard c : player.getBuilders()){
            player.editPrestige(c.getPps());
        }
    }
}
