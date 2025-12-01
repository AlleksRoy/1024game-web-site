package controller;

import UI.ConsoleUI;
import core.GameLogic;

import java.util.Scanner;

public class Controller {
    private boolean canUseUndo;
    private Scanner scanner;

    public Controller() {
        scanner = new Scanner(System.in);
        canUseUndo = false;
    }

    public void processInput(GameLogic gameLogic, ConsoleUI consoleUI) {
        System.out.println("Type 'H' for command list");
        System.out.print("Enter move or command: ");
        char move = scanner.next().toUpperCase().charAt(0);

        switch (move) {
            case 'W':
                savePrevState(gameLogic);
                gameLogic.moveUp();
                break;
            case 'A':
                savePrevState(gameLogic);
                gameLogic.moveLeft();
                break;
            case 'S':
                savePrevState(gameLogic);
                gameLogic.moveDown();
                break;
            case 'D':
                savePrevState(gameLogic);
                gameLogic.moveRight();
                break;
            case 'H':
                consoleUI.printHelpMessage();
                break;
            case 'U':
                undo(gameLogic);
                break;
            case 'R':
                System.out.println();
                gameLogic.resetGame();
                consoleUI.play();
                break;
            case 'Q':
                System.exit(0);
                break;
            default:
                consoleUI.printInvalidMoveMessage();
                processInput(gameLogic, consoleUI);
        }
        System.out.println();
    }

    private void savePrevState(GameLogic gameLogic) {
        canUseUndo = true;
        gameLogic.getField().setPrevBoard(copyBoard(gameLogic.getField().getBoard()));
        gameLogic.setPrevScore(gameLogic.getScore());
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    private void undo(GameLogic gameLogic) {
        if (gameLogic.getField().isEmptyPrevBoard() || !canUseUndo) {
            System.out.println("You can't use Undo");
            return;
        }
        if (gameLogic.getUndoUses() > 0) {
            gameLogic.getField().setBoard(gameLogic.getField().getPrevBoard());
            gameLogic.setScore(gameLogic.getPrevScore());
            gameLogic.setUndoUses(gameLogic.getUndoUses() - 1);
            if (gameLogic.getUndoUses() < 0) {
                gameLogic.setUndoUses(0);
            }
            canUseUndo = false;
        } else {
            System.out.println("No more undo uses");
        }
    }

    public void playAgain(GameLogic gameLogic, ConsoleUI consoleUI) {
        System.out.print("Do you want to play again? (y/n): ");
        char playAgain = scanner.next().toUpperCase().charAt(0);
        if (playAgain == 'Y') {
            gameLogic.resetGame();
            consoleUI.play();
        } else if (playAgain != 'N') {
            consoleUI.printInvalidMoveMessage();
            playAgain(gameLogic, consoleUI);
        } else {
            System.out.println("Invalid input");
        }
    }

    public String requestComment() {
        scanner.nextLine();
        String comment;
        do {
            System.out.print("Leave your comment (max 100 symbols): ");
            comment = scanner.nextLine();
            if (comment.length() > 100) {
                System.out.println("Your comment is too long");
            }
        } while (comment.length() > 100);
        return comment;
    }

    public int requestRating() {
        int rating;
        do {
            System.out.print("Rate this game (from 1 to 5): ");
            while (!scanner.hasNextInt()) {
                scanner.next();
                System.out.print("Rate this game (from 1 to 5): ");
            }
            rating = scanner.nextInt();
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1 and 5");
            }
        } while (rating < 1 || rating > 5);
        scanner.nextLine();
        return rating;
    }
}
