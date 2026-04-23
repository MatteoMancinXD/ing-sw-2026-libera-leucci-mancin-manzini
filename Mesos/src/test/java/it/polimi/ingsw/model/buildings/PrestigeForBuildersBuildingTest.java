package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.prestigebuildings.PrestigeForBuildersBuilding;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForBuildersBuildingTest {
    @Test
    void prestigeGainOnGameEndTest() {

        PrestigeForBuildersBuilding testBuild = new PrestigeForBuildersBuilding(1, 0, 0, 0);
        BuilderCard c1 = new BuilderCard();
        BuilderCard c2 = new BuilderCard();
        BuilderCard c3 = new BuilderCard();
        BuilderCard c4 = new BuilderCard();
        BuilderCard c5 = new BuilderCard();
        BuilderCard c6 = new BuilderCard();

        Player p1 = new Player("Dante");
        Player p2 = new Player("Beatrice");

        p1.drawCard(c1);
        p1.drawCard(c2);
        p1.drawCard(c3);
        p1.drawCard(c4);        //p1 ha 4 builder

        p2.drawCard(c5);        //p2 ha 2 builder
        p2.drawCard(c6);

        p1.drawCard(testBuild);
        p2.drawCard(testBuild);

        testBuild.onGameEnd(p1);
        testBuild.onGameEnd(p2);

        assertEquals(16, p1.getPrestige(), "Player 1 should have 4*4=16 prestige");
        assertEquals(8, p2.getPrestige(), "Player 2 should have 4*2=8 prestige");

    }
}
