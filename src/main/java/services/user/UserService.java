package services.user;

import entity.Users;

public interface UserService {
    boolean addUser(Users user);

    boolean isLoginUsed(String game, String login);

    boolean isEmailUsed(String game, String email);

    String getUserPassword(String game, String login);

    String getUresRegestationDate(String game, String login);

    String getUserEmail(String game, String login);

    void changeLogin(String game, String oldLogin, String newLogin);

    void changePassword(String game, String login, String newPassword);

    void reset();
}
