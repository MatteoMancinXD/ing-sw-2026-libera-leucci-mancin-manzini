package it.polimi.ingsw.network.db;

import it.polimi.ingsw.model.Player;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManagerDAO {

    private static DatabaseManagerDAO instance;
    private String url;
    private String user;
    private String password;

    private DatabaseManagerDAO() {
        loadProperties();
    }

    public static synchronized DatabaseManagerDAO getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerDAO();
        }
        return instance;
    }

    //recupera le credenziali dal file di connessione al db (root + password)
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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

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