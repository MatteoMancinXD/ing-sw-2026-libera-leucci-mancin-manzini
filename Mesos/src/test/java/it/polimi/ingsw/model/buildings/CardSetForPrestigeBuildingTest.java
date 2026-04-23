package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSetForPrestigeBuildingTest {

    @Test
    void cardSetForPrestigeBuildingTest() {
        CardSetForPrestigeBuilding b = new CardSetForPrestigeBuilding(1,1,0,0);
        Player p = new Player("Player1");

        for(int i = 0; i < 3; i++) {
            p.drawCard(new HunterCard(0,0,0,false));
        }

        for(int i = 0; i < 4; i++) {
            p.drawCard(new BuilderCard(0,0,0,0,0));
        }

        for(int i = 0; i < 5; i++) {
            p.drawCard(new HarvesterCard(0,0,0));
        }

        for(int i = 0; i < 6; i++) {
            p.drawCard(new ArtistCard(0,0,0));
        }

        for(int i = 0; i < 7; i++) {
            p.drawCard(new InventorCard(0,0,0, Invention.ROPE));
        }

        for(int i = 0; i < 8; i++) {
            p.drawCard(new ShamanCard(0,0,0,0));
        }

        p.drawCard(b);
        b.onGameEnd(p);

        // Checking player's prestige
        assertEquals(18, p.getPrestige());
    }
}