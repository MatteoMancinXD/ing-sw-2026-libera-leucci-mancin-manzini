/*package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;

import java.util.EnumSet;
import java.util.Set;


 //!!! BUILDING IS OBSOLETE !!!




public abstract class SustenanceBuilding extends BuildingCard{

    private Character character;


    //these collections are needed for checks

    //there are 3 sustenance building each of these work with just one Character type
    private static final Set<Character> ALLOWED_CHARACTERS = EnumSet.of(
                Character.ARTIST,
                Character.HARVESTER,
                Character.INVENTOR
    );




    //constructor
    public SustenanceBuilding(int id, int era, int  foodCost, int prestigeGain, Character character) {
        super(id,era,foodCost,prestigeGain);


        //checks if the character is allowed
        if(!ALLOWED_CHARACTERS.contains(character)){
            throw new IllegalArgumentException("Error: " + character + " not a valid Character for SustenanceBuilding");
        }

        this.character=character;
    }

    public SustenanceBuilding (){}

    //operative methods
    public void setCharacter(Character character) {this.character=character;}
    public Character getCharacter() { return this.character; }

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getCharacterDeck(this.character).size();

       /* return p.getHunters().size()+p.getBuilders().size()+p.getHarvesters().size()
                +p.getArtists().size()+p.getInventors().size()+p.getShamans().size();*/
    }*/




}
