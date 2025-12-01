package services.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import entity.Rating;


public class RatingServiceRestClient implements RatingService {
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url + "/rating", rating, Rating.class);
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {

    }

    @Override
    public double getAverageRating(String game) {
        Double average = restTemplate.getForObject(url + "/rating/average/" + game, Double.class);
        return average != null ? average.doubleValue() : 0.0;
    }

    @Override
    public int getRating(String game, String player) {
        Integer rating = restTemplate.getForObject(url + "/rating/" + game + "/" + player, Integer.class);
        return rating != null ? rating.intValue() : 0;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
