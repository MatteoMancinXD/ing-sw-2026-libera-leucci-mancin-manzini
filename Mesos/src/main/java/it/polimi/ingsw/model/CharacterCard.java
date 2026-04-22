package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;
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
}
