package it.polimi.ingsw.model.events;
import it.polimi.ingsw.model.buildings.*;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;


/**
 *
 * this class extends the method solveEventCard by solving the event "Ritual" .
 * During a game 3 RitualEvent will always happen regardless of the number of players, 1 in each era.
 
 * The bonus/malus depends on how much stars each player has. By calling the solveEventCard method on 
 * a RitualEvent card you have to put game and player as parameters. The player will be checked
 * individually if he has or has not more or less stars than the other players (or less equal/ more equal 
 * to someone other). The method then gives the player the bonus/malus he deserves. 
 * 
 * the method also checks the presence of RitualEventBuildings. If they are present in player's deck 
 * they give a bonus during the event. 
 *
 * @author Riccardo Libera 
 * */

public class RitualEvent extends EventCard{

    public RitualEvent(int id,int era){
        super(id,era);
    }

    @Override
    public void solveEventCard(Game game,Player player){

        boolean buildingBonusI = false;


        int era = this.getEra();


        //compare the player's stars with the other players' stars: if the player's is among the bests or among the worsts: then give bonus/malus
        int i=0;
        int j=0;

        /* RITUAL BUILDING THAT ADDS 3 STARS: THE 3 STARS MUST BE ADDED BY ritualBuildingIII DRAWING
        if(starsBonusFlag = true) {    //otherwise you put this starsBonusFlag on the card's draw on false
            for (BuildingCard card : player.getBuildings()) {
                player.setTotStars(player.getTotStars() + card.getRitualEventBonusStars());
            }
            starsBonusFlag = false;
        } */
        for(Player playerInGame : game.getPlayersView()){ //game.getPlayersView is the unmodifiable list of players inside the game
            if(!(playerInGame.equals(player))){
                if(player.getTotStars() >= playerInGame.getTotStars()){
                    i++;
                }
                if (player.getTotStars() <= playerInGame.getTotStars()) {
                    j++;
                }
            }

        }




        //i and j are to be removed with the controller's "stars compare method"
        if(i==game.getNumPlayers()-1){  //all the others - himself(1)
            //player who invoked the solve event method has more stars than all the others (or equal to someone else with whom has more stars than all the others)
            for(BuildingCard card : player.getBuildings()) {
                player.editPrestige(5 * era * card.getRitualEventDoublePrestigeBonus());
            }

        }
        //A METHOD THAT CHECKS IF A CARD IS IN A DECK MUST BE ADDED
        //if player has the RitualEventBuildingI no malus
        for(BuildingCard card : player.getBuildings()){
            buildingBonusI = card.getRitualEventNoPrestigeMalus();
        }

        if(( j==game.getNumPlayers()-1) && !buildingBonusI){
            //player who invoked the solve event method has less stars than all the others (or equal to someone else with whom has less stars than all the others)
            switch(era){
                case 1:
                    player.editPrestige(-3);
                    break;
                case 2:
                    player.editPrestige(-5);
                    break;
                case 3:
                    player.editPrestige(-7);
                    break;
            }
    }



    }
}
