package it.polimi.ingsw.model.buildings.prestigebuildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForHuntersBuildingTest {
    @Test
    void prestigeGainOnGameEndTest() {

        PrestigeForHuntersBuilding testBuild = new PrestigeForHuntersBuilding(1, 0, 0, 0);
        HunterCard c1 = new HunterCard();
        HunterCard c2 = new HunterCard();
        HunterCard c3 = new HunterCard();
        HunterCard c4 = new HunterCard();
        HunterCard c5 = new HunterCard();
        HunterCard c6 = new HunterCard();

        Player p1 = new Player("Dante");
        Player p2 = new Player("Beatrice");

        p1.drawCard(c1);
        p1.drawCard(c2);
        p1.drawCard(c3);
        p1.drawCard(c4);        //p1 ha 4 hunter

        p2.drawCard(c5);        //p2 ha 2 hunter
        p2.drawCard(c6);

        p1.drawCard(testBuild);
        p2.drawCard(testBuild);

        testBuild.onGameEnd(p1);
        testBuild.onGameEnd(p2);

        assertEquals(12, p1.getPrestige(), "Player 1 should have 4*3=12 prestige");
        assertEquals(6, p2.getPrestige(), "Player 2 should have 2*3=6 prestige");

    }
}
