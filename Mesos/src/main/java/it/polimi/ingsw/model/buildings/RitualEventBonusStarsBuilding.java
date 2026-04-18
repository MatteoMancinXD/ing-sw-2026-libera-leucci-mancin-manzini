package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class RitualEventBonusStarsBuilding extends BuildingCard {

    public RitualEventBonusStarsBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }


    //adds 3 stars
    @Override
    public void onPurchase(Player player){
        player.setTotStars(player.getTotStars() + 3);
    }

}
