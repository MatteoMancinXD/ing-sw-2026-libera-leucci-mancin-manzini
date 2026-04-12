package it.polimi.ingsw.model;

import java.util.ArrayList;


//this obv solves just the event "Hunt Event Card"
//IMPORTANT: the event "Drew HunterCard with getsFood bonus" is not solved here

public class HuntEvent extends EventCard{

    public solveEventCard(Player player,Era era){
        int hunters = 0;

        ArrayList<Card> playerDeck = player.getPlayerCards();

       for(Card card : playerDeck){
           if(card instanceof HunterCard){
               hunters++;
           }
       }


    // +1 food and +1*era prestige foreach hunter
        switch (era){
            case(1):
                player.editFood(hunters);
                player.editPrestige(hunters);
                break;
            case(2):
                player.editFood(hunters);
                player.editPrestige(hunters*2);
                break;
            case(3):
                player.editFood(hunters);
                player.editPrestige(hunters*3);
                break;
        }

       //HuntEventBuilding bonus
        for(Card card : playerDeck){
            if(card instanceof HuntEventBuilding){
                //gives an extra +1 food and +1 prestige for each hunter
                editFood(hunters);
                editPrestige(hunters);
            }
        }



    }

}


