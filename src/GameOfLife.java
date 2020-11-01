import java.util.Arrays;
import java.util.Random;

public class GameOfLife {
    static final int MEANING = 42;

    int step = 0;

    // Size of grid
    int size;

    // Data of grid:
    // 1st bit is alive
    // 2nd to 5th bit is neighbours
    // 6th to 32nd bit is counter
    int[][] grid;

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

        createNeighbourGrid();

    }
    public GameOfLife (int initialState[][]) {
        this.size   = initialState.length;
        snapshots   = new boolean[SNAPSHOTS][size][size];

        grid        = initialState;

        createNeighbourGrid();
    }

    /* TIL LUKAS OG NIELS
    * OK Lukas og Niels, her forklarer jeg lige hvordan det fungere
    * I opgaven er der beskrevet at man skal gemme information i en int array
    * Problemet er bare at en int bruger 32 bits of data, men vi har kun brug for en bit til at fortælle om den er i live eller ej
    * Derfor vil der være 31 bits man ikke bruger til noget som helst, hvilket jo er træls -_-
    * Derfor valgte jeg bruge hver int til både at opbevare data der beskrev om den var i live, hvor mange naboer den har, og hvor mange gange den har været i live
    *
    * En int består af 32 bits, hvilket kan repræsentere således:
    * 00000000 00000000 00000000 00000000
    *
    * Her er en list over hvad hver bit beskriver
    * bit 32 - 6: En counter der tæller antallet af gange cellen har været i live       - har en værdi mellem 0 og 100.000.000 ca.  (27 bits)
    * bit  5 - 2: Naboer, hvor mange naboer                                             - har en værdi mellem 0 og 8                (4  bits)
    * bit      1: I live                                                                - har en værdi mellem 0 og 1                (1  bit)
    *
    *
    * Eksempel: hvis en int ser således ud
    * 00000000 00000000 00001100 11001101
    * Så kan man aflæse at
    * bit 32 - 6: 00000000 00000000 00001100 11 = [51]  // Cellen har været i live 51 gange
    * bit  5 - 2: 0110                          = [ 6]  // Cellen har 6 naboer
    * bit      1: 1                             = [ 1]  // Cellen er i live
    *
    *
    * Implementering:
    * For hver cyklus skal man:
    * Finde ud af om den er i live
    * Finde ud af hvor mange naboer den har
    * Inkrementer den gamle celles counter
    *
    * For at finde ud af om den er i live:
    * Her skal man isolere den første bit, hvilket man gør således
    * alive = (cell & 0b1)
    *
    * For at finde ud af hvor mange naboer den har:
    * Her skal man isolere bit 2, 3, 4 og 5, hvilket man gør således
    * neighbours = (cell & 0b11110)
    *
    * For at finde den gamle counter:
    * Her skal man isolere alle bits undtagen de første 5
    * counter = (cell & ~0b11111)
    *
    * For at inkrementere:
    * Her skal man inkrementere fra den 6. bit, hvilket kan gøres således
    * if (alive == 1) counter += 0b100000
    * Men det kan man også skrive som
    * counter += 0b100000 * alive
    * Eller
    * counter += (alive << 5)
    *
    * FRA JONAS */
    public void nextState () {
        int[][] nextGrid = new int[size][size];

        // Calculate alive
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                int alive       = (grid[x][y] &  0b1);                       // Alive      is bit 1
                int neighbours  = (grid[x][y] &  0b11110) >> 1;              // Neighbours is bit 5-2

                // Increment next value, and clear alive and neighbour bits
                nextGrid[x][y]  = (grid[x][y] & ~0b11111) + (alive << 5);
                // Set 1st bit as either alive or dead
                nextGrid[x][y] |= ((neighbours | alive) == 0b11) ? 0b1 : 0b0;
            }
        }

        // Calculate neighbours
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                // if bit 1 is set, then it is alive, and therefore add neighbours to next grid
                if ((nextGrid[x][y] & 0b1) == 0b1) addToNeighbours(nextGrid, x, y);
            }
        }

        grid = nextGrid;
        step++;
        checkPeriodic();
    }

    void createNeighbourGrid () {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                if ((grid[x][y] & 0b1) == 1) addToNeighbours(grid, x, y);
    }
    void addToNeighbours (int[][] neighbourGrid, int x, int y) {
        for (int dy = -1; dy <= 1; dy++){
            for (int dx = -1; dx <= 1; dx++){
                if (dx == 0 && dy == 0) continue; // Dont count center
                neighbourGrid[(x + dx + size) % size][(y + dy + size) % size] += 0b1 << 1;  // Increment from 2nd bit (neighbours begins there)
            }
        }
    }

    // Stores a snapshot every 10, 100, 1,000 and 10,000 steps
    void checkPeriodic () {
        boolean[][] aliveGrid = generateAliveGrid();
        for (int i = SNAPSHOTS; i > 0; i--) {
            int deltaStep = (int)Math.pow(10, i);

            // Compare alive grid to snapshot
            if (Arrays.deepEquals(aliveGrid, snapshots[i-1])) {
                int steps = step % deltaStep;
                if (steps == 0) steps += deltaStep;
                
                if (lowestRepeat > steps){
                    lowestRepeat = steps;
                    System.out.println("Repeats itself every " + (lowestRepeat)+ " steps at: " + step);
                }
            }

            // Set snapshot after checking
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
