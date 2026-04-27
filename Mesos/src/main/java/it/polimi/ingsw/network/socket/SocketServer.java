package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class SocketServer extends Thread{

    private final int port;
    private GameManager mngr;

    public SocketServer(int port, GameManager mngr) {
        this.port = port;
        this.mngr = mngr;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection received");

                SocketClientHandler handler = new SocketClientHandler(clientSocket, mngr);
                new Thread(handler).start();
            }
        }catch (IOException e) {
            System.err.println("Critical Error: "+e.getMessage());
        }
    }
}
