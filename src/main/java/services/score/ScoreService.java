package services.score;

import entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score, String mode);

    void updatePlayerName(String oldPlayer, String newPlayer);

    List<Score> getTop3Scores(String game, String mode);

    List<Score> getTopScore(String game, String mode);

    Integer getScoreByPlayer(String game, String player, String mode);

    void reset();
}

