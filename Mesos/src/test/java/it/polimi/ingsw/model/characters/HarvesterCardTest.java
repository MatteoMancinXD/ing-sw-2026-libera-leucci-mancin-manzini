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


public class HarvesterCardTest {
    private List<TribeCard> allCards;
    private List<HarvesterCard> harvesterCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        harvesterCards = allCards.stream()
                .filter(c -> c instanceof HarvesterCard)
                .map(c -> (HarvesterCard) c)
                .collect(Collectors.toList());
    }

    private List<TribeCard> loadCardsFromJson() { //method in game
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> cards = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<>() {};
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                int eraNumber = Integer.parseInt(entry.getKey().substring(3));
                for(TribeCard card : entry.getValue()) {
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
    public void testConstructor(){
        HarvesterCard harvesterCard1 = new HarvesterCard(0,0,2);
        HarvesterCard harvesterCard2 = new HarvesterCard();

        assertFalse(harvesterCards.isEmpty());
    }

    @Test
    public void testMethods(){
        HarvesterCard harvesterCard1 = new HarvesterCard(0,0,2);
        Player player1 = new Player("giacomo");

        harvesterCard1.assignTo(player1);
        assertTrue(player1.getHarvesters().contains(harvesterCard1));
    }
}
