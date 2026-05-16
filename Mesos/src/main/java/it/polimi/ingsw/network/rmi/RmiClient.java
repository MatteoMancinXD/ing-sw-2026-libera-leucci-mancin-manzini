package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.ServerInterface;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.view.ui;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RmiClient extends UnicastRemoteObject implements ClientRemote, NetworkClient {

    private String token;
    private final String nickname;
    private ServerInterface serverStub;
    private final ui userInterface;

    public RmiClient(ui userInterface,  String nickname) throws  RemoteException {
        super();
        this.nickname = nickname;
        this.userInterface = userInterface;
    }

    public void startConnection(String serverIP, int port) throws RemoteException {
        try {
            userInterface.showMessage("Connecting to " + serverIP + ":" + port);
            Registry registry = LocateRegistry.getRegistry(serverIP, port);
            serverStub = (ServerInterface) registry.lookup("MesosServer");
            userInterface.showMessage("Connected and logged with success to " + serverIP + ":" + port);
        }
        catch (Exception e) {
            userInterface.showError("Failed to connect to RMI server");
            e.printStackTrace();
        }
    }

    @Override
    public void requestAvailableGames() throws RemoteException{
        try {
            Map<Integer,String> games = serverStub.getAvailableGames();
            userInterface.showMessage("Available games:");

            for(Map.Entry<Integer,String> game : games.entrySet()) {
                userInterface.showMessage("Game #" +  game.getKey() + ": " + game.getValue() + "'s game");
            }
        }catch (Exception e) {
            userInterface.showError("Failed to request available games");
            e.printStackTrace();
        }
    }

    public void createGame(String nickName,int numPlayers) throws RemoteException{
        Map<Integer, String> games = serverStub.getAvailableGames();
        if (numPlayers < 2 || numPlayers > 5) {
            throw new NumberFormatException("Invalid number of players! ");
        }
        try {
            //see if else in login method in VirtualRMIServer
            int id = serverStub.createGame(this.nickname, numPlayers, this);
            userInterface.showMessage("You created the game with id: " + id);
        } catch (RemoteException e) {
            userInterface.showError("Failed to create game!");
        }
    }

    public void joinGame(String nickName, int gameID) throws RemoteException{
       try {
            serverStub.joinGame(this.nickname, gameID,this);
           userInterface.showMessage("You joined the game with id: "+gameID);
        } catch (RemoteException e) {
           userInterface.showError("Failed to join game with id: " + gameID);
        }
    }

    public void askToSkipBonus(){
        try{
            serverStub.skipBonusPick(this.token);
        }catch (RemoteException e){
            userInterface.showError("Failed to ask to skip bonus");
        }

    }

    public void askToEndTurn(){
        try{
            if(token!=null){
                serverStub.serverEndTurn(this.token);
                userInterface.showMessage("End turn request sent");
            }
        }catch (RemoteException e){
            userInterface.showMessage("End turn request failed");
        }
    }

    public void sendChatMessage(String message) throws RemoteException{
        try {
            if (token != null) {
                serverStub.sendChatMessage(this.token, message);
            }
        } catch (RemoteException e) {
            userInterface.showError("Error couldn't send chat message");
        }
    }
    public void receiveChatMessage(String sender, String message) throws RemoteException {
        userInterface.showChatMessage(sender, message);
    }


    public void disconnect(){
        try {
            if (token != null) {
                serverStub.logout(this.token);
                this.token = null;
            }
        }catch(RemoteException e){
            userInterface.showMessage("Failed to disconnect from RMI server");
        }
    }

    public void askToDrawCard(boolean row, int idx) {
        try {
            serverStub.drawCard(token, row, idx);
        } catch(RemoteException e) {
            userInterface.showError("Error couldn't ask to draw card");
        }
    }

    public void askToPlaceTotem(int index) {
        try {
            serverStub.placeTotem(token, index);
        } catch(RemoteException e) {
            userInterface.showError("Connection error: Failed to place totem");
        }
    }

    @Override
    public void requestAvailableTotems() {
        try {
            Set<Totem> totems = serverStub.getAvailableTotems(token);
            userInterface.showAvailableTotems(totems);
        } catch(RemoteException e) {
            userInterface.showError("Failed to request available totems");
        }
    }

    @Override
    public void askToSelectTotem(Totem totem) {
        try {
            serverStub.selectTotem(token, totem);
        } catch(RemoteException | IllegalArgumentException e) {
            userInterface.showError("Connection error: Failed to select totem");
        }
    }

    @Override
    public void receiveBoardUpdate(Board board, List<Player> players) throws RemoteException {
        userInterface.updateBoard(board, players);
    }

    @Override
    public void receiveError(String errorMessage) throws RemoteException {
        userInterface.showError("Server error: "+errorMessage);
    }

    @Override
    public void receiveTurnNotification(String currentPlayerNickname, String gamePhase) throws RemoteException {
        userInterface.notifyTurn(currentPlayerNickname, gamePhase);
    }

    @Override
    public void receiveAskBonusExtraPick() throws RemoteException {
        userInterface.showMessage("You have activated ExtraPickBuilding");
        userInterface.showMessage("Choose if you want to draw a bonus card or not");
    }

    @Override
    public void receiveGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException {
        userInterface.notifyEndGame(rankings, globalRanks);
    }
    @Override
    public void receiveMessage(String message) throws RemoteException {
        userInterface.showMessage(message);
    }

    @Override
    public void receiveToken(String token)  throws RemoteException {
        this.token = token;
    }

    @Override
    public void receiveEventResolution(EventCard card) throws RemoteException {
        userInterface.showMessage("Event resolved: " + card.getShortString());
    }

    @Override
    public void ping() throws RemoteException {}

    @Override
    public void onTotemSelected() {
        userInterface.onTotemSelected();
    }

    @Override
    public void onGameParticipation() {
        userInterface.onGameParticipation();
    }
}
