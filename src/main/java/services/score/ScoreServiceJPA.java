package services.score;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import entity.Score;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score, String mode) {
        int incoming = score.getPoints();

        List<Score> existing = entityManager
                .createNamedQuery("Score.checkIfScoreExist", Score.class)
                .setParameter("game", score.getGame())
                .setParameter("player", score.getPlayer())
                .getResultList();

        if (!existing.isEmpty()) {
            Score s = existing.get(0);
            boolean updated = false;
            switch (mode) {
                case "easy":
                    if (incoming > s.getEasyPoints()) {
                        s.setEasyPoints(incoming);
                        updated = true;
                    }
                    break;
                case "hard":
                    if (incoming > s.getHardPoints()) {
                        s.setHardPoints(incoming);
                        updated = true;
                    }
                    break;
                default:
                    if (incoming > s.getPoints()) {
                        s.setPoints(incoming);
                        updated = true;
                    }
                    break;
            }
            if (updated) {
                s.setPlayed_at(score.getPlayed_at());
                entityManager.merge(s);
            }
        } else {
            Score s = new Score();
            s.setPlayer(score.getPlayer());
            s.setGame(score.getGame());
            s.setPlayed_at(score.getPlayed_at());
            s.setPoints(0);
            s.setEasyPoints(0);
            s.setHardPoints(0);
            switch (mode) {
                case "easy":
                    s.setEasyPoints(incoming);
                    break;
                case "hard":
                    s.setHardPoints(incoming);
                    break;
                default:
                    s.setPoints(incoming);
                    break;
            }
            entityManager.persist(s);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {
        entityManager.createNamedQuery("Score.updatePlayer")
                .setParameter("newName", newPlayer)
                .setParameter("oldName", oldPlayer)
                .executeUpdate();
    }

    @Override
    public List<Score> getTop3Scores(String game, String mode) {
        List<Score> list = entityManager.createNamedQuery("Score.findAllByGame", Score.class)
                .setParameter("game", game)
                .getResultList();
        return list.stream()
                .sorted((s1, s2) -> {
                    int p1 = switch (mode) {
                        case "easy" -> s1.getEasyPoints();
                        case "hard" -> s1.getHardPoints();
                        default -> s1.getPoints();
                    };
                    int p2 = switch (mode) {
                        case "easy" -> s2.getEasyPoints();
                        case "hard" -> s2.getHardPoints();
                        default -> s2.getPoints();
                    };
                    return Integer.compare(p2, p1);
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<Score> getTopScore(String game, String mode) {
        List<Score> list = entityManager.createNamedQuery("Score.findAllByGame", Score.class)
                .setParameter("game", game)
                .getResultList();
        return list.stream()
                .sorted((s1, s2) -> {
                    int p1 = switch (mode) {
                        case "easy" -> s1.getEasyPoints();
                        case "hard" -> s1.getHardPoints();
                        default -> s1.getPoints();
                    };
                    int p2 = switch (mode) {
                        case "easy" -> s2.getEasyPoints();
                        case "hard" -> s2.getHardPoints();
                        default -> s2.getPoints();
                    };
                    return Integer.compare(p2, p1);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getScoreByPlayer(String game, String player, String mode) {
        Score s = entityManager.createNamedQuery("Score.findByPlayer", Score.class)
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultStream().findFirst().orElse(null);
        if (s == null) return 0;
        return switch (mode) {
            case "easy" -> s.getEasyPoints();
            case "hard" -> s.getHardPoints();
            default -> s.getPoints();
        };
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Score.reset").executeUpdate();
    }
}
