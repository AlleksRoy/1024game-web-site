import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import UI.ConsoleUI;
import controller.Controller;
import core.Field;
import core.GameLogic;
import services.comment.CommentService;
import services.comment.CommentServiceRestClient;
import services.rating.RatingService;
import services.rating.RatingServiceRestClient;
import services.score.ScoreService;
import services.score.ScoreServiceRestClient;
import services.user.UserService;
import services.user.UserServiceRestClient;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
        //SpringApplication.run(SpringClient.class);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI consoleUI) {
        return s -> consoleUI.play();
    }

    @Bean
    public ConsoleUI consoleUI(GameLogic gameLogic, Controller controller) {
        return new ConsoleUI(gameLogic, controller);
    }

    @Bean
    public Controller controller() {
        return new Controller();
    }

    @Bean
    public GameLogic gameLogic(Field field) {
        return new GameLogic(field);
    }

    @Bean
    public Field field() {
        return new Field(4);
    }

    @Bean
    public ScoreService scoreService() {
        //return new ScoreServiceJDBC();
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        //return new CommentServiceJDBC();
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        //return new RatingServiceJDBC();
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }

    @Bean
    public UserService userService() {
        return new UserServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
