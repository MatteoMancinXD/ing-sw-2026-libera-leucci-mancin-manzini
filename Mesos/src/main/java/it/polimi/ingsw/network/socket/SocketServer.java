package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread{

    private final int port;
    private final GameController gameController;

    public SocketServer(int port, GameController gameController) {
        this.port = port;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection received");

                SocketClientHandler handler = new SocketClientHandler(clientSocket, gameController);
                new Thread(handler).start();
            }
        }catch (IOException e) {
            System.err.println("Critical Error: "+e.getMessage());
        }
    }

}
