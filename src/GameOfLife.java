import java.util.Arrays;
import java.util.Random;

public class GameOfLife {

    int step = 0;
    int size;       // Size of grid
    int grid[][];   // Data of grid

    final int SNAPSHOTS = 4;
    int snapshots[][][];
    int lowestRepeat = Integer.MAX_VALUE;

    public GameOfLife (int size) {
        this.size = size;
        grid = new int[size][size];

        Random r = new Random();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                grid[x][y] = r.nextBoolean() ? 1 : 0;

        snapshots = new int[SNAPSHOTS][size][size];

        openWindow();
    }
    public GameOfLife (int initialState[][]) {
        this.size = initialState.length;
        grid = initialState;

        snapshots = new int[SNAPSHOTS][size][size];

        openWindow();
    }

    public void play (int msDelay) {
        while(true) {
            nextState(msDelay);
        }
    }
    public void nextState (int msDelay) {
        int nextGrid[][] = new int[size][size];

        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                int alive = grid[x][y];
                int neighbours = liveNeighbours(x, y);

                int next = 0;
                if (neighbours == 3) next = 1;
                else if (neighbours == 2 && alive == 1) next = 1;

                nextGrid[x][y] = next;
            }
        }
        grid = nextGrid;
        step++;
        checkPeriodic();

        drawGrid(msDelay);
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
        boolean added = false;
        for (int i = SNAPSHOTS; i > 0; i--) {
            // Check
            if (Arrays.deepEquals(grid, snapshots[i-1])) {
                int steps = (int)(step % Math.pow(10, i));
                if (lowestRepeat > steps){
                    lowestRepeat = steps;
                    System.out.println("Repeats itself every " + (lowestRepeat+1)+ " steps");
                }
            }

            // Add
            if (step % Math.pow(10, i) == 0) {
                snapshots[i-1] = grid;
                added = true;
            }

        }
    }

    // DRAW
    void drawGrid(int msDelay){
        StdDraw.clear();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                if (grid[x][y] != 0) StdDraw.point(x,y);
        StdDraw.show(msDelay);
    }

    final int CANVAS_SIZE = 1024;
    final int DEFAULT_CANVAS_SIZE = 512;
    void openWindow(){
        StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
        StdDraw.setScale(-1, size);
        StdDraw.setPenRadius( 1 / (double)((size + 2) / (CANVAS_SIZE / DEFAULT_CANVAS_SIZE)) );      // Size is equal to one point
        StdDraw.show(1);
        drawGrid(1);
    }
}
