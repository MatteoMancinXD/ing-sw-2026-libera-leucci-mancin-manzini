package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class SustenanceBuildingI extends BuildingCard{

    private Character character;

    SustenanceBuildingI(int id, int era, int  foodCost, int prestigeGain, Character character) {
        super(id,era,foodCost,prestigeGain);
        this.character=character;
    }
    public void setCharacter(Character character) {this.character=character;}
    public Character getCharacter() { return this.character; }

    @Override
    public int getSustenanceEventFoodBonus(Player p){
        return p.getCharacterDeck(this.character).size();
    }

}
