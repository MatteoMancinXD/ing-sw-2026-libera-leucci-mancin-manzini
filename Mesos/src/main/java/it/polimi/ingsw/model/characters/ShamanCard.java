package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class ShamanCard extends CharacterCard {
    private final int stars;

    public ShamanCard(int era, int minPlayers, Character type, int stars) {
        super(era, minPlayers, type);
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    @Override
    public void assignTo(Player player) {
        player.addShaman(this);
    }
}
