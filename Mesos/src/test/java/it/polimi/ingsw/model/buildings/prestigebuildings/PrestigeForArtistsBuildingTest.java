package it.polimi.ingsw.model.buildings.prestigebuildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForArtistsBuildingTest {

    @Test
    void prestigeGainOnGameEndTest() {

        PrestigeForArtistsBuilding testBuild = new PrestigeForArtistsBuilding(1, 0, 0, 0);
        ArtistCard c1 = new ArtistCard();
        ArtistCard c2 = new ArtistCard();
        ArtistCard c3 = new ArtistCard();
        ArtistCard c4 = new ArtistCard();
        ArtistCard c5 = new ArtistCard();
        ArtistCard c6 = new ArtistCard();

        Player p1 = new Player("Dante");
        Player p2 = new Player("Beatrice");

        p1.drawCard(c1);
        p1.drawCard(c2);
        p1.drawCard(c3);
        p1.drawCard(c4);        //p1 ha 4 artist

        p2.drawCard(c5);        //p2 ha 2 artist
        p2.drawCard(c6);

        p1.drawCard(testBuild);
        p2.drawCard(testBuild);

        testBuild.onGameEnd(p1);
        testBuild.onGameEnd(p2);

        assertEquals(16, p1.getPrestige(), "Player 1 should have 4*4=16 prestige");
        assertEquals(8, p2.getPrestige(), "Player 2 should have 4*2=8 prestige");

    }

}
