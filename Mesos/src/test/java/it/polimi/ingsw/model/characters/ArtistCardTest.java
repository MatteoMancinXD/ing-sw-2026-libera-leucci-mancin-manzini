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
    void setUp() {}

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
    public void testAssignTo() {
        Player pl = new Player("giacomo");

        //ArtistCard artistCard4 = new  ArtistCard(1,1,2,Character.ARTIST);
        ArtistCard artistCard4 = new  ArtistCard(1,1,2);
        artistCard4.assignTo(pl);

        ArtistCard artiCard5 = new  ArtistCard(0,0,0);
        artiCard5.assignTo(pl);
    }

    @Test
    public void testAssignToCardSet() {
        CardSetForFoodBuilding newSpecificCard = new CardSetForFoodBuilding();
        newSpecificCard.incrementArtists();

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
