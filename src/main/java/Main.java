import UI.ConsoleUI;
import controller.Controller;
import core.Field;
import core.GameLogic;

public class Main {
    public static void main(String[] args) {
        var gameField = new Field(4);
        var gameLogic = new GameLogic(gameField);
        var controller = new Controller();
        var consoleUI = new ConsoleUI(gameLogic, controller);
        consoleUI.play();
    }
}
