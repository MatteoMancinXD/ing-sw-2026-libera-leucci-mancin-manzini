package it.polimi.ingsw.model;


public abstract class EventCard extends TribeCard{
    
    public EventCard(int era, int minPlayers) {
    super(era, minPlayers);
    }
    abstract void solveEventCard(Player player);  // ex. huntCardEventII.solveEventCard(giacomo)
}
