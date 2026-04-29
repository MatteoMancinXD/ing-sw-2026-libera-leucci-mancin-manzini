package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.ServerInterface;
import it.polimi.ingsw.view.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RmiClientTest {

    ui ui1 = new cli("giacomo");
    RmiClient client1;

    private RmiClient client;
    private Registry registry;
    private GameManager mngr;

    @BeforeEach
    void setUp() throws RemoteException {

        try {
            registry = LocateRegistry.createRegistry(1099);
            ServerInterface serverStub = new VirtualRMIServer(mngr);
            registry.rebind("MesosServer", serverStub);

        } catch (ExportException e) { //if the registry already exists
            registry = LocateRegistry.getRegistry(1099);
        }


        try {
            client1 = new RmiClient(ui1,"giacomo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructor() throws RemoteException {
        cli ui2 = new cli("andrea");
        RmiClient client2 = new RmiClient(ui2,"andrea");
    }

    @Test
    public void testStartConnection() throws RemoteException {

        assertDoesNotThrow(() -> {  //on success
            client1.startConnection("localhost", 1099);
        });

    }

    @Test
    void testStartConnectionFailure() throws RemoteException {
        TestUI testUi = new TestUI();
        RmiClient client = new RmiClient(testUi, "Riccardo");

        client.startConnection("localhost", 9999); //failure to access non-existing port
        assertEquals("Failed to connect to RMI server", testUi.getLastErrorMessage());

    }

    @Test
    public void requestAvailableGamesTest() throws RemoteException {
        assertDoesNotThrow(() -> {
           client1.requestAvailableGames();
        });
    }

}
