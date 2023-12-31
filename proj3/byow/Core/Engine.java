package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//TODO: REMOVE UNNECESSARY CODE

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private Game game;
    private String input;
    private String stringSeed;
    private long seed;
    private final boolean render;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public Engine(boolean render) {
        this.render = render;
        if (render) {
            StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16); // starts Java visual
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.WHITE);
        }
        stringSeed = "";
    }

    public void interactWithKeyboard() {
        /** Called when user wants to use GUI menu and wants to
         * explore the world using their keyboard.
         */
        drawMainMenu();


    }

    private void drawMainMenu() {

        Font fontLarge = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontLarge);
        StdDraw.text(WIDTH / 2, HEIGHT / 4 * 3, "CS61B: THE GAME");
        StdDraw.show();

        Font fontSmall = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");

        if (!hasSavedFile()) {
            StdDraw.setPenColor(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Load Game (L) (No saved file yet!)");
            StdDraw.setPenColor(Color.WHITE);
        } else {
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
        if (
                ((Character.toUpperCase(key) == 'L' && !hasSavedFile())
                        || Character.toUpperCase(key) != 'L') &&
                        Character.toUpperCase(key) != 'N' &&
                        Character.toUpperCase(key) != 'Q') {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(100);
            }
            userMenuCommand(StdDraw.nextKeyTyped());
        }
        switch (Character.toUpperCase(key)) {
            case 'N':
                drawSeedInputMenu("");
                createGame(true, true);
                break;
            case 'L':
                loadPreviousGame();
                break;
            case 'Q':
                System.exit(0);
        }
    }

    private TETile[][] createGame(boolean render, boolean loadGUI) {
        /** makes GUI game */
        game = new Game(seed, render); // Creates GUI if render is true
        if (render) {
            game.addInput("N" + seed + "S");
        } else {
            game.addInput(input);
            game.isLoadingForGui = loadGUI;
        }
        return game.startGame();
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
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (c == 'S') {
                StdDraw.clear(Color.BLACK);
                StdDraw.show();
                this.seed = Long.parseLong(seed);
            } else if (Character.isDigit(c)) {
                drawSeedInputMenu(seed + c);
                StdDraw.show();
            } else if (c == '\b') {
                drawSeedInputMenu(seed.substring(0, seed.length() - 1));
            } else {
                drawSeedInputMenu(seed);
                StdDraw.show();
            }
        }
    }


    private void loadPreviousGame() {
        try {
            File file = new File("saved_game.txt");

            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                String input = scanner.nextLine();
                interactWithInputString(input); // creates a game unrendered and returns TETiles
                game.render(); // render and start game
            } else {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasSavedFile() {
        File file = new File("saved_game.txt");
        return file.exists();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        /** Called when a user gives the program a command
         * line argument, describing how they want to generate
         * the random world and what exploration they wish to complete.
         */
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        this.input = input;
        int i = 0;
        if (Character.toUpperCase(input.charAt(i)) == 'N') {
            // creates a TETile world without rendering it.
            while (Character.toUpperCase(input.charAt(i + 1)) != 'S'
                    && Character.isDigit(input.charAt(i + 1))) {
                stringSeed += input.charAt(i + 1);
                i++;
            }
            seed = Long.parseLong(stringSeed);
            return createGame(false, false);
        } else {
            if (hasSavedFile()) {
                try {
                    File file = new File("saved_game.txt");
                    Scanner scanner = new Scanner(file);
                    return interactWithInputString(scanner.nextLine() + input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}