package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public class InventorCardTest {


    private List<TribeCard> allCards;
    private List<InventorCard> inventorCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        inventorCards = allCards.stream()
                .filter(c -> c instanceof InventorCard)
                .map(c -> (InventorCard) c)
                .collect(Collectors.toList());
    }

    private List<TribeCard> loadCardsFromJson() { //method in game
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> cards = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<>() {
            };
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                int eraNumber = Integer.parseInt(entry.getKey().substring(3));
                for (TribeCard card : entry.getValue()) {
                    card.setEra(eraNumber);
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            fail("Impossibile caricare il file JSON: " + e.getMessage());
        }
        return cards;
    }

    @Test
    void testConstructor(){
        InventorCard inventorCard1= new InventorCard(0,0,2,Invention.ROPE);
        InventorCard inventorCard2 = new InventorCard();

        assertFalse(inventorCards.isEmpty());
    }

    @Test
    public void testMethods(){
        Player player = new Player("giacomo");
        InventorCard inventorCard1= new InventorCard(0,0,2,Invention.ROPE);

        inventorCard1.setInvention(Invention.BREAD);
        assertEquals(Invention.BREAD,inventorCard1.getInvention());

        //assignTo
        inventorCard1.assignTo(player);
        assertTrue(player.getInventors().contains(inventorCard1));

    }

    @Test
    public void testJSON(){
        for(InventorCard card: inventorCards){
            assertNotNull(card.getInvention());
        }
    }

}