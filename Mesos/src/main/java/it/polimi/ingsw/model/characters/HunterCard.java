package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class HunterCard extends CharacterCard {
    private boolean getsFood;

    public HunterCard(int id, int era, int minPlayers, boolean getsFood) {
        super(id, era, minPlayers);
        this.getsFood = getsFood;
    }
    public HunterCard() {}

    public void setGetsFood(boolean getsFood) {
        this.getsFood = getsFood;
    }

    public boolean getGetsFood() {
        return getsFood;
    }

    @Override
    public void assignTo(Player player) {
        player.addHunter(this);
    }
}
