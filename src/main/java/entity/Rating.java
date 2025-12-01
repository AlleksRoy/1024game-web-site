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
@NamedQuery(name = "Rating.reset", query = "DELETE FROM Rating")
@NamedQuery(name = "Rating.getAverageRating", query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
@NamedQuery(name = "Rating.getRating", query = "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery(name = "Rating.checkRating", query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery(name = "Rating.updatePlayer", query = "UPDATE Rating r SET r.player = :newName WHERE r.player = :oldName")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;

    private String game;

    private int rating;

    private Date rated_at;

    public Rating(String player, String game, int rating, Date rated_at) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.rated_at = rated_at;
    }
}
