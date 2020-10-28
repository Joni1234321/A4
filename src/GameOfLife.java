import java.util.Arrays;
import java.util.Random;

public class GameOfLife {

    int step = 0;

    int size;                   // Size of grid
    int[][] grid;               // Data of grid
    int[][] gridCounter;        // X amount of times cell has been alive

    final int SNAPSHOTS = 4;
    int[][][] snapshots;
    int lowestRepeat = Integer.MAX_VALUE;

    public GameOfLife (int size) {
        this.size   = size;
        gridCounter = new int[size][size];
        snapshots   = new int[SNAPSHOTS][size][size];

        // Generate grid
        grid = new int[size][size];
        Random r = new Random();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                grid[x][y] = r.nextBoolean() ? 1 : 0;
    }
    public GameOfLife (int initialState[][]) {
        this.size   = initialState.length;
        gridCounter = new int[size][size];
        snapshots   = new int[SNAPSHOTS][size][size];

        grid        = initialState;
    }

    public void nextState () {
        int[][] nextGrid = new int[size][size];

        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                int alive = grid[x][y];
                int neighbours = liveNeighbours(x, y);

                int next = 0;
                if (neighbours == 3) next = 1;
                else if (neighbours == 2 && alive == 1) next = 1;

                nextGrid[x][y] = next;
                gridCounter[x][y] += next;
            }
        }
        grid = nextGrid;
        step++;
        checkPeriodic();
    }

    int liveNeighbours (int x, int y){
        int n = 0;
        for (int dy = -1; dy <= 1; dy++){
            for (int dx = -1; dx <= 1; dx++){
                if (dx == 0 && dy == 0) continue; // Dont count center
                n += grid[(x + dx + size) % size][(y + dy + size) % size];
            }
        }
        return n;
    }

    // Stores a snapshot every 10, 100, 1,000 and 10,000 steps
    void checkPeriodic () {
        for (int i = SNAPSHOTS; i > 0; i--) {
            int deltaStep = (int)Math.pow(10, i);
            // Check
            if (Arrays.deepEquals(grid, snapshots[i-1])) {
                int steps = step % deltaStep;
                if (steps == 0) steps += deltaStep;
                
                if (lowestRepeat > steps){
                    lowestRepeat = steps;
                    System.out.println("Repeats itself every " + (lowestRepeat)+ " steps");
                }
            }

            // Add after checking
            if (step % deltaStep == 0) {
                snapshots[i-1] = grid;
            }
        }
    }

    public int getSize (){
        return size;
    }
    public int[][] getGrid() {
        return grid;
    }
    public int[][] getGridCounter () {
        return gridCounter;
    }


}
