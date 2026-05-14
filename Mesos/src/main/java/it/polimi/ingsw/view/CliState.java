package it.polimi.ingsw.view;

public enum CliState {
    LOBBY, //fase di scelta del game/creazione del game
    WAITING, //tocca un avversario
    PLACING, //fase placing totem
    DRAWING,     //fase pescaggio carte
    END_GAME        //fine della partita
}
