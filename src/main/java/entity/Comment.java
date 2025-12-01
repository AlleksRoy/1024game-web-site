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
@NamedQuery(name = "Comment.reset", query = "DELETE FROM Comment")
@NamedQuery(name = "Comment.checkComment", query = "SELECT c FROM Comment c WHERE c.game = :game AND c.player = :player")
@NamedQuery(name = "Comment.getComments", query = "SELECT s FROM Comment s WHERE s.game = :game ORDER BY s.ident DESC")
@NamedQuery(name = "Comment.updatePlayer", query = "UPDATE Comment c SET c.player = :newName WHERE c.player = :oldName")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;

    private String game;

    private String comment;

    private Date commented_at;

    public Comment(String player, String game, String comment, Date commented_at) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commented_at = commented_at;
    }
}

