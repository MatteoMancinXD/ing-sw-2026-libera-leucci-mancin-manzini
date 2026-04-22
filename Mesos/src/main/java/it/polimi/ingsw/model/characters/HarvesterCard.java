package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CharacterCard;

public class HarvesterCard extends CharacterCard {
    public HarvesterCard(int id, int era, int minPlayers) {
        super(id, era, minPlayers);
    }
    public HarvesterCard() {}

    @Override
    public void assignTo(Player player) {
        player.addHarvester(this);
    }
}
