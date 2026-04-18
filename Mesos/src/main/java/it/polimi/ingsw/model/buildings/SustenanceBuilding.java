package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;

import java.util.EnumSet;
import java.util.Set;
import java.util.HashSet;


public class SustenanceBuilding extends BuildingCard{

    private Character character;


    //these collections are needed for checks

    //there are 3 sustenance building each of these work with just one Character type
    private static final Set<Character> ALLOWED_CHARACTERS = EnumSet.of(
                Character.ARTIST,
                Character.HARVESTER,
                Character.INVENTOR
    );
    //it works also if two different players draw the cards, because it s a static set, and because it s hash it can contain nothing
    private static final HashSet<Character> TAKEN_CHARACTERS = new HashSet<>();



    //constructor
    SustenanceBuilding(int id, int era, int  foodCost, int prestigeGain, Character character) {
        super(id,era,foodCost,prestigeGain);


        //checks if the character is allowed
        if(!ALLOWED_CHARACTERS.contains(character)){
            throw new IllegalArgumentException("Error: " + character + " not a valid Character for SustenanceBuilding");
        }
        //checks if the character is already taken (you can declare 1 building per character)
        if (TAKEN_CHARACTERS.contains(character)) {
            throw new IllegalStateException("Sustenance building already exists for" + character);
        }
        TAKEN_CHARACTERS.add(character); //if it wasn't taken add it to the taken list




        this.character=character;
    }

    //operative methods
    public void setCharacter(Character character) {this.character=character;}
    public Character getCharacter() { return this.character; }

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getCharacterDeck(this.character).size();
    }


    //check method
    //MUST BE USED ON endGame()
    public static void resetTakenSustenanceBuildingCharactersSet() {
        TAKEN_CHARACTERS.clear();
    }

}
