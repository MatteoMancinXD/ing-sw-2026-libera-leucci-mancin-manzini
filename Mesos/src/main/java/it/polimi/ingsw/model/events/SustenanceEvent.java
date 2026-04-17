package it.polimi.ingsw.model.events;
import it.polimi.ingsw.model.buildings.*;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

import java.util.ArrayList;

/**
 *
 * this class extends the method solveEventCard by solving the event "Sustenance" .
 * During a game 3 SustenanceEvent will always happen regardless of the number of players, 1 in each era.
 * The bonus/malus depends: on the era, on the quantity of Harvesters that are inside player's deck, on how much food
 * the player has while the event is happening and on whether the player has bonus buildings for the event.
 * 
 * the method also checks the presence of HuntEventBuilding. If it's present in player's deck 
 * give a bonus during the event. 
 *
 * @author Riccardo Libera 
 * */

public class SustenanceEvent extends EventCard{

    public SustenanceEvent(int id, int era){
        super(id, era);
    }

    public void solveEventCard(Game game,Player player){
        int foodPoints;
        int currentHunger;
        int foodFromBuildings = 0; 
        int numCharacterCards = player.getArtists().size() + player.getHunters().size() + player.getInventors().size() + player.getShamans().size() + player.getHarvesters().size() + player.getBuilders().size();

        //check if there are buildings with Sustenance bonuses
        for(BuildingCard card : player.getBuildings()){
            //if the card is present these methods return the parameter, if it s not present it returns zero
            //so no bonus added
            foodFromBuildings += card.getSustenanceEventArtistsFoodBonus(player.getArtists().size());
            foodFromBuildings += card.getSustenanceEventHarvestersFoodBonus(player.getHarvesters().size());
            foodFromBuildings += card.getSustenanceEventInventorsFoodBonus(player.getInventors().size());

        }

        currentHunger = numCharacterCards - (player.getHarvesters().size()*3) - foodFromBuildings; // current Hunger = number of cards - value of the harvest - BuildingFoodDiscount
        foodPoints = player.getFood() - currentHunger;

        if(foodPoints>=0){
            player.setFood(foodPoints);

        }else{
            //not having enough food -> food is put to 0
            //and implies prestige loss depending on the SustenanceCard's era

            player.setFood(0);
            player.editPrestige(this.getEra()*foodPoints); //foodPoints are negative inside else statement

        }

    }
}
