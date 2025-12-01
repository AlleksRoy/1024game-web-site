package services.user;

import entity.Users;
import services.GameStudioException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceJDBC implements UserService {
    public static final String DELETE = "DELETE FROM account";
    public static final String INSERT = "INSERT INTO score (game, email, login, password) VALUES (?, ?, ?, ?)";
    public static final String SELECT_LOGIN = "SELECT  login FROM account WHERE game = ? AND login ?";
    public static final String SELECT_PASSWORD = "SELECT game, password FROM account WHERE game = ? AND login = ?";

    private static final String URL = "jdbc:postgresql://localhost:5353/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "Pechenka05";

    @Override
    public boolean addUser(Users user) {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(INSERT);
        ) {
            statement.setString(1, user.getGame());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

        return true;
    }

    @Override
    public boolean isLoginUsed(String game, String login) {
        return false;
    }

    @Override
    public boolean isEmailUsed(String game, String email) {
        return false;
    }

    @Override
    public String getUserPassword(String game, String login) {
        try (var connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             var statement = connection.prepareStatement(SELECT_PASSWORD)
        ) {
            statement.setString(1, game);
            statement.setString(2, login);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getString(4);
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public String getUresRegestationDate(String game, String login) {
        return "";
    }

    @Override
    public String getUserEmail(String game, String login) {
        return "";
    }

    @Override
    public void changeLogin(String game, String oldLogin, String newLogin) {

    }

    @Override
    public void changePassword(String game, String login, String newPassword) {

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
