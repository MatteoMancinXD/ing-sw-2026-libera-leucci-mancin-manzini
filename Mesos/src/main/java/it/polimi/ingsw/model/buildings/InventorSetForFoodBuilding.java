package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Invention;
import it.polimi.ingsw.model.characters.InventorCard;

import java.util.HashMap;
import java.util.Map;

public class InventorSetForFoodBuilding extends BuildingCard {
    Map<Invention, Integer> newInventors;
    boolean coupleCompleted;
    Invention inv;


    public InventorSetForFoodBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);

        coupleCompleted = false;
        newInventors = new HashMap<>();
        for(Invention i : Invention.values()) {
            newInventors.put(i, 0);
        }
    }

    public InventorSetForFoodBuilding() {
        newInventors = new HashMap<>();
        coupleCompleted = false;
    }

    public void setCoupleCompleted(boolean coupleCompleted) {this.coupleCompleted = coupleCompleted;}
    public void setInv(Invention inv) {this.inv = inv;}

    public void incrementInvention(Invention i) {
        if(newInventors.containsKey(i)) {
            newInventors.put(i, newInventors.get(i) + 1);
        } else {
            newInventors.put(i, 1);
        }
    }

    @Override
    public void onCharacterCardPurchase(Player player, CharacterCard card) {
        card.registerInvention(this);

        for(Invention i : Invention.values()) {
            if(newInventors.get(i) >= 2)  {
                coupleCompleted = true;
                inv = i;
            }
        }

        if(coupleCompleted) {
            player.editFood(3);
            newInventors.put(inv, 0);
            coupleCompleted = false;
        }
    }
}
