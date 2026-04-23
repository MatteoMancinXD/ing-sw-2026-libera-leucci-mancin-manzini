package it.polimi.ingsw.model.buildings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.sustenancebuildings.SustenanceForHarvestersBuilding;
import it.polimi.ingsw.model.characters.ArtistCard;
import it.polimi.ingsw.model.characters.HarvesterCard;
import it.polimi.ingsw.model.characters.HunterCard;
import it.polimi.ingsw.model.events.HuntEvent;
import it.polimi.ingsw.model.events.SustenanceEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SustenanceForHarvestersBuildingTest {

    @Test
    void testSustenanceEvent() {


        SustenanceEvent sustenance = new SustenanceEvent(1,2,false);
        SustenanceForHarvestersBuilding b = new SustenanceForHarvestersBuilding(0,0,0,0);
        Player p = new Player("JonSnow");
        p.editFood(10);             //Cibo iniziale player = 10
        for(int i = 0; i < 3; i++) {
            p.drawCard(new HarvesterCard());
        }
        for(int i = 0; i < 15; i++) {
            p.drawCard(new HunterCard());   //10 altre carte casuali
        }
        p.drawCard(b);
        sustenance.solveEventCard(p, new ArrayList<>());
        //SCONTO HARVESTER = 3*3 = 9
        //SCONTO HARVESTERBUILDING = 3
        //SCONTO TOTALE SU SUSTENANCE = 12
        //NUMERO CARTE = 18; CIBO INIZIALE = 10.
        //SUSTENANCE: 10-(18-12) = 4 cibo rimasto dopo l'evento

        assertEquals(4, p.getFood());
    }

}

