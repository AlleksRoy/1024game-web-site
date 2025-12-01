package UI;

import controller.Controller;
import core.GameLogic;
import core.GameState;

public class ConsoleUI {
    private GameLogic gameLogic;
    private Controller controller;

    public ConsoleUI(GameLogic gameLogic, Controller controller) {
        this.gameLogic = gameLogic;
        this.controller = controller;
    }

    public void play() {
        printWelcomeMessage();
        printBestScores();
        printLastComments();

        while (gameLogic.getGameState() == GameState.RUNNING) {

            printBoard(gameLogic.getField().getBoard());

            controller.processInput(gameLogic, this);

            if (checkWin()) {
                controller.playAgain(gameLogic, this);
                break;
            }
            if (checkLose()) {
                controller.playAgain(gameLogic, this);
                break;
            }
        }
    }

    private boolean checkWin() {
        if (gameLogic.getGameState() == GameState.WON) {
            printBoard(gameLogic.getField().getBoard());
            printWinMessage();
            gameLogic.saveScore();
            String comment = controller.requestComment();
            gameLogic.saveComment(comment);
            int rating = controller.requestRating();
            gameLogic.saveRating(rating);
            return true;
        }
        return false;
    }

    private boolean checkLose() {
        if (!gameLogic.canMove()) {
            printBoard(gameLogic.getField().getBoard());
            gameLogic.setGameState(GameState.LOST);
            printGameOverMessage();
            gameLogic.saveScore();
            String comment = controller.requestComment();
            gameLogic.saveComment(comment);
            int rating = controller.requestRating();
            gameLogic.saveRating(rating);
            return true;
        }
        return false;
    }

    private void printBoard(int[][] board) {
        System.out.println("   Your Score: " + gameLogic.getScore() + "  Undo: " + gameLogic.getUndoUses() + " uses left      ");

        System.out.println("╔═════════╦═════════╦═════════╦═════════╗");
        for (int i = 0; i < 4; i++) {
            System.out.println("║         ║         ║         ║         ║");
            System.out.print("║");
            for (int j = 0; j < 4; j++) {
                System.out.printf("  %-4s   ║", board[i][j] == 0 ? "" : board[i][j]);
            }
            System.out.println();
            System.out.println("║         ║         ║         ║         ║");
            if (i < 3) {
                System.out.println("╠═════════╬═════════╬═════════╬═════════╣");
            } else {
                System.out.println("╚═════════╩═════════╩═════════╩═════════╝");
            }
        }
    }

    private void printBestScores() {
        var scores = gameLogic.getScoreService().getTopScore("1024", "");
        System.out.println("       Best Players:");
        for (int i = 0; i < scores.size(); i++) {
            var score = scores.get(i);
            System.out.printf("            |%d| %s %d ★\n", i + 1, score.getPlayer(), score.getPoints());
        }
        System.out.println("       └─────────────────────────┘");
    }

    private void printLastComments() {
        var comments = gameLogic.getCommentService().getLastComments("1024");
        System.out.println("       Last Comments:");
        for (int i = 0; i < comments.size(); i++) {
            var comment = comments.get(i);
            System.out.printf("          %s: %s\n", comment.getPlayer(), comment.getComment());
        }
        System.out.println("       └─────────────────────────┘");
        System.out.println();
    }

    private void printWelcomeMessage() {
        System.out.println("       ╔══════════════════════════╗");
        System.out.println("       ║ Welcome to the game 1024 ║");
        System.out.println("       ║ made by Arsenii Burlaka! ║");
        System.out.println("       ╚══════════════════════════╝");
    }

    private void printWinMessage() {
        System.out.println("       ╔═════════════════════════╗");
        System.out.println("       ║  !!!Congratulations!!!  ║");
        System.out.println("       ╠═════════════════════════╣");
        System.out.println("       ║   You've reached 1024   ║");
        System.out.println("       ╚═════════════════════════╝");
        System.out.println();
    }

    private void printGameOverMessage() {
        System.out.println("       ╔═════════════════════════╗");
        System.out.println("       ║     !!!Game Over!!!     ║");
        System.out.println("       ╠═════════════════════════╣");
        System.out.println("       ║      No more moves      ║");
        System.out.println("       ╚═════════════════════════╝");
        System.out.println();
    }

    public void printHelpMessage() {
        System.out.println();
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║         Movement buttons         ║");
        System.out.println("║ W/w - move up                    ║");
        System.out.println("║ A/a - move left                  ║");
        System.out.println("║ S/s - move down                  ║");
        System.out.println("║ D/d - move right                 ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║           Command keys           ║");
        System.out.println("║ U/u - undo (go back one step)    ║");
        System.out.println("║ make a 128 tile to get more uses ║");
        System.out.println("║                                  ║");
        System.out.println("║ R/r - restart game               ║");
        System.out.println("║ Q/q - quit                       ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    public void printInvalidMoveMessage() {
        System.out.println("Invalid input!!!");
        System.out.println();
    }
}
