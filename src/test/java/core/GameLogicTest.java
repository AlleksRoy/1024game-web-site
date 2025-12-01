package core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    private final int SIZE = 4;

    @Test
    public void testMoveLeftMerge() {
        var field = new Field(4);
        int[][] board = new int[4][4];
        board[0] = new int[]{2, 2, 0, 0};
        field.setBoard(board);

        GameLogic gameLogic = new GameLogic(field);
        gameLogic.moveLeft();

        int[][] result = field.getBoard();
        assertArrayEquals(new int[]{4, 0, 0, 0}, result[0]);
    }

    @Test
    void testCanMoveReturnsTrue() {
        var field = new Field(4);
        var gameLogic = new GameLogic(field);
        assertTrue(gameLogic.canMove());
    }

    @Test
    public void testNoMergeNoMove() {
        Field field = new Field(4);
        int[][] board = new int[][]{
                {2, 4, 8, 16},
                {32, 64, 128, 256},
                {512, 2, 4, 8},
                {16, 32, 64, 128}
        };
        field.setBoard(board);
        GameLogic gameLogic = new GameLogic(field);
        gameLogic.moveLeft();
        int[][] result = field.getBoard();
        for (int i = 0; i < 4; i++) {
            assertArrayEquals(board[i], result[i]);
        }
    }

    @Test
    void testCanMoveReturnsFalse() {
        var field = new Field(4);
        var gameLogic = new GameLogic(field);
        int[][] board = field.getBoard();
        int value = 2;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = value;
                value += 2;
            }
        }
        assertFalse(gameLogic.canMove());
    }

    @Test
    void testMoveRight() {
        var field = new Field(4);
        var gameLogic = new GameLogic(field);
        int[][] board = {
                {0, 0, 2, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        field.setBoard(board);
        gameLogic.moveRight();
        assertEquals(4, field.getBoard()[0][3]);
    }

    @Test
    void testMoveUpAndMoveDown() {
        var field = new Field(4);
        var gameLogic = new GameLogic(field);
        int[][] board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            board[i][0] = 2;
        }
        field.setBoard(board);
        gameLogic.moveUp();
        assertEquals(4, field.getBoard()[0][0]);
    }

    @Test
    void testResetGame() {
        var field = new Field(SIZE);
        var gameLogic = new GameLogic(field);
        gameLogic.setScore(100);
        field.getBoard()[0][0] = 2;
        gameLogic.resetGame();
        assertEquals(0, gameLogic.getScore());
        int nonZeroCount = 0;
        int[][] board = gameLogic.getField().getBoard();
        for (int[] row : board) {
            for (int val : row) {
                if (val != 0) {
                    nonZeroCount++;
                }
            }
        }
        assertEquals(2, nonZeroCount);
        assertEquals(GameState.RUNNING, gameLogic.getGameState());
        assertEquals(2, gameLogic.getUndoUses());
    }
}
