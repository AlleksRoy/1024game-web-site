package server.webservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import entity.Rating;
import services.rating.RatingService;

@RestController
@RequestMapping("/api/rating")
@AllArgsConstructor
public class RatingServiceREST {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) {
        return ratingService.getRating(game, player);
    }

    @GetMapping("/average/{game}")
    public double getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @PostMapping
    public void addRating(@RequestBody Rating rating) {
        ratingService.setRating(rating);
    }
}
