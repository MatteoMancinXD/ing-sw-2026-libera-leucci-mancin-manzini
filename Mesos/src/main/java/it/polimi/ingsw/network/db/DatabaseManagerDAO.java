package it.polimi.ingsw.network.db;

import it.polimi.ingsw.model.Player;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Data Access Object (DAO) responsible for managing database interactions.
 * * This class implements the Singleton design pattern to ensure a single, centralized
 * access point to the database throughout the application's lifecycle. It handles:
 * - Loading connection properties from the {@code db.properties} file.
 * - Establishing connections to the SQL database.
 * - Persisting game results and player scores at the end of a match.
 * - Retrieving leaderboard statistics based on the game mode (number of players).
 */
public class DatabaseManagerDAO {

    private static DatabaseManagerDAO instance;
    private String url;
    private String user;
    private String password;

    private DatabaseManagerDAO() {
        loadProperties();
    }
    /**
     * Retrieves the singleton instance of the {@code DatabaseManagerDAO}.
     * * The method is synchronized to guarantee thread safety during the initial
     * instantiation in a multi-threaded environment.
     *
     * @return the single, shared instance of {@code DatabaseManagerDAO}
     */
    public static synchronized DatabaseManagerDAO getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerDAO();
        }
        return instance;
    }

    /**
     * Loads the database connection credentials (URL, username, and password)
     * from the {@code db.properties} configuration file located in the resources folder.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(input);
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
        } catch (Exception e) {
            System.err.println("db.properties file loading error");
            e.printStackTrace();
        }
    }
    /**
     * Establishes and returns a new connection to the database using the loaded properties.
     *
     * @return a {@link Connection} object to the database
     * @throws SQLException if a database access error occurs or the url is null
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Persists the results of a completed match into the database.
     * * This method performs a series of database operations to save the game state:
     * 1. Creates a new entry in the {@code game} table with the current date and player count.
     * 2. Retrieves the auto-generated unique identifier for the new game.
     * 3. Ensures every {@link Player} is registered in the {@code user} table.
     * 4. Creates entries in the {@code play} table to link each player to the game.
     *
     * @param players    the list of {@link Player}s who participated in the match
     * @param numPlayers the total number of players in the match (defines the game mode)
     * @throws SQLException if a database access error occurs or if the game ID generation fails
     */
    public void saveMatchResults(List<Player> players, int numPlayers) throws SQLException {

        try (Connection con = getConnection()) {

            String insertGameQuery = "INSERT INTO game (date, numPlayer) VALUES (?, ?)";
            int idGame = -1;

            try (PreparedStatement stmtGame = con.prepareStatement(insertGameQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmtGame.setDate(1, new Date(System.currentTimeMillis())); // Data di oggi
                stmtGame.setInt(2, numPlayers);
                stmtGame.executeUpdate();

                try (ResultSet generatedKeys = stmtGame.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idGame = generatedKeys.getInt(1);
                    }
                }
            }

            if (idGame == -1) {
                throw new SQLException("Game ID generation error");
            }

            String insertUserQuery = "INSERT IGNORE INTO user (nickname) VALUES (?)";
            String insertPlayQuery = "INSERT INTO play (nickname, idGame, prestigePoints) VALUES (?, ?, ?)";

            try (PreparedStatement stmtUser = con.prepareStatement(insertUserQuery);
                 PreparedStatement stmtPlay = con.prepareStatement(insertPlayQuery)) {

                for (Player p : players) {
                    // Aggiunge l'utente (se esiste già, IGNORE evita il crash)
                    stmtUser.setString(1, p.getNickname());
                    stmtUser.executeUpdate();

                    // Associa il punteggio alla partita
                    stmtPlay.setString(1, p.getNickname());
                    stmtPlay.setInt(2, idGame);
                    stmtPlay.setInt(3, p.getPrestige());
                    stmtPlay.executeUpdate();
                }
            }

            System.out.println("Successfully inserted game stats, gameID = " + idGame);

        } catch (SQLException e) {
            System.err.println("SQL error during game stats uploading");
            e.printStackTrace();
        }

    }
    /**
     * Retrieves the leaderboard for a specific game mode, ranked by total prestige points.
     * * The leaderboard is calculated by summing the prestige points obtained by each player
     * across all games played with the specified number of players. The result is ordered
     * in descending order (highest score first).
     *
     * @param numPlayers the number of players defining the game mode (e.g., 2, 3, or 4 players)
     * @return a list of {@link LeaderboardEntryBean} objects representing the ranked players
     */
    public List<LeaderboardEntryBean> getLeaderboardByPlayerCount(int numPlayers) {
        List<LeaderboardEntryBean> leaderboard = new ArrayList<>();

        String query = "SELECT p.nickname, SUM(p.prestigePoints) AS total " +
                        "FROM play p " +
                        "JOIN game g ON p.idGame = g.idGame " +
                        "WHERE g.numPlayer = ? " +
                        "GROUP BY p.nickname " +
                        "ORDER BY total DESC";

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, numPlayers);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nick = rs.getString("nickname");
                    int score = rs.getInt("total");

                    leaderboard.add(new LeaderboardEntryBean(nick, score));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error during rankings fetching");
            e.printStackTrace();
        }
        return leaderboard;
    }


}