package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.PaintingsEventBuilding;
import it.polimi.ingsw.model.characters.ArtistCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaintingsEventTest {

    @Test
    void PaintingsEventTest() {
        PaintingsEvent paint = new PaintingsEvent(1,3);
        List<Player> players = new ArrayList<>();


        for(int i = 0; i < 3; i++) {
            players.add(new Player("Player" + Integer.toString(i)));
        }

        ArtistCard a1 = new ArtistCard(2,1,2, Character.ARTIST);
        ArtistCard a2 = new ArtistCard(3,1,2, Character.ARTIST);
        ArtistCard a3 = new ArtistCard(4,1,2, Character.ARTIST);
        ArtistCard a4 = new ArtistCard(5,1,2, Character.ARTIST);
        ArtistCard a5 = new ArtistCard(6,1,2, Character.ARTIST);
        ArtistCard a6 = new ArtistCard(7,1,2, Character.ARTIST);

        PaintingsEventBuilding b = new PaintingsEventBuilding(8,1,0,0);

        players.get(0).drawCard(a1);
        players.get(1).drawCard(a2);
        players.get(1).drawCard(a3);
        players.get(2).drawCard(a4);
        players.get(2).drawCard(a5);
        players.get(2).drawCard(a6);

        players.get(1).drawCard(b);

        // Checking 0 food and 0 prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            paint.solveEventCard(p, players);
        }

        // Checking Player0 prestige is -2 and food is 0
        assertEquals(-2, players.getFirst().getPrestige());
        assertEquals(0, players.getFirst().getFood());

        // Checking Player1 prestige is -2 and food is numOfArtists
        assertEquals(-2, players.get(1).getPrestige());
        assertEquals(2, players.get(1).getFood());

        // Checking Player2 prestige is 9 and food is 0
        assertEquals(9, players.get(2).getPrestige());
        assertEquals(0, players.get(2).getFood());
    }

}