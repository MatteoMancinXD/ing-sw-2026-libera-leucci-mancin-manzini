package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.ServerInterface;
import it.polimi.ingsw.view.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RmiClientTest {

    ui ui1 = new cli("giacomo"); //real cli
    ui ui3 = new cli("matteo");
    ui ui4 = new cli("cesare");
    RmiClient client1;
    RmiClient client3;
    RmiClient client4;

    TestUI ui2 = new TestUI(); //test ui
    RmiClient client2;

    private RmiClient client;
    private Registry registry;
    private GameManager mngr;

    Map<Integer, GameController> availableGames = new HashMap<>();
    Map<Integer, GameController> startedGames = new  HashMap<>();
    Map<String, GameSession> sessions =  new HashMap<>() ;



    @BeforeEach
    void setUp() throws RemoteException {

        try {
            registry = LocateRegistry.createRegistry(1099);

        } catch (ExportException e) { //if the registry already exists
            registry = LocateRegistry.getRegistry(1099);
        }

        mngr = new GameManager(availableGames,startedGames,sessions);
        ServerInterface serverStub = new VirtualRMIServer(mngr);
        registry.rebind("MesosServer", serverStub);

        try {
            client1 = new RmiClient(ui1,"giacomo"); //real cli
            client2 = new RmiClient(ui2, "riccardo"); // test ui  (serverStub is null)
            client3 = new RmiClient(ui3,"matteo"); //real cli
            client4 = new RmiClient(ui4,"cesare"); //real cli

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructor() throws RemoteException {
        cli ui3 = new cli("andrea");
        RmiClient client3 = new RmiClient(ui3,"andrea");
    }

    @Test
    public void testStartConnection() throws RemoteException {

        assertDoesNotThrow(() -> {  //on success
            client1.startConnection("localhost", 1099); //real cli
            client2.startConnection("localhost", 1099); //test ui
        });

    }

    @Test
    void testStartConnectionFailure() throws RemoteException { //it works
        //test ui
        //client2.startConnection("localhost", 9999); //failure to access non-existing port
        //assertEquals("Failed to connect to RMI server", ui2.getLastErrorMessage());

    }

    @Test
    public void requestAvailableGamesTest() throws RemoteException {
        assertDoesNotThrow(() -> {
            client1.startConnection("localhost", 1099);
            client1.requestAvailableGames();
        });

        client2.startConnection("localhost", 1099);
        client2.requestAvailableGames();
        assertTrue(ui2.getLastMessage().contains("Available games:"));
    }

    @Test
    public void createAndJoinGameTest() throws RemoteException {
        assertDoesNotThrow(() -> {
            client1.startConnection("localhost", 1099);
            client1.createGame("giacomo",3);
        });

        client3.startConnection("localhost", 1099);
        client3.requestAvailableGames();
        client3.joinGame("matteo",1);

        client4.startConnection("localhost", 1099);
        client4.requestAvailableGames();
        client4.joinGame("matteo",1);
    }

    @Test
    public void createGameFailureTest() throws RemoteException {
        assertThrows(Exception.class,() -> {
            client1.startConnection("localhost", 1099);
            client1.createGame("giacomo",0);
            client1.createGame("giacomo",-3);
        });
        client3.startConnection("localhost", 1099);
        client3.requestAvailableGames();
    }

    @Test
    public void disconnectTest() throws RemoteException {
        client1.startConnection("localhost", 1099);
        client1.createGame("giacomo",3);

        client3.startConnection("localhost", 1099);
        client3.requestAvailableGames();
        client3.joinGame("matteo",1);

        client4.startConnection("localhost", 1099);
        client4.requestAvailableGames();
        client4.joinGame("cesare",1);


        client4.disconnect();
        client3.disconnect();
        client4.createGame("cesare",2);
        client3.requestAvailableGames();
        client3.joinGame("matteo",1);
    }

    @Test
    public void methodTest() throws RemoteException {
        GameManager checkGames = new GameManager(availableGames,startedGames,sessions);

        client1.startConnection("localhost", 1099);
        client1.createGame("giacomo",3);

        client3.startConnection("localhost", 1099);
        client3.requestAvailableGames();
        client3.joinGame("matteo",1);

        client4.startConnection("localhost", 1099);
        client4.requestAvailableGames();
        client4.joinGame("cesare",1);





    }

}
