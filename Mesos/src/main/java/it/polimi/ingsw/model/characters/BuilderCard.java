package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;

public class BuilderCard extends CharacterCard {
    private int discount;
    private int pps;

    public BuilderCard(int id, int era, int minPlayers, int pps, int discount) {
        super(id, era, minPlayers);
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
        player.editDiscount(this.discount);
        player.addBuilder(this);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementBuilders();
    }

    @Override
    public String getShortString() {
        return String.format("Builder: era=%d, discount=%d", getEra(), getDiscount());
    }
}
