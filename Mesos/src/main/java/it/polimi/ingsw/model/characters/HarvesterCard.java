package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;

public class HarvesterCard extends CharacterCard {
    public HarvesterCard(int era, int minPlayers, Character type) {
        super(era, minPlayers, type);
    }

    @Override
    public void assignTo(Player player) {
        player.addHarvester(this);
    }
}
