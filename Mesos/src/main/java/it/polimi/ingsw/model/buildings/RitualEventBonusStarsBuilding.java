package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Player;

public class RitualEventBonusStarsBuilding extends BuildingCard {

    public RitualEventBonusStarsBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id,era,foodCost,prestigeGain);
    }

    public RitualEventBonusStarsBuilding() {}

    //adds 3 stars
    @Override
    public void onPurchase(Player player){
        player.editStars(3);
    }

    @Override
    public String getShortString() {
        return String.format("Ritual Event Bonus Stars [cost=%d, pp=%d] - +3 shaman stars on purchase", getFoodCost(), getPrestigeGain());
    }
}
