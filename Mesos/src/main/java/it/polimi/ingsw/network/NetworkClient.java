package it.polimi.ingsw.network;

public interface NetworkClient {

    void requestAvailableGames();

    void createGame(String nickname, int numPlayers);

    void joinGame(String nickname, int gameID);

    void askToDrawCard(boolean row, int index);

    void askToPlaceTotem(int pos);

    void askToSkipBonus();

    void sendChatMessage(String message);

    void askToEndTurn();

    void disconnect();

}
