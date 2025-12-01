package core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private final int SIZE = 4;

    @Test
    void testConstructorCreatesTwoRandomTiles() {
        var field = new Field(SIZE);
        int[][] board = field.getBoard();
        int nonZeroCount = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    nonZeroCount++;
                }
            }
        }
        assertEquals(2, nonZeroCount);
    }

    @Test
    public void testAddRandomTile() {
        Field field = new Field(4);
        int[][] board = field.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }
        field.addRandomTile();
        int nonZeroCount = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    nonZeroCount++;
                }
            }
        }
        assertEquals(1, nonZeroCount);
    }

    @Test
    void testRotateBoardClockwise() {
        Field field = new Field(SIZE);
        int[][] board = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        field.setBoard(board);
        field.rotateBoardClockwise();
        int[][] rotated = field.getBoard();
        int[][] expected = new int[][]{
                {13, 9, 5, 1},
                {14, 10, 6, 2},
                {15, 11, 7, 3},
                {16, 12, 8, 4}
        };
        assertArrayEquals(expected, rotated);
    }

    @Test
    void testIsEmptyPrevBoard() {
        var field = new Field(SIZE);
        assertTrue(field.isEmptyPrevBoard());
    }
}
