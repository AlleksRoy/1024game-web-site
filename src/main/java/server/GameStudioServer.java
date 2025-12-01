package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import services.comment.CommentService;
import services.comment.CommentServiceJPA;
import services.email.EmailSender;
import services.rating.RatingService;
import services.rating.RatingServiceJPA;
import services.score.ScoreService;
import services.score.ScoreServiceJPA;
import services.user.UserService;
import services.user.UserServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan(basePackages = "entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public UserService userService() {
        return new UserServiceJPA();
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }
}
