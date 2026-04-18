package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

import java.util.HashSet;

public class PrestigeForCharacterBuilding extends BuildingCard {
    private CharacterCard characterCard;

    //it works also if two different players draw the cards, because it s a static set, and because it s hash it can contain nothing
    private static final HashSet<Character> TAKEN_CHARACTERS = new HashSet<>();

    public PrestigeForCharacterBuilding(int id, int era, int foodCost, int prestigeGain,CharacterCard card) {
        super(id,era,foodCost,prestigeGain);


        //checks if the character is already taken (you can declare 1 building per character)
        if (TAKEN_CHARACTERS.contains(characterCard.getType())) {
            throw new IllegalStateException("Sustenance building already exists for" + characterCard.getType());
        }
        TAKEN_CHARACTERS.add(characterCard.getType()); //if it wasn't taken add it to the taken list

        this.characterCard=card;

    }

    //operative methods
    public void setCharacterCard(CharacterCard characterCard) {this.characterCard=characterCard;}
    public CharacterCard getCharacterCard() { return this.characterCard; }

    @Override
    public void onGameEnd(Player player){

        int deckSize = player.getCharacterDeck(characterCard.getType()).size();

        switch(characterCard.getType()){
            case INVENTOR ->  player.editPrestige(deckSize*2);
            case HUNTER ->   player.editPrestige(deckSize*3);
            case HARVESTER,ARTIST,SHAMAN,BUILDER ->   player.editPrestige(deckSize*4);

        }


        //on end game after editing the prestige, TAKEN_CHARACTERS is cleaned for the next game
        TAKEN_CHARACTERS.clear();




    }
}
