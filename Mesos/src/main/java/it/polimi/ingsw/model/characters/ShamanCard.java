package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;

public class ShamanCard extends CharacterCard {
    private int stars;

    public ShamanCard(int id, int era, int minPlayers, int stars) {
        super(id, era, minPlayers);
        this.stars = stars;
    }
    public ShamanCard() {}

    public void setStars(int stars) {
        this.stars = stars;

    }
    public int getStars() {
        return stars;
    }

    @Override
    public void assignTo(Player player) {
        player.addShaman(this);
        player.editStars(this.stars);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementShamans();
    }
}
