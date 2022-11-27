package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private Game game;
    private String input;
    private String stringSeed;
    private long seed;
    private boolean render;

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
        switch (Character.toUpperCase(key)) {
            case 'N':
                drawSeedInputMenu("");
                createGame(true);
                break;
            case 'L':
                loadPreviousGame();
                // after
                break;
            case 'Q':
                System.exit(0);
        }
    }

    private TETile[][] createGame(boolean render) {
        /** makes GUI game */
        game = new Game(seed, render); // Creates GUI if render is true
        if (render) {
            game.addInput("N" + seed + "S");
        } else {
            game.addInput(input);
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
            char c = StdDraw.nextKeyTyped();
            if (Character.toUpperCase(c) == 'S') {
                StdDraw.clear(Color.BLACK);
                StdDraw.show();
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
        //TODO: use interactWithStringInput to save time
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
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        // TODO: should NOT render tiles
        this.input = input;
        int i = 0;
        if (Character.toUpperCase(input.charAt(i)) == 'N') {
            // creates a TETile world without rendering it.
                //TODO?
                /* get seed first then create game*/
                while (Character.toUpperCase(input.charAt(i + 1)) != 'S'
                        && Character.isDigit(input.charAt(i + 1))) {
                    stringSeed += input.charAt(i + 1);
                    i++;
                }
                seed = Long.parseLong(stringSeed);
                return createGame(false);
            } else {
            if (hasSavedFile()) {
                //TODO
                try {
                    File file = new File("saved_game.txt");
                    Scanner scanner = new Scanner(file);
                    return interactWithInputString(scanner.nextLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}