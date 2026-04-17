package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.Map;

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
            player.setFood(player.getFood() + 5);
            for(Character ch : Character.values()) {
                newCharacters.put(ch, newCharacters.get(ch) - 1);
            }
            setCompleted = false;
        }
    }
}
