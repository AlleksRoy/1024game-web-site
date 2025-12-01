package server.webservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import entity.Score;
import services.score.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
@AllArgsConstructor
public class ScoreServiceREST {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScore(game, "");
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score, "");
    }

}
