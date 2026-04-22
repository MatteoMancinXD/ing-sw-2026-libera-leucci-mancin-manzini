package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;

public class ArtistCard extends CharacterCard {
    public ArtistCard(int id, int era, int minPlayers) {
        super(id, era, minPlayers);
    }
    public ArtistCard() {}


    @Override
    public void assignTo(Player player) {
        player.addArtist(this);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementArtists();
    }
}
