package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

public class PrestigeForCharacterBuilding extends BuildingCard {
    public PrestigeForCharacterBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }
    @Override
    public void onCharacterCardGameEnd(Player player, CharacterCard card){

        int deckSize = player.getCharacterDeck(card.getType()).size();

        switch(card.getType()){
            case INVENTOR ->  player.editPrestige(deckSize*2);
            case HUNTER ->   player.editPrestige(deckSize*3);
            case HARVESTER,ARTIST,SHAMAN,BUILDER ->   player.editPrestige(deckSize*4);

        }






    }
}
