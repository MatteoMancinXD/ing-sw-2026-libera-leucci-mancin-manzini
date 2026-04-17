package it.polimi.ingsw.model;


public abstract class EventCard extends TribeCard{

    public EventCard(int id, int era) {
    super(id, era,2); //EventCards are 12 (4events * 3eras) and are always used in the game, regardless the number of players, so minPlayers = 2
    }
    protected abstract void solveEventCard(Player player);  // ex. huntCardEventII.solveEventCard(game1,giacomo)
}
