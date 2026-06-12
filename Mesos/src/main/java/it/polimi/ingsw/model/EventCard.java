package it.polimi.ingsw.model;
import java.util.List;

/**
 * Abstract class representing an event card that triggers effects on all players
 * when resolved at the end of a round. Events include Hunt, Paintings, Ritual,
 * and Sustenance. There are 12 event cards in total (4 types x 3 eras),
 * always present in the deck regardless of player count.
 *
 * @see Board#solveEvents(java.util.List, it.polimi.ingsw.controller.GameObserver)
 */
public abstract class EventCard extends TribeCard{

    private boolean finalEvent;

    public void setFinalEvent(boolean finalEvent) {
        this.finalEvent = finalEvent;
    }

    public EventCard(){
        super(0,0,0); //jackson default
    }


    public EventCard(int id, int era,boolean finalEvent) {
    super(id, era,2); //EventCards are 12 (4events * 3eras) and are always used in the game, regardless the number of players, so minPlayers = 2
    }

    /**
     * Resolves this event's effect on a single player, considering all players
     * for events that compare player stats (e.g. Ritual).
     * @param player     the player being affected
     * @param allPlayers all players in the game for comparison-based events
     */
    protected abstract void solveEventCard(Player player,List<Player> allPlayers);  // ex. huntCardEventII.solveEventCard(giacomo,game.getPlayers()) , if game can be used
    @Override
    public boolean isEventCard() { return true;}
}
