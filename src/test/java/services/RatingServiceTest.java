package services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import entity.Rating;
import services.rating.RatingService;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class RatingServiceTest {
    @Autowired
    RatingService ratingService;

    @Test
    void reset() {
        ratingService.reset();
        assertEquals(0, ratingService.getRating("1024", "AlleksRoy"));
    }

    @Test
    void setRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("AlleksRoy", "1024", 3, new Date()));
        assertEquals(3, ratingService.getRating("1024", "AlleksRoy"));

        ratingService.setRating(new Rating("AlleksRoy", "1024", 5, new Date()));
        assertEquals(5, ratingService.getRating("1024", "AlleksRoy"));
    }

    @Test
    void getRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("AlleksRoy", "1024", 5, new Date()));
        assertEquals(5, ratingService.getRating("1024", "AlleksRoy"));
    }

    @Test
    void getAverageRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("AlleksRoy", "1024", 5, new Date()));
        ratingService.setRating(new Rating("Yaroslav", "1024", 4, new Date()));
        ratingService.setRating(new Rating("Valentyn", "1024", 5, new Date()));
        assertEquals(4.666, ratingService.getAverageRating("1024"), 0.001);
    }
}
