import java.util.Random;

public class GameOfLife {

    int size;       // Size of grid
    int grid[][];   // Data of grid


    public GameOfLife (int size) {
        this.size = size;
        grid = new int[size][size];

        Random r = new Random();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                grid[x][y] = r.nextBoolean() ? 1 : 0;

        openWindow();
        drawGrid(1);

    }
    public GameOfLife (int initialState[][]) {

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
        drawGrid(msDelay);
    }

    int liveNeighbours (int x, int y){
        int n = 0;

        for (int dy = -1; dy <= 1; dy++){
            for (int dx = -1; dx <= 1; dx++){
                if (dx == 0 && dy == 0) continue; // Dont count center
                if (x + dx == -1 || x + dx == size || y + dy == -1 || y + dy == size) continue; // Overflow
                n += grid[x+dx][y+dy];
            }
        }

        return n;
    }

    // DRAW
    void drawGrid(int msDelay){
        StdDraw.clear();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                if (grid[x][y] == 1) StdDraw.point(x,y);
        StdDraw.show(msDelay);
    }

    void openWindow(){
        StdDraw.setCanvasSize(1000,1000);
        StdDraw.setScale(-1, size);
        StdDraw.setPenRadius( 1 / (float)(size + 1) );      // Size is equal to one point
        StdDraw.show(1);
    }
}
