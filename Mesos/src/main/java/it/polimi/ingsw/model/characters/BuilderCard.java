package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class BuilderCard extends CharacterCard {
    private int discount;
    private int pps;

    public BuilderCard(int id, int era, int minPlayers, Character type, int pps, int discount) {
        super(id, era, minPlayers, type);
        this.pps = pps;
        this.discount = discount;
    }
    public BuilderCard() {}

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public int getDiscount() {
        return discount;
    }

    public void setPps(int pps) {
        this.pps = pps;
    }
    public int getPps() {
        return pps;
    }

    @Override
    public void assignTo(Player player) {
        player.addBuilder(this);
    }
}
