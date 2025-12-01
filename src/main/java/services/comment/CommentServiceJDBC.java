package services.comment;

import entity.Comment;
import services.GameStudioException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    private static final String INSERT = "INSERT INTO comment (player, game, comment, commented_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT player, game, comment, commented_at FROM comment WHERE game = ? ORDER BY ident DESC LIMIT 3";
    private static final String DELETE = "DELETE FROM comment";

    private static final String URL = "jdbc:postgresql://localhost:5353/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "Pechenka05";

    @Override
    public void addComment(Comment comment) {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(INSERT);
        ) {
            statement.setString(1, comment.getPlayer());
            statement.setString(2, comment.getGame());
            statement.setString(3, comment.getComment());
            statement.setTimestamp(4, new Timestamp(comment.getCommented_at().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {

    }

    @Override
    public List<Comment> getLastComments(String game) {
        var comments = new ArrayList<Comment>();
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getTimestamp(4)));
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

        return comments;
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
