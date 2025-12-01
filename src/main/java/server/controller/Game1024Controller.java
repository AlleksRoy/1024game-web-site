package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import core.Field;
import core.GameLogic;
import core.GameState;
import core.HardField;
import entity.Comment;
import entity.Rating;
import entity.Score;
import services.comment.CommentService;
import services.rating.RatingService;
import services.score.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/1024")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class Game1024Controller {
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    CommentService commentService;

    private Field normalLvlField = new Field(4);
    private GameLogic normalLvlGameLogic = new GameLogic(normalLvlField);
    private boolean isNormalLvl = true;

    private Field easyLvlField = new Field(4);
    private GameLogic easyLvlGameLogic = new GameLogic(easyLvlField);
    private boolean isEasyLvl = false;

    private Field hardLvlField = new HardField(4);
    private GameLogic hardLvlGameLogic = new GameLogic(hardLvlField);
    private boolean isHardLvl = false;

    @GetMapping
    public String game1024(Model model) {
        if (isGameWon() && authenticationController.isUserLogged()) {
            saveScoreByMode();
        }

        Field field = currentField();
        GameLogic logic = currentLogic();
        String mode = currentMode();

        model.addAttribute("htmlField", renderGrid(field, mode));
        model.addAttribute("gridScore", logic.getScore());
        model.addAttribute("bestScore", scoreService.getScoreByPlayer("1024", authenticationController.getUserName(), mode));
        model.addAttribute("best3Score", scoreService.getTop3Scores("1024", mode));
        model.addAttribute("HOFScore", scoreService.getTopScore("1024", mode));
        model.addAttribute("undoUsesLeft", logic.getUndoUses());
        model.addAttribute("swapUsesLeft", logic.getSwapUses());
        model.addAttribute("deleteUsesLeft", logic.getDeleteUses());
        model.addAttribute("shuffleUsesLeft", logic.getShuffleUses());

        model.addAttribute("currentMode", mode);
        String newSuffix = mode.isEmpty() ? "Normal" : capitalize(mode);
        model.addAttribute("newGamePath", "/1024/new" + newSuffix);

        return "1024";
    }

    @GetMapping("/{command}")
    public String move(@PathVariable String command) {
        GameLogic logic = currentLogic();
        if (logic.getGameState() == GameState.RUNNING) {
            easyLvlGameLogic.savePrevState();
            switch (command) {
                case "up" -> logic.moveUp();
                case "down" -> logic.moveDown();
                case "left" -> logic.moveLeft();
                case "right" -> logic.moveRight();
            }
        }
        if (authenticationController.isUserLogged()) {
            saveScoreByMode();
        }
        return "redirect:/1024";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        model.addAttribute("avgRating", ratingService.getAverageRating("1024"));
        model.addAttribute("comment", commentService.getLastComments("1024"));
        return "ReviewsPage";
    }

    @PostMapping("/reviews")
    public String addReviews(@RequestParam("rating") Integer rating, @RequestParam("comment") String comment) {
        if (authenticationController.isUserLogged()) {
            String user = authenticationController.getUserName();
            ratingService.setRating(new Rating(user, "1024", rating, new Date()));
            commentService.addComment(new Comment(user, "1024", comment, new Date()));
        }
        return "redirect:/1024/reviews";
    }

    @GetMapping("/undo")
    public String undo() {
        easyLvlGameLogic.undo();
        return "redirect:/1024";
    }

    @GetMapping("/swap")
    public String swap(@RequestParam int r1, @RequestParam int c1, @RequestParam int r2, @RequestParam int c2) {
        easyLvlGameLogic.swapTiles(r1, c1, r2, c2);
        return "redirect:/1024";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int value) {
        easyLvlGameLogic.deleteTileByValue(value);
        return "redirect:/1024";
    }

    @GetMapping("/shuffle")
    public String shuffle() {
        easyLvlGameLogic.shuffleTiles();
        return "redirect:/1024";
    }

    @GetMapping("/easy")
    public String easyLvl() {
        isEasyLvl = true;
        isNormalLvl = isHardLvl = false;
        return "redirect:/1024";
    }

    @GetMapping("/newEasy")
    public String newEasy() {
        reset(easyLvlField);
        return "redirect:/1024";
    }

    @GetMapping("/normal")
    public String normalLvl() {
        isNormalLvl = true;
        isEasyLvl = isHardLvl = false;
        return "redirect:/1024";
    }

    @GetMapping("/newNormal")
    public String newNormal() {
        reset(normalLvlField);
        return "redirect:/1024";
    }

    @GetMapping("/hard")
    public String hardLvl() {
        isHardLvl = true;
        isNormalLvl = isEasyLvl = false;
        return "redirect:/1024";
    }

    @GetMapping("/newHard")
    public String newHard() {
        reset(hardLvlField);
        return "redirect:/1024";
    }

    private Field currentField() {
        if (isEasyLvl) return easyLvlField;
        if (isHardLvl) return hardLvlField;
        return normalLvlField;
    }

    private GameLogic currentLogic() {
        if (isEasyLvl) return easyLvlGameLogic;
        if (isHardLvl) return hardLvlGameLogic;
        return normalLvlGameLogic;
    }

    private String currentMode() {
        if (isEasyLvl) return "easy";
        if (isHardLvl) return "hard";
        return "";
    }

    private void saveScoreByMode() {
        String user = authenticationController.getUserName();
        GameLogic logic = currentLogic();
        scoreService.addScore(new Score(user, "1024", logic.getScore(), new Date()), currentMode());
    }

    private void reset(Field field) {
        if (field.equals(easyLvlField)) {
            easyLvlField = new Field(4);
            easyLvlGameLogic = new GameLogic(easyLvlField);
            easyLvlGameLogic.setScore(0);
            easyLvlGameLogic.setPrevScore(0);
            easyLvlGameLogic.setUndoUses(2);
            easyLvlGameLogic.setSwapUses(1);
            easyLvlGameLogic.setDeleteUses(0);
        } else if (field.equals(normalLvlField)) {
            normalLvlField = new Field(4);
            normalLvlGameLogic = new GameLogic(normalLvlField);
            normalLvlGameLogic.setScore(0);
        } else if (field.equals(hardLvlField)) {
            hardLvlField = new HardField(4);
            hardLvlGameLogic = new GameLogic(hardLvlField);
            hardLvlGameLogic.setScore(0);
        }
    }

    private String renderGrid(Field field, String mode) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class='grid'>\n");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int value = field.getBoard()[row][col];
                if ("hard".equals(mode) && value == -1) {
                    sb.append("<div class='tile tile-block' ")
                            .append("data-row='").append(row).append("' ")
                            .append("data-col='").append(col).append("'></div>\n");
                } else {
                    sb.append("<div class='tile tile-").append(value).append("' ")
                            .append("data-row='").append(row).append("' ")
                            .append("data-col='").append(col).append("'>")
                            .append(value == 0 ? "" : value)
                            .append("</div>\n");
                }
            }
        }
        sb.append("</div>\n");
        return sb.toString();
    }

    private String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public boolean isGameWon() {
        return currentLogic().getGameState() == GameState.WON;
    }

    public boolean isGameLost() {
        return currentLogic().getGameState() == GameState.LOST;
    }

    public boolean isGameRunning() {
        return currentLogic().getGameState() == GameState.RUNNING;
    }
}