package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RmiClient extends UnicastRemoteObject implements ClientRemote, NetworkClient {

    private String token;
    private final String nickname;
    private final ClientController uiController; //Controller lato client - sostanzialmente javafx
    private ServerInterface serverStub;

    public RmiClient(ClientController uiController, String nickname) throws  RemoteException {
        super();
        this.uiController = uiController;
        this.nickname = nickname;
    }

    public void startConnection(String serverIP, int port, int gameId, int numPlayers) throws RemoteException {
        try {
            System.out.println("Connecting to " + serverIP + ":" + port);
            Registry registry = LocateRegistry.getRegistry(serverIP, port);
            serverStub = (ServerInterface) registry.lookup("MesosServer");
            token = serverStub.login(nickname, gameId, numPlayers, this);
            System.out.println("Connected and logged with success to " + serverIP + ":" + port);
        }
        catch (Exception e) {
            System.err.println("Failed to connect to RMI server");
            e.printStackTrace();
        }
    }

    public void requestAvailableGames() throws RemoteException{
        try {
            Map<Integer,String> games = serverStub.getAvailableGames();
            System.out.println("Available games:"+games);
        }catch (Exception e) {
            System.err.println("Failed to request available games");
            e.printStackTrace();
        }
    }

    public void createGame(String nickName,int numPlayers) throws RemoteException{
        Map<Integer, String> games = serverStub.getAvailableGames();

        int gameID = serverStub.askNewGameID();

            try {
                //see if else in login method in VirtualRMIServer
                this.token = serverStub.login(this.nickname, numPlayers, gameID,this);
                System.out.println("You created the game with id: "+gameID);
            } catch (RemoteException e) {
                System.err.println("Failed to create game with ID " + gameID + "!");
            }

    }

    public void joinGame(String nickName, int gameID) throws RemoteException{
        Map<Integer,String> games = serverStub.getAvailableGames();
        try {

            this.token = serverStub.login(this.nickname, gameID, 0,this);
            System.out.println("You joined the game with id: "+gameID);
        } catch (RemoteException e) {
            System.err.println("Failed to join game with id: " + gameID);
        }

    }

    public void askToSkipBonus(){

    }

    public void askToEndTurn(){

    }

    public void sendChatMessage(String Message){

    }

    public void disconnect(){

    }

    public void askToDrawCard(boolean row, int idx) {
        try {
            serverStub.drawCard(token, row, idx);
        } catch(RemoteException e) {
            System.err.println("Failed to ask to draw card, lost connection");
        }
    }

    public void askToPlaceTotem(int index) {
        try {
            serverStub.placeTotem(token, index);
        } catch(RemoteException e) {
            System.err.println("Connection error: Failed to place totem");
        }
    }

    @Override
    public void receiveBoardUpdate(Board board) throws RemoteException {
        System.out.println("Received board update \n");
        //uiController aggiorna la view
    }

    @Override
    public void receiveError(String errorMessage) throws RemoteException {
        System.out.println("Server Error: "+errorMessage+ "\n");
    }

    @Override
    public void receiveTurnNotification(String currentPlayerNickname) throws RemoteException {
        System.out.println("Turn changed. It's now "+currentPlayerNickname+ " 's turn");
        if (currentPlayerNickname.equals(nickname)) {
            System.out.println("It's your turn");
        }
    }

    @Override
    public void receiveAskBonusExtraPick() throws RemoteException {
        System.out.println("You have activated ExtraPickBuilding");
        System.out.println("Choose if you want to draw a bonus card or not");
    }

    @Override
    public void receiveGameEnd(List<String> rankings) throws RemoteException {
        System.out.println("Game ended. Rankings: ");
        for(int i = 0; i < rankings.size(); i++) {
            System.out.println((i+1)+" place: "+rankings.get(i));
        }
    }
    @Override
    public void receiveMessage(String message) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {}
}
