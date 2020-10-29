import java.util.Arrays;
import java.util.Random;

public class GameOfLife {
    static final int MEANING = 42;

    int step = 0;

    int size;                   // Size of grid
    int[][] grid;               // Data of grid

    final int SNAPSHOTS = 4;                    // Those are rookie numbers
    boolean[][][] snapshots;
    int lowestRepeat = Integer.MAX_VALUE;       // IT'S OVER NINE-THOUSAAAAAAAAAAAND !!!!!!

    public GameOfLife (int size) {
        this.size   = size;
        snapshots   = new boolean[SNAPSHOTS][size][size];

        // Generate grid
        grid = new int[size][size];
        Random r = new Random();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                grid[x][y] = r.nextBoolean() ? 1 : 0;
    }
    public GameOfLife (int initialState[][]) {
        this.size   = initialState.length;
        snapshots   = new boolean[SNAPSHOTS][size][size];

        grid        = initialState;
    }

    public void nextState () {
        int[][] nextGrid = new int[size][size];

        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                int alive = (grid[x][y] & 0b1);
                int neighbours = liveNeighbours(x, y);

                // BRAAANCHLESSS!S!!!!
                nextGrid[x][y] = (grid[x][y] & ~0b1) + (alive << 1);                 // Increment next value, and clear 1st bit
                nextGrid[x][y] |= ((neighbours | alive) - 0b11 == 0) ? 0b1 : 0b0;    // Set 1st bit

                // if ((neightbours | alive) == 3) så er cellen i live
                // derfor kan man - med 3 på begge sider
                // (neightbours | alive) - 3 == 0


            }
        }
        grid = nextGrid;
        step++;
        checkPeriodic();
    }

    int liveNeighbours (int x, int y) {
        int n = 0;
        for (int dy = -1; dy <= 1; dy++){
            for (int dx = -1; dx <= 1; dx++){
                if (dx == 0 && dy == 0) continue; // Dont count center
                n += (grid[(x + dx + size) % size][(y + dy + size) % size] & 0b1);
            }
        }
        return n;
    }

    // Stores a snapshot every 10, 100, 1,000 and 10,000 steps
    void checkPeriodic () {
        boolean[][] aliveGrid = generateAliveGrid();
        for (int i = SNAPSHOTS; i > 0; i--) {
            int deltaStep = (int)Math.pow(10, i);
            // Check
            if (Arrays.deepEquals(aliveGrid, snapshots[i-1])) {     // Compare alive grid to snapshot
                int steps = step % deltaStep;
                if (steps == 0) steps += deltaStep;
                
                if (lowestRepeat > steps){
                    lowestRepeat = steps;
                    System.out.println("Repeats itself every " + (lowestRepeat)+ " steps at:" + step);
                }
            }

            // Add after checking
            if (step % deltaStep == 0) {
                snapshots[i-1] = aliveGrid;
            }
        }
    }

    boolean[][] generateAliveGrid () {
        boolean[][] aliveGrid = new boolean[size][size];
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                aliveGrid[x][y] = (grid[x][y] & 0b1) == 1;
        return aliveGrid;
    }

    public int getSize () {
        return size;
    }
    public int[][] getGrid() {
        return grid;
    }

    public String toString () {
        String s = "";
        // Print map
        for (int y = 0; y < grid.length; y++){
            s += Arrays.toString(grid[y]);
        }
        return s;
    }

}
