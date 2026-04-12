package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class InventorCard extends CharacterCard {
    private final Invention invention;

    public InventorCard(int era, int minPlayers, Character type, Invention invention) {
        super(era, minPlayers, type);
        this.invention = invention;
    }

    public Invention getInvention() {
        return invention;
    }

    @Override
    public void assignTo(Player player) {
        player.addInventor(this);
    }
}
