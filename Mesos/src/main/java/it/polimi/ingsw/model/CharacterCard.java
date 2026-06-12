package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;
import it.polimi.ingsw.model.characters.BuilderCard;

/**
 * Abstract class representing a character card that can be acquired by a player.
 * Character types include Artist, Builder, Harvester, Hunter, Inventor, and Shaman.
 * When acquired, character cards notify the player's buildings to trigger
 * any purchase-related effects.
 *
 * @see BuildingCard#onCharacterCardPurchase(Player, CharacterCard)
 */
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
