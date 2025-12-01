package services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import entity.Comment;
import services.comment.CommentService;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    void reset() {
        commentService.reset();
        assertEquals(0, commentService.getLastComments("1024").size());
    }

    @Test
    void addComment() {
        commentService.reset();
        commentService.addComment(new Comment("AlleksRoy", "1024", "Nice!", new Date()));
        var comments = commentService.getLastComments("1024");
        assertEquals(1, comments.size());
        var score = comments.get(0);
        assertEquals("AlleksRoy", score.getPlayer());
        assertEquals("1024", score.getGame());
        assertEquals("Nice!", score.getComment());
    }

    @Test
    void getLastComments() {
        commentService.reset();
        commentService.addComment(new Comment("AlleksRoy", "1024", "Nice!", new Date()));
        commentService.addComment(new Comment("Yaroslav", "1024", "Good!", new Date()));
        commentService.addComment(new Comment("Valentyn", "1024", "Not bad", new Date()));
        commentService.addComment(new Comment("Dariia", "1024", "Nice!", new Date()));

        var comments = commentService.getLastComments("1024");
        assertEquals(3, comments.size());
        var comment = comments.get(0);
        assertEquals("Dariia", comment.getPlayer());
        assertEquals("1024", comment.getGame());
        assertEquals("Nice!", comment.getComment());

        comment = comments.get(1);
        assertEquals("Valentyn", comment.getPlayer());
        assertEquals("1024", comment.getGame());
        assertEquals("Not bad", comment.getComment());

        comment = comments.get(2);
        assertEquals("Yaroslav", comment.getPlayer());
        assertEquals("1024", comment.getGame());
        assertEquals("Good!", comment.getComment());
    }
}
