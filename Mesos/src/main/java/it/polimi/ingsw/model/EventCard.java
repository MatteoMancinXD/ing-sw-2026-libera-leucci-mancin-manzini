package it.polimi.ingsw.model;
import java.util.List;


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
    protected abstract void solveEventCard(Player player,List<Player> allPlayers);  // ex. huntCardEventII.solveEventCard(giacomo,game.getPlayers()) , if game can be used
}
