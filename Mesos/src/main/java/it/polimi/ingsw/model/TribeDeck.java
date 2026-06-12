package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the main deck of 96 tribe cards used during the game.
 * Cards are filtered by minimum player count and shuffled by era
 * so that era 1 cards are drawn first, then era 2, then era 3.
 *
 * @see TribeCard
 */
public class TribeDeck {
    private ArrayList<TribeCard> cards;

    /**
     * @param cards to be taken from an archive of all TribeCards
     * @param numPlayers number of players in game
     */
    public TribeDeck(List<TribeCard> cards, int numPlayers) {
        this.cards = new ArrayList<>();

        for(TribeCard card : cards) {
            if(card.getMinPlayers() <= numPlayers)
                this.cards.add(card);
        }
    }


    public TribeDeck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Shuffles the deck segmenting the cards by era, shuffling them, and piling them up
     */
    public void shuffle() {
        ArrayList<TribeCard> era1 = new ArrayList<>();
        ArrayList<TribeCard> era2 = new ArrayList<>();
        ArrayList<TribeCard> era3 = new ArrayList<>();

        for(TribeCard card : cards) {
            switch(card.getEra()) {
                case 1:
                    era1.add(card);
                break;

                case 2:
                    era2.add(card);
                break;

                case 3:
                    era3.add(card);
                break;
            }
        }

        Collections.shuffle(era1);
        Collections.shuffle(era2);
        Collections.shuffle(era3);

        cards.clear();
        cards.addAll(era1);
        cards.addAll(era2);
        cards.addAll(era3);
    }

    /**
     * Draws the first card in the deck by removing it
     * @return drawn card
     */
    public TribeCard draw() {
        return cards.removeFirst();
    }

    public int size() {
        return cards.size();
    }
}
