package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

import java.util.HashSet;

public class PrestigeForCharacterBuilding extends BuildingCard {

    private Character character;
    private int bonusPrestige;


    public PrestigeForCharacterBuilding(int id, int era, int foodCost, int prestigeGain, Character character, int bonusPrestige) {
        super(id,era,foodCost,prestigeGain);

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

    }

}
