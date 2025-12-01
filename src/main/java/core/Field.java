package core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private static int SIZE;
    private int[][] board;
    private int[][] prevBoard;

    public Field(int size) {
        this.SIZE = size;
        board = new int[SIZE][SIZE];
        prevBoard = new int[SIZE][SIZE];
        addRandomTile();
        addRandomTile();
    }

    public void addBlockedTile() {
        int emptyCount = countEmptyTiles();
        if (emptyCount == 0) {
            return;
        }
        int target = (int) (Math.random() * emptyCount);

        emptyCount = 0;
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    if (emptyCount == target) {
                        board[row][col] = -1;
                        return;
                    }
                    emptyCount++;
                }
            }
        }
    }

    public void addRandomTile() {
        int emptyCount = countEmptyTiles();
        if (emptyCount == 0) {
            return;
        }
        int target = (int) (Math.random() * emptyCount);

        emptyCount = 0;
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    if (emptyCount == target) {
                        board[row][col] = Math.random() < 0.9 ? 2 : 4;
                        return;
                    }
                    emptyCount++;
                }
            }
        }
    }

    public int countEmptyTiles() {
        int emptyCount = 0;
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    emptyCount++;
                }
            }
        }
        return emptyCount;
    }

    public void rotateBoardClockwise() {
        int[][] newBoard = new int[SIZE][SIZE];
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                newBoard[col][SIZE - 1 - row] = board[row][col];
            }
        }
        board = newBoard;
    }

    public boolean isEmptyPrevBoard() {
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                if (prevBoard[row][col] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}