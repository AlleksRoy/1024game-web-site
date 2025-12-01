package services.rating;

import entity.Rating;
import services.GameStudioException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingServiceJDBC implements RatingService {
    private static final String INSERT = "INSERT INTO rating (player, game, rating, rated_at) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE rating SET rating = ?, rated_at = ? WHERE player = ? AND game = ?";
    private static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
    private static final String SELECT = "SELECT * FROM rating WHERE game = ? AND player = ?";
    private static final String DELETE = "DELETE FROM rating";

    private static final String URL = "jdbc:postgresql://localhost:5353/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "Pechenka05";

    @Override
    public void setRating(Rating rating) {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            connection.setAutoCommit(false);

            try (var updateStatement = connection.prepareStatement(UPDATE)) {
                updateStatement.setInt(1, rating.getRating());
                updateStatement.setTimestamp(2, new Timestamp(rating.getRated_at().getTime()));
                updateStatement.setString(3, rating.getPlayer());
                updateStatement.setString(4, rating.getGame());

                int affectedRows = updateStatement.executeUpdate();
                if (affectedRows == 0) {
                    try (var insertStatement = connection.prepareStatement(INSERT)) {
                        updateStatement.setString(1, rating.getPlayer());
                        updateStatement.setString(1, rating.getGame());
                        updateStatement.setInt(3, rating.getRating());
                        updateStatement.setTimestamp(4, new Timestamp(rating.getRated_at().getTime()));
                        insertStatement.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                new GameStudioException(e);
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {

    }

    @Override
    public double getAverageRating(String game) {
        double result = 0.0;
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(SELECT_AVERAGE);
        ) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
        return result;
    }

    @Override
    public int getRating(String game, String player) {
        int result = 0;
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
        return result;
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
