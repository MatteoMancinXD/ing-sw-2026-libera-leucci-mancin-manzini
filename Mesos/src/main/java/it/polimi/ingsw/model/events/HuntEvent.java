package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.buildings.HuntEventBuilding;


import java.util.ArrayList;
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

public class HuntEvent extends EventCard{

    public HuntEvent(int era) {
        super(era);
    }

    @Override
    public void solveEventCard(Game game,Player player){
        
        int hunters = player.getHunters().size();
        int era = this.getEra(); 

    // +1 food and +1*era prestige foreach hunter
        switch (era){
            case 1:
                player.editFood(hunters);
                player.editPrestige(hunters);
                break;
            case 2:
                player.editFood(hunters);
                player.editPrestige(hunters*2);
                break;
            case 3:
                player.editFood(hunters);
                player.editPrestige(hunters*3);
                break;
        }

       //HuntEventBuilding bonus
        for(BuildingCard card : player.getBuildings()){
            if(card instanceof HuntEventBuilding){
                //gives an extra +1 food and +1 prestige for each hunter
                player.editFood(hunters);
                player.editPrestige(hunters);
            }
        }

    }

}


