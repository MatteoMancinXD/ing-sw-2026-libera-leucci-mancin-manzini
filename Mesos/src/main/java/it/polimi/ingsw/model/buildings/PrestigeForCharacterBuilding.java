package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

import java.util.HashSet;

public class PrestigeForCharacterBuilding extends BuildingCard {

    private Character character;
    private int bonusPrestige;

    //it works also if two different players draw the cards, because it s a static set, and because it s hash it can contain nothing
    private static final HashSet<Character> TAKEN_CHARACTERS = new HashSet<>();

    public PrestigeForCharacterBuilding(int id, int era, int foodCost, int prestigeGain, Character character, int bonusPrestige) {
        super(id,era,foodCost,prestigeGain);

        //checks if the character is already taken (you can declare 1 building per character)
        if (TAKEN_CHARACTERS.contains(character)) {
            throw new IllegalStateException("Sustenance building already exists for" + character);
        }
        TAKEN_CHARACTERS.add(character); //if it wasn't taken add it to the taken list

        this.character=character;
        this.bonusPrestige=bonusPrestige;
    }

    public PrestigeForCharacterBuilding() {}

    public void setCharacter(Character character) {this.character=character;}
    public Character getCharacter() { return this.character; }

    public void setBonusPrestige(int bonusPrestige) {this.bonusPrestige=bonusPrestige;}

    @Override
    public void onGameEnd(Player player){

        player.editPrestige(this.bonusPrestige*(player.getCharacterDeck(this.character).size()));


        //on game end after editing prestige,it clears TAKEN_CHARACTERS to free the buildings for next game
        TAKEN_CHARACTERS.clear();
    }
}
