package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.OrderTile;
import it.polimi.ingsw.model.Player;
public class OrderTileBuilding extends BuildingCard {
    public OrderTileBuilding(int id, int era, int foodCost, int prestigeGain) {
        super(id, era, foodCost, prestigeGain); // era 1, foodCost 3, prestigeGain 3
    }

    public void onOrderTilePlacement(Player player, int position, OrderTile order) {
        if (order.getModifiers()[position] > 0) {
            player.editFood(1);
        }
    }
}