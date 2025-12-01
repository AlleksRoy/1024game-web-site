package controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import UI.ConsoleUI;
import core.Field;
import core.GameLogic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class ControllerTest {
    private final int SIZE = 4;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testProcessInputHelp() {
        String input = "H\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Field field = new Field(SIZE);
        GameLogic gameLogic = new GameLogic(field);
        Controller controller = new Controller();
        ConsoleUI consoleUI = new ConsoleUI(gameLogic, controller);

        controller.processInput(gameLogic, consoleUI);
        String output = outContent.toString();
        assertTrue(output.contains("Movement buttons"));
    }

    @Test
    public void testPlayAgainInvalidThenYes() {
        String input = "X\nY\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Field field = new Field(SIZE);
        GameLogic gameLogic = new GameLogic(field);
        Controller controller = new Controller();
        ConsoleUI consoleUI = new ConsoleUI(gameLogic, controller) {
            @Override
            public void play() {
                System.out.println("Game restarted");
            }
        };
        controller.playAgain(gameLogic, consoleUI);
        String output = outContent.toString();
        assertTrue(output.contains("Invalid input"));
        assertTrue(output.contains("Game restarted"));
    }
}
