package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.buildings.HuntEventBuilding;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * this class extends the method solveEventCard by solving the event "Hunt" .
 * During a game 3 HuntEvent will always happen regardless of the number of players, 1 in each era.
 * The bonus/malus depends on the era and on the quantity of Hunters in the player's deck.
 * 
 * the method also checks the presence of HuntEventBuilding. If it's present in player's deck 
 * give a bonus during the event. 
 *
 * @author Riccardo Libera 
 * */

//IMPORTANT: the event "Drew HunterCard with getsFood bonus" is not solved here

public class HuntEvent extends EventCard {

    public HuntEvent() {
        super(0, 0,false); // jackson default constructor
    }

    /*
    @Override
    public void setEra(int era) {
        super.setEra(era);
    }*/

    public HuntEvent(int id, int era,boolean finalEvent) {
        super(id,era,false);
    }

    @Override
    public void solveEventCard(Player player, List<Player> allPlayers) {

        int hunters = player.getHunters().size();

        // +1 food and +1*era prestige foreach hunter

        player.editFood(hunters);
        player.editPrestige(hunters * this.getEra());


        //HuntEventBuilding bonus
        for (BuildingCard card : player.getBuildings()) {
            //gives one extra food and one extra prestige if the HuntEventBuilding is in player's deck
            player.editFood(card.getHuntEventFoodBonus(hunters));
            player.editPrestige(card.getHuntEventFoodBonus(hunters));

        }

    }

}


