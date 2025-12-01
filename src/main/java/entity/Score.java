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
@NamedQuery(name = "Score.reset", query = "DELETE FROM Score")
@NamedQuery(name = "Score.findAllByGame", query = "SELECT s FROM Score s WHERE s.game = :game")
@NamedQuery(name = "Score.findByPlayer", query = "SELECT s FROM Score s WHERE s.game = :game AND s.player = :player")
@NamedQuery(name = "Score.checkIfScoreExist", query = "SELECT s FROM Score s WHERE s.game = :game AND s.player = :player")
@NamedQuery(name = "Score.updatePlayer", query = "UPDATE Score s SET s.player = :newName WHERE s.player = :oldName")
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;

    private String game;

    private int points;

    private int easyPoints;

    private int hardPoints;

    private Date played_at;

    public Score(String player, String game, int points, Date played_at) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.played_at = played_at;
    }
}

