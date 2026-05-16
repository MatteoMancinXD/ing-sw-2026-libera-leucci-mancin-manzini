package it.polimi.ingsw.view;

public enum CliState {
    LOBBY,      //fase di scelta del game/creazione del game
    TOTEM,      //scelta del totem
    STARTING,   //aspetto inizio partita
    WAITING,    //tocca a un avversario
    PLACING,    //fase placing totem
    DRAWING,    //fase pescaggio carte
    END_GAME 
}
