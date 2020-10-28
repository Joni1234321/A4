import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameOfLifeMain {
    static GameOfLife game;                 // ( ͡° ͜ʖ ͡°)

    static final boolean MANUAL = false;    // Sry sir, but i don't know how to drive stick!
    static final int FPS = 60;              // ThE huMaN eyE caN't SeE moRe tHaN 24 fpS

    public static void main(String[] args) throws FileNotFoundException {
        int[][] map = loadMap("src/map/pulsar.gol");
        if (map == null) return;

        game = new GameOfLife(map);
        openWindow(game);

        if (MANUAL)
            manualGame();
        else
            automaticGame(FPS);

    }

    // ==================== STARTING GAME  ===================== //
    static void automaticGame (int updatesPerSecond) {
        int msDelay = 1000 / updatesPerSecond;
        while(true) {
            game.nextState();
            drawGOL(game, msDelay);
        }
    }
    static void manualGame () {
        boolean running = true;
        Scanner console = new Scanner(System.in);
        while (running) {
            int n = getInput(console);
            for (int i = 0; i < n; i++)
                game.nextState();
            drawGOL(game, 1);
        }
    }

    // ==================== MAP GENERATION ===================== //
    static int[][] loadMap (String path) throws FileNotFoundException {
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
            while(reader.hasNextInt() && x < size) {            // Read every line
                grid[x][y] = reader.nextInt() == 1 ? 1 : 0;
                x++;
            }
            while (x < size) {                                  // Fill the rest with 0
                grid[x][y] = 0;
                x++;
            }
        }
        return grid;
    }

    // ==================== USER INPUT ========================= //
    static int getInput (Scanner console) {
        console = new Scanner(System.in);

        System.out.print("Enter amount of turns you want to skip: ");
        while (!console.hasNextInt()){
            console.nextLine();
            System.out.print("Please write an integer: ");
        }
        return console.nextInt();
    }


    // ==================== DRAW ENGINE ======================== //
    static void drawGOL(GameOfLife gol, int msDelay){
        StdDraw.clear();
        int[][] grid = gol.getGrid();
        int[][] gridCounter = gol.getGridCounter();
        int size = gol.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean alive = grid[x][y] != 0 ? true : false;
                StdDraw.setPenColor(colorFancy(alive, gridCounter[x][y]));
                StdDraw.filledSquare(x+.5f,y+.5f, 1/2f);

            }
        }
        StdDraw.show(msDelay);
    }

    static final int CANVAS_SIZE = 1024;
    static final int DEFAULT_CANVAS_SIZE = 512;
    static void openWindow(GameOfLife gol){
        int size = gol.getSize();

        StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
        StdDraw.setScale(0, size);
        StdDraw.setPenRadius( 1 / (size) / (CANVAS_SIZE / (double)DEFAULT_CANVAS_SIZE));    // Size is equal to one point
        StdDraw.show(1);
        drawGOL(gol, 1);
    }


    // ==================== COLOR SCHEMES ====================== //
    static final float SCALE1 = 5f, SCALE2 = 25f, SCALE3 = 5f;
    static Color colorNormal (boolean alive, int n) {
        if (alive) return Color.black;
        return Color.white;
    }
    static Color colorFancy (boolean alive, int n) {
        if (alive) return Color.black;
        int rgb = Color.HSBtoRGB(.25f + (((n/(SCALE1) / SCALE2) / SCALE2)) * .75f, .5f + (((n % SCALE1) / SCALE1) * .5f),1);
        return new Color(rgb);
    }
    static Color colorFancyInvert (boolean alive, int n){
        return colorFancy(!alive, n);
    }
}
