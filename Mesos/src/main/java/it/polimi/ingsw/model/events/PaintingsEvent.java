package it.polimi.ingsw.model.events;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.buildings.*;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;



import java.util.ArrayList;
import java.util.List;

/**
 *
 * this class extends the method solveEventCard by solving the event "Paintings" .
 * During a game 3 PaintingsEvent will always happen regardless of the number of players, 1 in each era.
 * The bonus/malus depends on the era and on the quantity of Painters in the player's deck.
 * 
 * the method also checks the presence of PaintingsEventBuilding. If it's present in player's deck 
 * give a bonus during the event. (see also package buildings)
 *
 * @author Riccardo Libera 
 * */

public class PaintingsEvent extends EventCard{

    public PaintingsEvent(){
        super(0,0,false);
    }
    public PaintingsEvent(int id,int era){
        super(id,era,false);
    }


    @Override 
    public void solveEventCard(Player player, List<Player> allPlayers){
        
        int artists = player.getCharacterDeck(Character.ARTIST).size();
        int era = this.getEra(); 

        // -2 prestige una tantum if below set number of artists else +era*artists
        // prestige can go negative by rules

        if (artists < era) {
            player.editPrestige(-2);
        } else {
            player.editPrestige(artists * era);
        }


        //PaintingsEventBuilding's food bonus check
        for(BuildingCard card : player.getBuildings()){

                //for x artists gives x food during PaintingsEvent only if you have PaintingsEventBuilding

                player.editFood(card.getPaintingsEventFoodBonus(artists)); //OLD VERSION

        }   //it is made like this not to use "instance of" if(BuildingCard card instanceof theExactBuilding) then give bonus

    }
}
