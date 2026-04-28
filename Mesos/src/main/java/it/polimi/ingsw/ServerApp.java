package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.rmi.VirtualRMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerApp
{
    public static void runRMIServer(GameManager mngr) {
        try {
            System.out.println("[SERVER] Starting RMI server...");
            VirtualRMIServer lobbyMaster = new VirtualRMIServer(mngr);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MesosServer", lobbyMaster);

            System.out.println("[SERVER] RMI register launched on port 1099.");
            System.out.println("[SERVER] Waiting for connections...");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void runSocketServer(GameManager mngr) {
        System.out.println("[SERVER] Starting socket server...");
        SocketServer socketServer = new SocketServer(5000, mngr);

        socketServer.start();
    }

    public static void main( String[] args )
    {
        Map<Integer, GameController> availableGames = new ConcurrentHashMap<>();
        Map<Integer, GameController> startedGames = new ConcurrentHashMap<>();
        Map<String, GameSession> sessions = new ConcurrentHashMap<>();

        GameManager mngr = new GameManager(availableGames, startedGames, sessions);

        Thread rmiServerThread = new Thread(() -> { runRMIServer(mngr); });
        rmiServerThread.start();

        Thread socketServerThread = new Thread(() -> { runSocketServer(mngr); });
        socketServerThread.start();
    }
}
