package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class ArtistCard extends CharacterCard {
    public ArtistCard(int id, int era, int minPlayers, Character type) {
        super(id, era, minPlayers, type);
    }
    public ArtistCard() {}


    @Override
    public void assignTo(Player player) {
        player.addArtist(this);
    }
}
