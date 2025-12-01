package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@NamedQuery(name = "User.resetUsers", query = "DELETE FROM Users")
@NamedQuery(name = "User.isLoginUsed", query = "SELECT a.login FROM Users a WHERE a.game =: game AND a.login =: login")
@NamedQuery(name = "User.isEmailUsed", query = "SELECT a.email FROM Users a WHERE a.game =: game AND a.email =: email")
@NamedQuery(name = "User.getUserPassword", query = "SELECT a.password FROM Users a WHERE a.game =: game AND a.login =: login")
@NamedQuery(name = "User.getRegistrationDate", query = "SELECT u.registrationDate FROM Users u WHERE u.game = :game AND u.login = :login")
@NamedQuery(name = "User.getUserEmail", query = "SELECT u.email FROM Users u WHERE u.game = :game AND u.login = :login")
@NamedQuery(name = "User.changeLogin", query = "UPDATE Users u SET u.login = :newLogin WHERE u.game = :game AND u.login = :oldLogin")
@NamedQuery(name = "User.changePassword", query = "UPDATE Users u SET u.password = :newPassword WHERE u.game = :game AND u.login = :login")
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String password;

    private String game;

    private Date registrationDate;

    public Users(String login, String email, String password, String game, Date registrationDate) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.game = game;
        this.registrationDate = registrationDate;
    }

}
