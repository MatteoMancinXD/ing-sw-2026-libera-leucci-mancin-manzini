
package it.polimi.ingsw.model;
import java.util.ArrayList;

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
    @Override
    public void solveEventCard(Game game,Player player){

        boolean buildingBonusI = false;
        boolean buildingBonusII = false; 

        int era = this.getEra();


        //compare the player's stars with the other players' stars: if the player's is among the bests or among the worsts: then give bonus/malus
        int i=0;
        int j=0;
        
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


        
        for(BuildingCard buildingCard : player.getBuildings()){
            if(buildingCard instanceof RitualEventBuildingII){
                buildingBonusII = true; 
                break; 
            }
        }
        for(BuildingCard buildingCard : player.getBuildings()){
            if(buildingCard instanceof RitualEventBuildingI){
                buildingBonusI = true; 
                break; 
            }
        }

        //i and j are to be removed with the controller's "stars compare method"
        if(i==game.getNumPlayers()-1){  //all the others - himself(1)
            //player who invoked the solve event method has more stars than all the others (or equal to someone else with whom has more stars than all the others)
            switch(era){
                case(1):
                    player.editPrestige(5 * (buildingBonusII ? 2 : 1));
                    break;
                case(2):
                    player.editPrestige(10 * (buildingBonusII ? 2 : 1));
                    break;
                case(3):
                    player.editPrestige(15 * (buildingBonusII ? 2 : 1));
                    break;
            }
        }
        //if player has the RitualEventBuildingI no malus
        if(( j==game.getNumPlayers()-1) && !buildingBonusI){
            //player who invoked the solve event method has less stars than all the others (or equal to someone else with whom has less stars than all the others)
            switch(era){
                case(1):
                    player.editPrestige(-3);
                    break;
                case(2):
                    player.editPrestige(-5);
                    break;
                case(3):
                    player.editPrestige(-7);
                    break;
            }
    }



    }
}
