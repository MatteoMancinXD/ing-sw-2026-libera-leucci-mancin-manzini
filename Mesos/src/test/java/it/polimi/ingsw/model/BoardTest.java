package it.polimi.ingsw.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
//import it.polimi.ingsw.model.Board.*;


public class BoardTest {

    private Board board;
    private List<TribeCard> allCard;
    private TribeDeck deck;
    private ArrayList<BuildingCard> allBuildings;
    private BuildingDeck bDeck;

    @BeforeEach
    public void setup() {
        int numPlayers = 3;
        allCard = new ArrayList<>();
        board = new Board(numPlayers);
        allCard = loadCardsFromJson();
        allBuildings = loadBuildingsFromJson();
        deck = new TribeDeck(allCard, numPlayers);
        bDeck = new BuildingDeck(allBuildings, numPlayers);
        board.fill(numPlayers, 1, deck , bDeck);
    }

    private List<TribeCard> loadCardsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> allCardsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<Map<String, List<TribeCard>>>() {};
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                String eraString = entry.getKey(); //Prende chiavi del JSON (era1, era2, era3)
                int eraNumber = Integer.parseInt(eraString.substring(3));
                for(TribeCard card : entry.getValue()) {
                    card.setEra(eraNumber); //l'era si imposta "manualmente" perchè NON è un parametro nel JSON
                    allCardsInGame.add(card);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allCardsInGame;
    }

    private ArrayList<BuildingCard> loadBuildingsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<BuildingCard> allBuildingsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/buildingsInfo.json");
            TypeReference<Map<String, List<BuildingCard>>> typeRef = new TypeReference<Map<String, List<BuildingCard>>>() {};
            Map<String, List<BuildingCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<BuildingCard>> entry : data.entrySet()) {
                String eraString = entry.getKey(); //Prende chiavi del JSON (era1, era2, era3)
                int eraNumber = Integer.parseInt(eraString.substring(3));
                for(BuildingCard card : entry.getValue()) {
                    card.setEra(eraNumber); //l'era si imposta "manualmente" perchè NON è un parametro nel JSON ma chiavi
                    allBuildingsInGame.add(card);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allBuildingsInGame;
    }

    @Test      //per 3 player
    void testSetupForThreePlayers() {
        assertEquals(5, board.getTrack().size(), "3 player -> 5 tile track");
        assertEquals('B', board.getTrack().get(0).getLetter(), "1st tile should be B");
    }

    @Test      //per 5 player
    void testSetupForFivePlayers() {
        Board board5 = new Board(5);
        assertEquals(7, board5.getTrack().size(), "5 player -> 7 tile track");
        assertEquals('A', board5.getTrack().get(0).getLetter(), "1st tile should be A");
    }

    @Test
    void testSetupRows() {
        assertEquals(9, board.getUpperRow().size(), "3 player -> 7 cards in upper row + 2 buildings");
        assertEquals(4, board.getLowerRow().size(), "3 player -> 4 cards in lower row");
    }

    @Test
    void testSetupRowsFivePlayers() {
        Board board5 = new Board(5);
        board5.fill(5, 1, deck, new BuildingDeck());
        assertEquals(9, board5.getUpperRow().size(), "5 player -> 9 cards in upper row");
        assertEquals(6, board5.getLowerRow().size(), "5 player -> 6 cards in lower row");
    }

    @Test
    void testEndRoundMechanics() {
        int initialUpperSize = board.getUpperRow().size();

        board.clearLowerRow();

        // ASSERT 1: La riga inferiore non deve avere carte normali (solo gli eventuali edifici scesi in ere precedenti, ma qui siamo a inizio gioco)
        for (Card c : board.getLowerRow()) {
            assertTrue(c instanceof BuildingCard, "Dopo clearLowerRow, sotto devono rimanere SOLO edifici");
        }
        board.shiftRow();

        // ASSERT 2: La riga superiore deve contenere SOLO edifici o eventi speciali (se la tua logica li tiene su), le altre sono scese
        for (Card c : board.getUpperRow()) {
            assertTrue(c instanceof BuildingCard, "Dopo lo shift, sopra rimangono solo gli edifici");
        }
        // La riga inferiore ora dovrebbe contenere le carte normali che prima erano sopra
        assertFalse(board.getLowerRow().isEmpty(), "La riga inferiore deve essersi riempita con le carte scese");
    }

}
