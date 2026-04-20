package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.BuilderCard;


public abstract class CharacterCard extends TribeCard{
    private Character type;

    public CharacterCard(int id, int era, int minPlayers, Character type) {
        super(id, era, minPlayers);
        this.type = type;
    }
    protected CharacterCard() {}

    public Character getType() {
        return type;
    }

    @Override
    public void notifyBuildings(Player player) {
        for(BuildingCard bc : player.getBuildings()) {
            bc.onCharacterCardPurchase(player, this);
        }
    }
}
