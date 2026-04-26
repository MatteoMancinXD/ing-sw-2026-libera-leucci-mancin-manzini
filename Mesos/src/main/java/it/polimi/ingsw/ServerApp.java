package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.rmi.VirtualRMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 *
 */
public class ServerApp
{
    public static void main( String[] args )
    {
        Map<Integer, GameController> lobbies = new ConcurrentHashMap<Integer, GameController>();
        try {
            System.out.println("[SERVER] Starting server...");
            VirtualRMIServer lobbyMaster = new VirtualRMIServer(lobbies);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MesosServer", lobbyMaster);

            System.out.println("[SERVER] RMI register launched on port 1099.");
            System.out.println("[SERVER] Waiting for connections...");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
