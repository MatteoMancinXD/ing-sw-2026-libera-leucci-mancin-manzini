package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 * Remote interface exposed by the RMI client to the server.
 * The server calls these methods to push notifications, board updates,
 * and game events to the client over RMI.
 *
 * @see RmiClient
 * @see VirtualRMIView
 */
public interface ClientRemote extends Remote{
    /**
     * Receives an event card resolution notification from the server.
     * @param card the event card that was resolved
     */
    void receiveEventResolution(EventCard card) throws RemoteException;

    /**
     * Receives an updated board state and player list from the server.
     * @param board   snapshot of the current board state
     * @param players list of player snapshots with current stats
     */
    void receiveBoardUpdate(BoardSnapshot board, List<PlayerSnapshot> players) throws RemoteException;

    /**
     * Receives an error message from the server.
     * @param errorMessage description of the error
     */
    void receiveError(String errorMessage) throws RemoteException;

    /**
     * Receives a turn notification indicating whose turn it is and the current phase.
     * @param currentPlayerNickname nickname of the player whose turn it is
     * @param gamePhase             current game phase (PLACEMENT, RESOLUTION, or EXTRA_PICK)
     * @param round                 current round number
     * @param era                   current era number
     */
    void receiveTurnNotification(String currentPlayerNickname, String gamePhase, int round, int era) throws RemoteException;

    /**
     * Notifies the client that they may use their Extra Pick building bonus.
     */
    void receiveAskBonusExtraPick() throws RemoteException;

    /**
     * Receives a generic text message from the server.
     * @param message the message content
     */
    void receiveMessage(String message) throws RemoteException;

    /**
     * Receives the final game rankings and global leaderboard at game end.
     * @param rankings    ordered list of player nicknames by final score
     * @param globalRanks global leaderboard entries for games with the same player count
     */
    void receiveGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException;

    /**
     * Receives the authentication token assigned by the server after login.
     * @param token the unique session token
     */
    void receiveToken(String token) throws RemoteException;

    /**
     * Receives a chat message from another player.
     * @param sender  nickname of the message sender
     * @param message the chat message content
     */
    void receiveChatMessage(String sender, String message) throws RemoteException;

    /**
     * Server-initiated heartbeat to detect client disconnections.
     */
    void ping() throws RemoteException;

    /**
     * Notifies the client that their totem selection was accepted.
     */
    void onTotemSelected() throws RemoteException;

    /**
     * Notifies the client that they have joined a game and should select a totem.
     * @param totems the set of totem colors still available
     */

    void onGameParticipation(Set<Totem> totems) throws RemoteException;

    /**
     * Receives an updated set of available totem colors after another player's selection.
     * @param totems the remaining available totem colors
     */
    void receiveAvailableTotemsUpdate(Set<Totem> totems) throws RemoteException;
}
