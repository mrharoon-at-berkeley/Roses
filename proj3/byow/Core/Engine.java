package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import org.eclipse.jetty.util.IO;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private Game game;
    private String input;
    private boolean gameStarted;
    private long seed;
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public Engine() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
    }

    public void interactWithKeyboard() {
        /** Called when user wants to use GUI menu and wants to
         * explore the world using their keyboard.
         */
        drawMainMenu();
        //playGame();


    }

    private void drawMainMenu() {

        Font fontLarge = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontLarge);
        StdDraw.text(WIDTH / 2, HEIGHT/4*3, "CS61B: THE GAME");
        StdDraw.show();

        Font fontSmall = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        if (hasSavedFile()) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Load Game (L)");
        }
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Quit (Q)");
        StdDraw.show();

        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(100);
        }
        if (StdDraw.hasNextKeyTyped()) {
            userMenuCommand(StdDraw.nextKeyTyped());
        }
    }

    private void userMenuCommand(char key) {
        switch (Character.toUpperCase(key)) {
            case 'N':
                drawSeedInputMenu("");
                createNewGame(true);
                break;
            case 'L':
                loadPreviousGame();
                // after
                break;
            case 'Q':
                System.exit(0);
        }
    }

    private void createNewGame(boolean render) {
        /** makes GUI game */
        game = new Game(seed, render);
        game.addInput("N");
        game.startGame();
    }

    private void drawSeedInputMenu(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        Font fontLarge = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontLarge);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "PLEASE ENTER SEED:");

        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 4, seed);
        StdDraw.show();

        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(100);
        }
        if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            if (Character.toUpperCase(c) == 'S') {
                StdDraw.clear(Color.BLACK);
                StdDraw.show();
                input+= seed + c;
                this.seed = Long.parseLong(seed);
                return;
            }
            drawSeedInputMenu(seed + c);
            StdDraw.show();
        }
    }




    private void loadPreviousGame() {
        //TODO: use interactWithStringInput to save time
        try {
            File file = new File("saved_game.txt");

            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                String input = scanner.nextLine();
                TETile[][] map = interactWithInputString(input);
                //game.loadMap(map);
            } else {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasSavedFile() {
        //TODO: use interactWithStringInput to save time
        File file = new File("saved_game.txt");
        return file.exists();
    }

    /* private _____ createNewGame(String restOfInput) {
        String seed = "" ;
        String playerInput = "";
        int index = 0;
        while (input[index] is an int) {
            seed += input[index];
        }
        for (int i = 0; i < input.length; i++) {
            if (input[i].toUpperCase() == 'Q') {
                if (input[i-1] == ':') {
                    make txt file
                    save input in it
                }
                quit(); //kill program
            }
            playerInput
        }
    *   generateWorld(Long.parseLong(seed),);
    *   generateHUD();
    *   startGame();
    * } */

    /* private ____ generateWorld(long seed) {
     *   // does what testMain does to generate world randomly
     *       // testMain should also generate an avatar and door
     * }*/

    /* private ____ generateHUD() {
     *   // uses Std.draw and instance variables to create and update HUD
     * } */

    /*
    private void startGame(String input) {
        int index = 0;
           while (!gameOver) {
            drawFrame();
            if (input[index] == ':' $$ input[index+1] == 'Q') {
                quit();
            }
            if (input[index].toUppercase == 'W') {
                moveUp();
            if (input[index].toUppercase == 'A') {
                moveLeft();
           }
           if (input[index].toUppercase == 'S') {
                moveDown();
           }
           if (input[index].toUppercase == 'D') {
                moveRight();
           }
    }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        /** Called when a user gives the program a command
         * line argument, describing how they want to generate
         * the random world and what exploration they wish to complete.
         */
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        // TODO: should NOT render tiles
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case 'N':
                    // creates a TETile world without rendering it.
                    while (Character.toUpperCase(input.charAt(i)) != 'S'
                            && Character.isDigit(input.charAt(i))) {
                        seed += input.charAt(i);
                    }
                    createNewGame(false);
                    break;
                case 'L': //TODO: load saved game file

            }
        }

        /*read input[0];
        if L, loadPreviousGame();
        if N, createNewGame();
        */
        return null;
    }

    /* private _____ createNewGame(String restOfInput) {
        String seed = "" ;
        String playerInput = "";
        int index = 0;
        while (input[index] is an int) {
            seed += input[index];
        }
        for (int i = 0; i < input.length; i++) {
            if (input[i].toUpperCase() == 'Q') {
                if (input[i-1] == ':') {
                    make txt file
                    save input in it
                }
                quit(); //kill program
            }
            playerInput
        }
    *   generateWorld(Long.parseLong(seed),);
    *   generateHUD();
    *   startGame();
    * } */

    /* private ____ generateWorld(long seed) {
    *   // does what testMain does to generate world randomly
    *       // testMain should also generate an avatar and door
    * }*/

    /* private ____ generateHUD() {
    *   // uses Std.draw and instance variables to create and update HUD
    * } */

    /*
    private void startGame(String input) {
        int index = 0;
           while (!gameOver) {
            drawFrame();
            if (input[index] == ':' $$ input[index+1] == 'Q') {
                quit();
            }
            if (input[index].toUppercase == 'W') {
                moveUp();
            if (input[index].toUppercase == 'A') {
                moveLeft();
           }
           if (input[index].toUppercase == 'S') {
                moveDown();
           }
           if (input[index].toUppercase == 'D') {
                moveRight();
           }
    }

                            ANOTHER SOLUTION
          read through user input, change coordinate of avatar before loading world,
          then load world with new location.
     */
}
