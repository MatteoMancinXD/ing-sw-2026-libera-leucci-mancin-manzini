
package it.polimi.ingsw.model;
import java.util.ArrayList;

public class PaintingsEvent extends EventCard{
    public solveEventCard(Player player,Era era){
        int artists = 0;

        ArrayList<Card> playerDeck = player.getPlayerCards();

        for(Card card : playerDeck){
            if(card instanceof ArtistCard){
                artists++;
            }
        }


        // -2 prestige una tantum if below set number of artists else +era*artists
        // prestige can go negative by rules
        switch (era){
            case(1):
                if(artists==0)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists);
                break;
            case(2):
                if(artists<2)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists*2);
                break;
            case(3):
                if(artists<3)
                    player.editPrestige(-2);
                else
                    player.editPrestige(artists*3);
                break;
        }

        //PaintingsEventBuilding's food bonus check
        for(Card card : playerDeck){
            if(card instanceof PaintingsEventBuilding){
                player.editFood(artists); //for x artists gives x food during PaintingsEvent only if you have PaintingsEventBuilding
            }
        }

    }
}
