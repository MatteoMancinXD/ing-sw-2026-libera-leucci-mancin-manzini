package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.characters.BuilderCard;

public class PrestigeDoublingBuilderValueBuilding extends BuildingCard {
    public PrestigeDoublingBuilderValueBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }

    public PrestigeDoublingBuilderValueBuilding() {}

    @Override
    public void onGameEnd(Player player){
        int builderPps;
        int numBuilders = player.getCharacterDeck(Character.BUILDER).size();
        BuilderCard builderCard = new BuilderCard();

        /*
        instead of doubling the builder's prestige points it just adds to player's
        total prestige points the total amount of points given by all the builders
        */
        for(CharacterCard c : player.getCharacterDeck(Character.BUILDER)){
            builderCard = (BuilderCard) c;
            builderPps = builderCard.getPps();
            player.editPrestige(builderPps);

        }


    }
}
