import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameOfLifeMain {
    static GameOfLife game;
    static final boolean MANUAL = false;
    public static void main(String[] args) throws FileNotFoundException {
        int grid [][] = loadMap("src/map/acorn.gol");
        if (grid == null) return;

        game = new GameOfLife(100);

        if (MANUAL)
            manualGame();
        else
            automaticGame(10);

    }

    static void automaticGame (int updatesPerSecond) {
        game.play(1000 / updatesPerSecond);
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


    static int[][] loadMap (String path) throws FileNotFoundException{
        Scanner reader = new Scanner(new File(path));
        if (!reader.hasNextLine()) return null;

        int size = 0;
        List<String> lines = new ArrayList<String>();

        // Add all lines to list
        while (reader.hasNextLine()){
            lines.add(reader.nextLine());
            size++;
        }
        reader.close();

        int[][] grid = new int[size][size];

        // Read individual lines
        for (int y = 0; y < lines.size(); y++) {
            reader = new Scanner(lines.get(y));

            int x = 0;
            while(reader.hasNextInt() && x < size) {   // Read every line
                grid[x][y] = reader.nextInt() == 1 ? 1 : 0;
                x++;
            }
            while (x < size){           // Fill the rest with 0
                grid[x][y] = 0;
                x++;
            }
        }
        return grid;
    }

    static void printGrid (int grid[][]){
        for (int y = 0; y < grid.length; y++){
            System.out.println(Arrays.toString(grid[y]));
        }
    }
}
