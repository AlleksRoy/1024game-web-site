package services.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import entity.Score;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score, String mode) {
        restTemplate.postForEntity(url + "/score", score, Score.class);
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {

    }

    @Override
    public List<Score> getTop3Scores(String game, String mode) {
        return List.of();
    }

    @Override
    public List<Score> getTopScore(String game, String mode) {
        return Arrays.asList(restTemplate.getForEntity(url + "/score/" + game, Score[].class).getBody());
    }

    @Override
    public Integer getScoreByPlayer(String game, String player, String mode) {
        return 0;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
