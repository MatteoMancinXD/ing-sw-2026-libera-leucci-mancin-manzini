package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
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
    int hunters, builders, harvesters, artists, inventors, shamans;

    public CardSetForFoodBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain);
        hunters = 0;
        builders = 0;
        harvesters = 0;
        artists = 0;
        inventors = 0;
        shamans = 0;
    }

    public CardSetForFoodBuilding() {
        super();
        hunters = 0;
        builders = 0;
        harvesters = 0;
        artists = 0;
        inventors = 0;
        shamans = 0;
    }

    public void incrementHunters() { hunters++; }
    public void incrementBuilders() { builders++; }
    public void incrementHarvesters() { harvesters++; }
    public void incrementArtists() { artists++; }
    public void incrementInventors() { inventors++; }
    public void incrementShamans() { shamans++; }

    @Override
    public void onCharacterCardPurchase(Player player, CharacterCard card) {
        card.registerForCardSet(this);

        if(hunters > 0 && builders > 0 &&  harvesters > 0 && artists > 0 && inventors > 0 && shamans > 0) {
            player.editFood(5);

            hunters--;
            builders--;
            harvesters--;
            artists--;
            inventors--;
            shamans--;
        }
    }
}
