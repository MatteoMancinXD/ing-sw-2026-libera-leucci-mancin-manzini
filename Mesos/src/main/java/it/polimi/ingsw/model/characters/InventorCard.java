package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;

public class InventorCard extends CharacterCard {
    private Invention invention;

    public InventorCard(int id, int era, int minPlayers, Invention invention) {
        super(id, era, minPlayers);
        this.invention = invention;
    }
    public InventorCard() {}

    public void setInvention(Invention invention) {
        this.invention = invention;

    }
    public Invention getInvention() {
        return invention;
    }

    @Override
    public void assignTo(Player player) {
        player.addInventor(this);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementInventors();
    }

    @Override
    public void registerInvention(InventorSetForFoodBuilding b) { b.incrementInvention(invention); }

    @Override
    public String getShortString() {
        return String.format("Artist, era=%d, invention=%s", getEra(), invention);
    }
}
