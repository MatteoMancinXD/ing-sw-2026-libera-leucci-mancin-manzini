package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.BuilderCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrestigeDoublingBuilderValueBuildingTest {

    @Test
    void prestigeDoublingBuilderValueBuilding() {
        PrestigeDoublingBuilderValueBuilding b = new PrestigeDoublingBuilderValueBuilding(0,0,0,0);
        Player p =  new Player("Player");

        p.drawCard(new BuilderCard(0,0,0,2,0));
        p.drawCard(new BuilderCard(0,0,0,4,0));

        p.drawCard(b);

        b.onGameEnd(p);

        // Checking player's bonus prestige is 6
        assertEquals(6, p.getPrestige());
    }
}