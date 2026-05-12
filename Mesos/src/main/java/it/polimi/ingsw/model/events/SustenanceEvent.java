package it.polimi.ingsw.model.events;
import it.polimi.ingsw.model.buildings.*;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;
import java.util.List;
import it.polimi.ingsw.model.buildings.sustenancebuildings.*;

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

    public SustenanceEvent(){
        super(0,0,false);
    }
    public SustenanceEvent(int id, int era,boolean finalEvent){
        super(id, era,finalEvent);
    }

    public void solveEventCard(Player player, List<Player> allPlayers){
        int foodPoints;
        int currentHunger;
        int foodFromBuildings = 0; 
        int numCharacterCards = player.getArtists().size() + player.getHunters().size() +
                player.getInventors().size() + player.getShamans().size() +
                player.getHarvesters().size() + player.getBuilders().size();

        //check if there are buildings with Sustenance bonuses (see SustenanceBuilding class)
        for(BuildingCard card : player.getBuildings()){
            foodFromBuildings += card.getSustenanceEventFoodBonus(player);
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

    @Override
    public boolean isSustenanceEvent() {return true;}

    @Override
    public String getShortString() {
        return String.format("Sustenance Event: -1 food for every character you have");
    }
}
