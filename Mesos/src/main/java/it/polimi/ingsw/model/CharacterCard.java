package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;
import it.polimi.ingsw.model.characters.BuilderCard;


public abstract class CharacterCard extends TribeCard{
    public CharacterCard(int id, int era, int minPlayers) {
        super(id, era, minPlayers);
    }
    protected CharacterCard() {}

    @Override
    public void notifyBuildings(Player player) {
        for(BuildingCard bc : player.getBuildings()) {
            bc.onCharacterCardPurchase(player, this);
        }
    }

    public void registerForCardSet(CardSetForFoodBuilding b) {}
    public void registerInvention(InventorSetForFoodBuilding b) {}
    public abstract String getShortString();
}
