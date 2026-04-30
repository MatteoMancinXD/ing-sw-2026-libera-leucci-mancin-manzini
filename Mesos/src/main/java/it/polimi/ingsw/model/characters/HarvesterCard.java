package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;

public class HarvesterCard extends CharacterCard {
    public HarvesterCard(int id, int era, int minPlayers) {
        super(id, era, minPlayers);
    }
    public HarvesterCard() {}

    @Override
    public void assignTo(Player player) {
        player.addHarvester(this);
    }

    @Override
    public void registerForCardSet(CardSetForFoodBuilding b) {
        b.incrementHarvesters();
    }

    @Override
    public String getShortString() {
        return String.format("Harvester: era=%d, SustenanceDiscount=3", getEra());
    }
}
