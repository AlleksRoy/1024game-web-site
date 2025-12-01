package services.rating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import entity.Rating;

import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        List<Rating> existingRatings = entityManager.createNamedQuery("Rating.checkRating", Rating.class)
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultList();
        if (!existingRatings.isEmpty()) {
            Rating existing = existingRatings.get(0);
            existing.setRating(rating.getRating());
            entityManager.merge(existing);
        } else {
            entityManager.persist(rating);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {
        entityManager.createNamedQuery("Rating.updatePlayer")
                .setParameter("newName", newPlayer)
                .setParameter("oldName", oldPlayer)
                .executeUpdate();
    }

    @Override
    public double getAverageRating(String game) {
        List<Double> result = entityManager.createNamedQuery("Rating.getAverageRating", Double.class)
                .setParameter("game", game)
                .getResultList();
        if (result.isEmpty() || result.get(0) == null) {
            return 0.0;
        }
        return result.get(0);
    }

    @Override
    public int getRating(String game, String player) {
        List<Integer> results = entityManager.createNamedQuery("Rating.getRating", Integer.class)
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultList();
        if (results.isEmpty()) {
            return 0;
        }
        return results.get(0);
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Rating.reset").executeUpdate();
    }
}
