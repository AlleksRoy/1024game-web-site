package UI;

import org.junit.Before;
import org.junit.Test;
import controller.Controller;
import core.Field;
import core.GameLogic;
import core.GameState;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class ConsoleUITest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testPrintWelcomeMessage() {
        Field field = new Field(4);
        GameLogic gameLogic = new GameLogic(field);
        Controller controller = new Controller();
        ConsoleUI consoleUI = new ConsoleUI(gameLogic, controller);
        String input = "Q\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        gameLogic.setGameState(GameState.LOST);
        consoleUI.play();
        String output = outContent.toString();
        assertTrue(output.contains("Welcome to the game 1024"));
    }

    @Test
    public void testPrintHelpMessage() {
        Field field = new Field(4);
        GameLogic gameLogic = new GameLogic(field);
        Controller controller = new Controller();
        ConsoleUI consoleUI = new ConsoleUI(gameLogic, controller);

        consoleUI.printHelpMessage();
        String output = outContent.toString();
        assertTrue(output.contains("W/w - move up"));
        assertTrue(output.contains("U/u - undo"));
    }
}
