package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;

public class PrestigeForSextetBuilding extends BuildingCard {
    public PrestigeForSextetBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }

    @Override
    public void onGameEnd(Player player) {
        int i;
       /* int numArtists = player.getCharacterDeck(Character.ARTIST).size();
        int numShamans = player.getCharacterDeck(Character.SHAMAN).size();
        int numHarvesters =  player.getCharacterDeck(Character.HARVESTER).size();
        int numInventors = player.getCharacterDeck(Character.INVENTOR).size();
        int numHunters = player.getCharacterDeck(Character.HUNTER).size();
        int numBuilders = player.getCharacterDeck(Character.BUILDER).size();*/

        //find the smallest deckSize
            int min = 0; //initializes min to a number (just to avoid compilation error)
            boolean firstCycle = true;
            for(Character c : Character.values()) {

                if (firstCycle) {
                    min = player.getCharacterDeck(c).size(); //than here it s set to the first card's deck value
                    firstCycle = false;
                }
                if (player.getCharacterDeck(c).size() < min) {
                    min = player.getCharacterDeck(c).size();
                }
            }
            //if the minimum alongside getCharacterDeck(card.getType()) is 2, it means the players has at least the amount of 2 card foreach type
            for(i=0;i<min;i++){
                player.editPrestige(6);
            }
        }



    }



