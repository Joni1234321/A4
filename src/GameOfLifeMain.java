import java.util.Scanner;

public class GameOfLifeMain {
    static GameOfLife game;

    public static void main(String[] args) {
        game = new GameOfLife(300);
        automaticGame();
    }

    static void automaticGame () {
        game.play(10);
    }

    static void manualGame () {
        boolean running = true;
        Scanner console = new Scanner(System.in);
        while (running) {
            System.out.print("Press enter to continue: ");
            console.nextLine();
            game.nextState(1);
        }
    }
}
