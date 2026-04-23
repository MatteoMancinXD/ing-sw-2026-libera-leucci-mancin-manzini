package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.sustenancebuildings.SustenanceForArtistsBuilding;
import it.polimi.ingsw.model.characters.ArtistCard;
import it.polimi.ingsw.model.characters.HunterCard;
import it.polimi.ingsw.model.events.HuntEvent;
import it.polimi.ingsw.model.events.SustenanceEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SustenanceForArtistsBuildingTest {

    @Test
    void testSustenanceEvent() {


        SustenanceEvent sustenance = new SustenanceEvent(1,2,false);
        SustenanceForArtistsBuilding b = new SustenanceForArtistsBuilding(0,0,0,0);
        Player p = new Player("JonSnow");
        p.editFood(5);             //Cibo iniziale player = 5
        for(int i = 0; i < 3; i++) {
            p.drawCard(new ArtistCard());
        }
        for(int i = 0; i < 3; i++) {
            p.drawCard(new HunterCard()); //3 altre carte casuali
        }
        p.drawCard(b);
        sustenance.solveEventCard(p, new ArrayList<>());

        assertEquals(2, p.getFood(), "Player has 6 character card, 3 of them are Artists: 6-3 = 3 food to pay. 5-3 = 2 food left");
    }

}
