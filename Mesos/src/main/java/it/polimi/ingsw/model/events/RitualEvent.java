
package it.polimi.ingsw.model;

import java.util.ArrayList;

/*
* da class Game serve
*   public ArrayList<Player> getPlayers(){ // (Game game) come parametro se ci dovessero essere piu game
 *      ArrayList<Player> playersInGame = players;
 *      return playersInGame;
* */



public class RitualEvent extends EventCard{
    public solveEventCard(Player player,Era era){

        ArrayList<Player> playersInGame = getPlayers(); //or game.getPlayers(); if there's more than 1 game
        ArrayList<Card> playerDeck = player.getPlayerCards();



        // !!! goes into CONTROLLER and then PUT A METHOD AND CALL IT HERE
        //compare the player's stars with the other players' stars
        int i=0;
        int j=0;

        for(Player playerInGame : playersInGame && !playerInGame.equals(player)){
            if(player.getTotStars() >= playerInGame.getTotStars()){
                i++;
            } else if (player.getTotStars() <= playerInGame.getTotStars()) {
                j++;
            }
        }
        // !!!



        //i and j are to be removed with the controller's "stars compare method"
        if(i==playersInGame.size()-1){  //all the others - himself(1)
            //player who invoked the solve event method has more stars than all the others (or equal to someone else with whom has more stars than all the others)
            switch(era){
                case(1):
                    player.editPrestige(5 * (player.CardInDeck(RitualEventBuildingII) ? 2 : 1));
                    break;
                case(2):
                    player.editPrestige(10 * (player.CardInDeck(RitualEventBuildingII) ? 2 : 1));
                    break;
                case(3):
                    player.editPrestige(15 * (player.CardInDeck(RitualEventBuildingII) ? 2 : 1));
                    break;
            }
        }
        //if player has the RitualEventBuildingI no malus
        if(( j==playersInGame.size()-1) && !player.CardInDeck(RitualEventBuildingI)){
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