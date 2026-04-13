
package it.polimi.ingsw.model;
import java.util.ArrayList;

/**
 *
 * this class extends the method solveEventCard by solving the event "Paintings" .
 * During a game 3 PaintingsEvent will always happen regardless of the number of players, 1 in each era.
 * The bonus/malus depends on the era and on the quantity of Painters in the player's deck.
 * 
 * the method also checks the presence of PaintingsEventBuilding. If it's present in player's deck 
 * give a bonus during the event. 
 *
 * @author Riccardo Libera 
 * */

public class PaintingsEvent extends EventCard{
    @Override 
    public void solveEventCard(Game game,Player player){
        
        int artists = player.getArtists().size(); 
        int era = this.getEra(); 

        // -2 prestige una tantum if below set number of artists else +era*artists
        // prestige can go negative by rules
        switch (era){
            case 1:
                if(artists==0)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists);
                break;
            case 2:
                if(artists<2)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists*2);
                break;
            case 3:
                if(artists<3)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists*3);
                break;
        }

        //PaintingsEventBuilding's food bonus check
        for(BuildingCard card : player.getBuildings()){
            if(card instanceof PaintingsEventBuilding){
                player.editFood(artists); //for x artists gives x food during PaintingsEvent only if you have PaintingsEventBuilding
            }
        }

    }
}
