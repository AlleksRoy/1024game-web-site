package services.score;

import entity.Score;
import services.GameStudioException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {
    private static final String INSERT = "INSERT INTO score (player, game, points, played_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT player, game, points, played_at FROM score WHERE game = ? ORDER BY points DESC LIMIT 3";
    private static final String DELETE = "DELETE FROM score";

    private static final String URL = "jdbc:postgresql://localhost:5353/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "Pechenka05";

    @Override
    public void addScore(Score score, String mode) {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(INSERT);
        ) {
            statement.setString(1, score.getPlayer());
            statement.setString(2, score.getGame());
            statement.setInt(3, score.getPoints());
            statement.setTimestamp(4, new Timestamp(score.getPlayed_at().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {

    }

    @Override
    public List<Score> getTop3Scores(String game, String mode) {
        return List.of();
    }

    @Override
    public List<Score> getTopScore(String game, String mode) {
        var scores = new ArrayList<Score>();
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    scores.add(new Score(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getTimestamp(4)));
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
        return scores;
    }

    @Override
    public Integer getScoreByPlayer(String game, String player, String mode) {
        return 0;
    }

    @Override
    public void reset() {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }
}

