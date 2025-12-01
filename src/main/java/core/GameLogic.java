package core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import entity.Comment;
import entity.Rating;
import entity.Score;
import services.comment.CommentService;
import services.rating.RatingService;
import services.score.ScoreService;

import java.util.*;

@Getter
@Setter
public class GameLogic {
    private static final int WINNING_TILE = 1024;
    private int score;
    private int prevScore;
    private int prevUndo;
    private int prevSwap;
    private int prevDelete;
    private int prevShuffle;

    private int undoUses;
    private int swapUses;
    private int deleteUses;
    private int shuffleUses;

    private GameState gameState;
    private Field field;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;


    public GameLogic(Field field) {
        this.field = field;
        this.gameState = GameState.RUNNING;
        this.score = 0;
        this.prevScore = 0;

        this.undoUses = 2;
        this.swapUses = 1;
        this.deleteUses = 0;
        this.shuffleUses = 0;
    }

    public void moveLeft() {
        boolean needAddTile = false;
        int[][] board = field.getBoard();

        for (var i = 0; i < board.length; i++) {
            int[] oldRow = board[i];
            int[] newRow = processRow(oldRow);
            if (!areEqual(oldRow, newRow)) {
                needAddTile = true;
            }
            board[i] = newRow;
        }
        if (needAddTile) {
            field.addRandomTile();
        }
        if (!canMove()) {
            gameState = GameState.LOST;
        }
    }

    private int[] processRow(int[] row) {
        int n = row.length;
        int[] newRow = new int[n];
        int barrier = -1;
        for (int i = 0; i < n; i++) {
            if (row[i] == -1) {
                barrier = i;
                newRow[i] = -1;
                break;
            }
        }
        if (barrier == -1) {
            return mergeSegment(row);
        }
        int[] leftRaw = Arrays.copyOfRange(row, 0, barrier);
        int[] leftMerged = mergeSegment(leftRaw);
        System.arraycopy(leftMerged, 0, newRow, 0, leftMerged.length);
        int[] rightRaw = Arrays.copyOfRange(row, barrier + 1, n);
        int[] rightMerged = mergeSegment(rightRaw);
        System.arraycopy(rightMerged, 0, newRow, barrier + 1, rightMerged.length);

        return newRow;
    }

    private int[] mergeSegment(int[] segment) {
        int len = segment.length;
        int[] temp = new int[len];
        int pos = 0;
        int lastMerged = -1;

        for (int i = 0; i < len; i++) {
            int v = segment[i];
            if (v == 0) continue;
            if (pos > 0
                    && temp[pos - 1] == v
                    && lastMerged != pos - 1) {
                temp[pos - 1] *= 2;
                checkUses(temp, pos);
                score += temp[pos - 1];
                lastMerged = pos - 1;
                if (temp[pos - 1] == WINNING_TILE) {
                    gameState = GameState.WON;
                }
            } else {
                temp[pos++] = v;
            }
        }
        return temp;
    }

    private boolean areEqual(int[] row1, int[] row2) {
        if (row1.length != row2.length) {
            return false;
        }
        for (var i = 0; i < row1.length; i++) {
            if (row1[i] != row2[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean canMove() {
        int[][] board = field.getBoard();
        int size = board.length;

        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                int current = board[i][j];
                if (current == 0) {
                    return true;
                }
                if ((i > 0 && current == board[i - 1][j]) ||
                        (i < size - 1 && current == board[i + 1][j]) ||
                        (j > 0 && current == board[i][j - 1]) ||
                        (j < size - 1 && current == board[i][j + 1])) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkUses(int[] temp, int pos) {
        if (temp[pos - 1] == 128 && undoUses < 2) {
            undoUses++;
        }
        if (temp[pos - 1] == 256 && swapUses < 2) {
            swapUses++;
        }
        if (temp[pos - 1] == 512 && deleteUses < 1) {
            deleteUses++;
        }
        if (temp[pos - 1] == 512 && shuffleUses < 1) {
            shuffleUses++;
        }
    }

    public void moveRight() {
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
        moveLeft();
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
    }

    public void moveUp() {
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
        moveLeft();
        field.rotateBoardClockwise();
    }

    public void moveDown() {
        field.rotateBoardClockwise();
        moveLeft();
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
        field.rotateBoardClockwise();
    }

    public void undo() {
        if (!field.isEmptyPrevBoard() && getUndoUses() > 0) {
            field.setBoard(field.getPrevBoard());
            setUndoUses(getUndoUses() - 1);
            setSwapUses(getPrevSwap());
            setDeleteUses(getPrevDelete());
            setShuffleUses(getPrevShuffle());
            setScore(getPrevScore());
            field.setPrevBoard(new int[field.getBoard().length][field.getBoard().length]);
        }
    }

    public void swapTiles(int r1, int c1, int r2, int c2) {
        savePrevState();
        if (field.getBoard()[r1][c1] == 0 && field.getBoard()[r2][c2] == 0) {
            return;
        }
        if (!field.isEmptyPrevBoard()) {
            int[][] board = field.getBoard();
            int tmp = board[r1][c1];
            board[r1][c1] = board[r2][c2];
            board[r2][c2] = tmp;
            swapUses--;
        }
    }

    public void deleteTileByValue(int value) {
        savePrevState();
        int[][] board = field.getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == value) {
                    board[row][col] = 0;
                }
            }
        }
        deleteUses--;
    }

    public void shuffleTiles() {
        savePrevState();
        int size = field.getBoard().length;
        List<Integer> vals = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                vals.add(field.getBoard()[i][j]);
            }
        }
        Collections.shuffle(vals);
        int idx = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field.getBoard()[i][j] = vals.get(idx++);
            }
        }
        shuffleUses--;
    }

    public void savePrevState() {
        field.setPrevBoard(copyBoard(field.getBoard()));
        setPrevScore(getScore());
        setPrevUndo(getUndoUses());
        setPrevSwap(getSwapUses());
        setPrevDelete(getDeleteUses());
        setPrevShuffle(getShuffleUses());
    }

    public void saveScore() {
        scoreService.addScore(new Score(System.getProperty("user.name"), "1024", getScore(), new Date()), "");
    }

    public void saveComment(String comment) {
        commentService.addComment(new Comment(System.getProperty("user.name"), "1024", comment, new Date()));
    }

    public void saveRating(int rating) {
        ratingService.setRating(new Rating(System.getProperty("user.name"), "1024", rating, new Date()));
    }

    public void resetGame() {
        this.field = new Field(4);
        this.score = 0;
        this.prevScore = 0;
        this.undoUses = 2;
        this.gameState = GameState.RUNNING;
    }

    public int[][] copyBoard(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }
}
