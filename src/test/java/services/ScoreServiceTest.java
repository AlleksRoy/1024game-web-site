package services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import entity.Score;
import services.score.ScoreService;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ScoreServiceTest {
    @Autowired
    private ScoreService scoreService;

    @Test
    void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScore("1024", "").size());
    }

    @Test
    void addScore() {
        scoreService.reset();
        scoreService.addScore(new Score("jaro", "1024", 150, new Date()), "");
        var scores = scoreService.getTopScore("1024", "");
        assertEquals(1, scores.size());
        var score = scores.get(0);
        assertEquals("jaro", score.getPlayer());
        assertEquals("1024", score.getGame());
        assertEquals(150, score.getPoints());
    }

    @Test
    void getTopScore() {
        scoreService.reset();
        scoreService.addScore(new Score("jaro", "1024", 150, new Date()), "");
        scoreService.addScore(new Score("fero", "1024", 250, new Date()), "");
        scoreService.addScore(new Score("mara", "1024", 200, new Date()), "");
        scoreService.addScore(new Score("jano", "1024", 190, new Date()), "");
        var scores = scoreService.getTopScore("1024", "");
        assertEquals(3, scores.size());
        var score = scores.get(0);
        assertEquals("fero", score.getPlayer());
        assertEquals("1024", score.getGame());
        assertEquals(250, score.getPoints());

        score = scores.get(1);
        assertEquals("mara", score.getPlayer());
        assertEquals("1024", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(2);
        assertEquals("jano", score.getPlayer());
        assertEquals("1024", score.getGame());
        assertEquals(190, score.getPoints());
    }

}
