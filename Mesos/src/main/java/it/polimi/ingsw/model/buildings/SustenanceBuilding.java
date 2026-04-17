package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import java.util.Set;
import java.util.EnumSet;
//first try not finished not working
public class SustenanceBuilding extends BuildingCard {

    private static final Set<Character> ALLOWED_CHARACTERS = EnumSet.of(
            Character.ARTIST,
            Character.HARVESTER,
            Character.INVENTOR
    );
    private final Character targetCharacter;


    SustenanceBuilding(int id,int era,int  foodCost,int prestigeGain,Character targetCharacter){
        super(id,era,foodCost,prestigeGain);

        if (!ALLOWED_CHARACTERS.contains(targetCharacter)) {
            throw new IllegalArgumentException(
                    "Error: " + targetCharacter + " not a valid Character for SustenanceBuilding!"
            );
        }
        this.targetCharacter=targetCharacter;
    }


    @Override
    public Character getSustenanceEventFoodBonus(Character c){


    }

}
