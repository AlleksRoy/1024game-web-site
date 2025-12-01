package services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import entity.Users;

public class UserServiceRestClient implements UserService {
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean addUser(Users user) {
        restTemplate.postForEntity(url, user, Users.class);
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
        return restTemplate.getForObject(url + "/" + game + "/" + login, String.class);
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
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
