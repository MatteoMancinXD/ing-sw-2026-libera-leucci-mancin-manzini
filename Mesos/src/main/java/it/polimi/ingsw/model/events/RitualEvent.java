package it.polimi.ingsw.model.events;
import it.polimi.ingsw.model.buildings.*;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;
import java.util.List;


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

    public RitualEvent(){
        super(0,0,false);
    }

    public RitualEvent(int id,int era,boolean finalEvent){
        super(id,era,finalEvent);
    }

    @Override
    public void solveEventCard(Player player, List<Player> allPlayers){

        boolean hasDoublePrestigeBuilding = false;
        boolean hasNoMalusBuilding = false;

        int betterThanOrEqual;
        int worseThanOrEqual;

        int otherPlayers = allPlayers.size()-1; //everybody - the player(1)

        int era = this.getEra();

    /*
        //compare the player's stars with the other players' stars: if the player's is among the bests or among the worsts: then give bonus/malus
        int i=0;
        int j=0;

        //if player has more/less equal stars compared to all the others
        for(Player playerInGame : allPlayers){
            if(!(playerInGame.equals(player))){
                if(player.getTotStars() >= playerInGame.getTotStars()){
                    i++;
                }
                if (player.getTotStars() <= playerInGame.getTotStars()) {
                    j++;
                }
            }

        }
        */

        //return the number of times the player's stars are better than or equal / worse than or equal, compared to the others
        betterThanOrEqual = allPlayers.stream()
                .filter(p -> !p.equals(player))
                .filter(p -> player.getTotStars() >= p.getTotStars())
                .toList().size();

        worseThanOrEqual = allPlayers.stream()
                .filter(p -> !p.equals(player))
                .filter(p -> player.getTotStars() <= p.getTotStars())
                .toList().size();


        //check if the buildings are present or not
        hasDoublePrestigeBuilding = player.getBuildings().stream().anyMatch(BuildingCard::getRitualEventDoublePrestigeBonus);
        hasNoMalusBuilding = player.getBuildings().stream().anyMatch(BuildingCard::getRitualEventNoPrestigeMalus);


        if(betterThanOrEqual==otherPlayers){
            //player who invoked the solve event method has more stars than all the others (or equal to someone else with whom has more stars than all the others)
            if(hasDoublePrestigeBuilding){
                player.editPrestige(5 * era * 2);
            }else{
                player.editPrestige(5 * era);
            }
        }



        if( worseThanOrEqual==otherPlayers && !hasNoMalusBuilding){
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
