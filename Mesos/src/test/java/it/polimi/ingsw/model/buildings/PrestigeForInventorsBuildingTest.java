package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.prestigebuildings.PrestigeForInventorsBuilding;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForInventorsBuildingTest {
    @Test
    void prestigeGainOnGameEndTest() {

        PrestigeForInventorsBuilding testBuild = new PrestigeForInventorsBuilding(1, 0, 0, 0);
        InventorCard c1 = new InventorCard();
        InventorCard c2 = new InventorCard();
        InventorCard c3 = new InventorCard();
        InventorCard c4 = new InventorCard();
        InventorCard c5 = new InventorCard();
        InventorCard c6 = new InventorCard();

        Player p1 = new Player("Dante");
        Player p2 = new Player("Beatrice");

        p1.drawCard(c1);
        p1.drawCard(c2);
        p1.drawCard(c3);
        p1.drawCard(c4);        //p1 ha 4 inventor

        p2.drawCard(c5);        //p2 ha 2 inventor
        p2.drawCard(c6);

        p1.drawCard(testBuild);
        p2.drawCard(testBuild);

        testBuild.onGameEnd(p1);
        testBuild.onGameEnd(p2);

        assertEquals(8, p1.getPrestige(), "Player 1 should have 4*2=8 prestige");
        assertEquals(4, p2.getPrestige(), "Player 2 should have 2*2=4 prestige");

    }
}
