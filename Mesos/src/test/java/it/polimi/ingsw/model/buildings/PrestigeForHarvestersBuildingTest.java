package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.prestigebuildings.PrestigeForHarvestersBuilding;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForHarvestersBuildingTest {

    @Test
    void prestigeGainOnGameEndTest() {

        PrestigeForHarvestersBuilding testBuild = new PrestigeForHarvestersBuilding(1, 0, 0, 0);
        HarvesterCard c1 = new HarvesterCard();
        HarvesterCard c2 = new HarvesterCard();
        HarvesterCard c3 = new HarvesterCard();
        HarvesterCard c4 = new HarvesterCard();
        HarvesterCard c5 = new HarvesterCard();
        HarvesterCard c6 = new HarvesterCard();

        Player p1 = new Player("Dante");
        Player p2 = new Player("Beatrice");

        p1.drawCard(c1);
        p1.drawCard(c2);
        p1.drawCard(c3);
        p1.drawCard(c4);        //p1 ha 4 harvesters

        p2.drawCard(c5);        //p2 ha 2 harvesters
        p2.drawCard(c6);

        p1.drawCard(testBuild);
        p2.drawCard(testBuild);

        testBuild.onGameEnd(p1);
        testBuild.onGameEnd(p2);

        assertEquals(16, p1.getPrestige(), "Player 1 should have 4*4=16 prestige");
        assertEquals(8, p2.getPrestige(), "Player 2 should have 4*2=8 prestige");

    }
}
