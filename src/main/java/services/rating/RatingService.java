package services.rating;

import entity.Rating;

public interface RatingService {
    void setRating(Rating rating);

    void updatePlayerName(String oldPlayer, String newPlayer);

    double getAverageRating(String game);

    int getRating(String game, String player);

    void reset();
}
