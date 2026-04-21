package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * This building gives the player 5 food each time they complete a set with each CharacterCard, since
 * the building was acquired.
 * It does so by being invoked by the controller each time a Round approaches its end.
 * It keeps track of the amount of each type of CharacterCard acquired, and if each is >= 1, a set is completed
 * and food is given. Each Character's counter then is decreased by one.
 */
public class CardSetForFoodBuilding extends BuildingCard {
    Map<Character, Integer> newCharacters;
    boolean setCompleted;

    public CardSetForFoodBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);

        setCompleted = false;
        newCharacters = new HashMap<>();
        for(Character ch : Character.values()) {
            newCharacters.put(ch, 0);
        }
    }

    public CardSetForFoodBuilding() {}

    public void setSetcompleted(boolean setCompleted) {this.setCompleted = setCompleted;}

    @Override
    public void onCharacterCardPurchase(Player player, CharacterCard card) {
        newCharacters.put(card.getType(), newCharacters.get(card.getType()) + 1);

        setCompleted = true;
        for(Character ch : Character.values()) {
            if(newCharacters.get(ch) < 1) {
                setCompleted = false;
            }
        }

        if(setCompleted) {
            player.editFood(5);
            for(Character ch : Character.values()) {
                newCharacters.put(ch, newCharacters.get(ch) - 1);
            }
            setCompleted = false;
        }
    }
}
