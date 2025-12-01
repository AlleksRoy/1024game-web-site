package services.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import entity.Users;

import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean addUser(Users user) {
        boolean loginUsed;
        try {
            Object used = entityManager.createNamedQuery("User.isLoginUsed")
                    .setParameter("game", user.getGame())
                    .setParameter("login", user.getLogin())
                    .getSingleResult();
            loginUsed = (used != null);
        } catch (NoResultException e) {
            loginUsed = false;
        }

        boolean emailUsed;
        try {
            Object used = entityManager.createNamedQuery("User.isEmailUsed")
                    .setParameter("game", user.getGame())
                    .setParameter("email", user.getEmail())
                    .getSingleResult();
            emailUsed = (used != null);
        } catch (NoResultException e) {
            emailUsed = false;
        }

        if (loginUsed || emailUsed) {
            return false;
        }

        entityManager.persist(user);
        return true;
    }

    @Override
    public boolean isLoginUsed(String game, String login) {
        try {
            entityManager.createNamedQuery("User.isLoginUsed", String.class)
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public boolean isEmailUsed(String game, String email) {
        try {
            entityManager.createNamedQuery("User.isEmailUsed", String.class)
                    .setParameter("game", game)
                    .setParameter("email", email)
                    .getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public String getUserPassword(String game, String login) {
        Object result = null;
        try {
            result = entityManager.createNamedQuery("User.getUserPassword")
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        if (result == null) {
            return null;
        } else return result.toString();
    }

    @Override
    public String getUresRegestationDate(String game, String login) {
        try {
            Date date = entityManager.createNamedQuery("User.getRegistrationDate", Date.class)
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
            if (date == null) {
                return null;
            }
            return new SimpleDateFormat("d.M.yyyy").format(date);
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public String getUserEmail(String game, String login) {
        try {
            return entityManager.createNamedQuery("User.getUserEmail", String.class)
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void changeLogin(String game, String oldLogin, String newLogin) {
        entityManager.createNamedQuery("User.changeLogin")
                .setParameter("newLogin", newLogin)
                .setParameter("game", game)
                .setParameter("oldLogin", oldLogin)
                .executeUpdate();
    }

    @Override
    public void changePassword(String game, String login, String newPassword) {
        entityManager.createNamedQuery("User.changePassword")
                .setParameter("newPassword", newPassword)
                .setParameter("game", game)
                .setParameter("login", login)
                .executeUpdate();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
