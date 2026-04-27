package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.rmi.VirtualRMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerApp
{
    public static void runRMIServer(Map<Integer, GameController> lobbies) {
        try {
            System.out.println("[SERVER] Starting RMI server...");
            VirtualRMIServer lobbyMaster = new VirtualRMIServer(lobbies);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MesosServer", lobbyMaster);

            System.out.println("[SERVER] RMI register launched on port 1099.");
            System.out.println("[SERVER] Waiting for connections...");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void runSocketServer(Map<Integer, GameController> lobbies) {
        System.out.println("[SERVER] Starting socket server...");
        SocketServer socketServer = new SocketServer(5000, lobbies);

        socketServer.start();
    }

    public static void main( String[] args )
    {
        Map<Integer, GameController> lobbies = new ConcurrentHashMap<Integer, GameController>();
        Thread rmiServerThread = new Thread(() -> { runRMIServer(lobbies); });
        rmiServerThread.start();

        Thread socketServerThread = new Thread(() -> { runSocketServer(lobbies); });
        socketServerThread.start();
    }
}
