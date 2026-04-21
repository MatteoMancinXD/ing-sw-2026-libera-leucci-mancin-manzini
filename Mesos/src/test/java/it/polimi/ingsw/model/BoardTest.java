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

    @BeforeEach
    public void setup() {
        allCard = new ArrayList<>();
        board = new Board(3);
        allCard = loadCardsFromJson();
        deck = new TribeDeck(allCard, 3);
        board.fill(3, 1, deck , new BuildingDeck());
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
        assertEquals(7, board.getUpperRow().size(), "3 player -> 7 cards in upper row");
        assertEquals(4, board.getLowerRow().size(), "3 player -> 4 cards in lower row");
    }

    @Test
    void testSetupRowsFivePlayers() {
        Board board5 = new Board(5);
        board5.fill(5, 1, deck, new BuildingDeck());
        assertEquals(9, board5.getUpperRow().size(), "5 player -> 9 cards in upper row");
        assertEquals(6, board5.getLowerRow().size(), "5 player -> 6 cards in lower row");
    }



}
