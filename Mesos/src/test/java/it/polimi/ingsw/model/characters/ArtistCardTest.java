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


public class ArtistCardTest {

    private List<TribeCard> allCards;
    private List<ArtistCard> artistCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        artistCards = allCards.stream()
                .filter(c -> c instanceof ArtistCard)
                .map(c -> (ArtistCard) c)
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
    public void testConstructor() {

        int id, numPl, era;
        Player player;


        ArtistCard artistCard2 = new ArtistCard();   //void constructor
        ArtistCard artistCard3 = new ArtistCard(0, 0, 0, Character.ARTIST); //no json


        //with json
        era = 1;
        numPl = 2;

        assertFalse(artistCards.isEmpty());

        for (ArtistCard card : artistCards) {

            for (id = 1; id <= 6; id++) {
                card = new ArtistCard(id, era, numPl, Character.ARTIST);
                if (id >= 5) numPl++;
            }
        }
    }

    @Test
    public void testArtistJSON() {
        int id, numPl, era;
        Player player;
        era =1;
        numPl = 2;


            for(ArtistCard card : artistCards) {
                //assertEquals(Character.ARTIST,card.getType());

                assertTrue(card.getId()>0);
                assertTrue(card.getId()<97);
                assertTrue(card.getEra()>=0 && card.getEra()<=3);
                assertTrue(card.getMinPlayers()>=2);

                assertFalse(card.getId()==0 && card.getId()>=97);
                assertFalse(card.getEra()<0 && card.getEra()>3);
                assertFalse(card.getMinPlayers()<2 && card.getMinPlayers()>6);


        }

    }

    @Test
    public void testAllJSON() {
        int id, numPl, era;
        Player player;


        for(TribeCard card : allCards) {
            //assertEquals(Character.ARTIST,card.getType());

            assertTrue(card.getId()>0 && card.getId()<97);
            assertTrue(card.getEra()>=0 && card.getEra()<=3);
            assertTrue(card.getMinPlayers()>=2 && card.getMinPlayers()<=6);

            assertFalse(card.getId()==0 && card.getId()>=97);
            assertFalse(card.getEra()<0 && card.getEra()>3);
            assertFalse(card.getMinPlayers()<2 && card.getMinPlayers()>6);


        }

    }









/*
        assertThrows(IllegalArgumentException.class, ()->
        {
            for(ArtistCard card : artistCards) {  //with json
                    card = new ArtistCard(0,era,2,Character.ARTIST);

            }
        }
        );*/

        //new ArtistCard(0,0,0,Character.ARTIST);
        /*assertThrows(IllegalArgumentException.class, () -> {
            new ArtistCard(0, 1, 2, Character.ARTIST);
        });*/



}
