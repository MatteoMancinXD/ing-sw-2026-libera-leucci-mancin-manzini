
package it.polimi.ingsw.model;

import java.util.ArrayList;

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
