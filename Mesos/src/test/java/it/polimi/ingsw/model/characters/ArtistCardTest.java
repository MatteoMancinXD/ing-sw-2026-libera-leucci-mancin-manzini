package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
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

    //from class Game

    private List<TribeCard> loadCardsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> allCardsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json"); //in game è resources/json/cardsInfo.json
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




    @Test
    public void testConstructor() {

        Player player = new Player("giacomo");

        ArtistCard artistCard1 = new ArtistCard();   //void constructor
        ArtistCard artistCard2 = new ArtistCard(0, 0, 0 );

        player.addArtist(artistCard1);
        player.addArtist(artistCard2);

        assertFalse(player.getArtists().isEmpty());
        assertEquals(2,player.getArtists().size());
        assertEquals(artistCard1,player.getArtists().get(0));
        assertEquals(artistCard2,player.getArtists().get(1));

    }

    @Test
    public void testJson(){
        assertFalse(allCards.isEmpty());
        assertFalse(artistCards.isEmpty());
    }

    @Test
    public void testAssignTo() {
        Player pl = new Player("giacomo");

        //ArtistCard artistCard4 = new  ArtistCard(1,1,2,Character.ARTIST);
        ArtistCard artistCard4 = new  ArtistCard(1,1,2);
        artistCard4.assignTo(pl);

        ArtistCard artiCard5 = new  ArtistCard(0,0,0);
        artiCard5.assignTo(pl);
    }

    @Test
    public void testRegisterForCardSet() {
        ArtistCard artistCard6 = new  ArtistCard(1,1,2);
        CardSetForFoodBuilding newSpecificBuildingCard = new CardSetForFoodBuilding();

        artistCard6.registerForCardSet(newSpecificBuildingCard);  //the building card increments its counter with a +1 artist

        //still to do , see behaviour in game
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
