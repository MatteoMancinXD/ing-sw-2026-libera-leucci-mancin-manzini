package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BuildingCard;

public class PrestigeForCharacterBuilding extends BuildingCard {
    private int bonusPrestige;


    public PrestigeForCharacterBuilding(int id, int era, int foodCost, int prestigeGain, int bonusPrestige) {
        super(id,era,foodCost,prestigeGain);
        this.bonusPrestige=bonusPrestige;
    }

    public PrestigeForCharacterBuilding() {}
    public void setBonusPrestige(int bonusPrestige) {this.bonusPrestige=bonusPrestige;}
}
