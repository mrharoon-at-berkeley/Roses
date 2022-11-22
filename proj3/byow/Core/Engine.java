package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.ArrayList;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean gameOver;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public Engine() {
        gameOver = false;
    }
    public void interactWithKeyboard() {
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
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

//        TETile[][] finalWorldFrame = null;
//        return finalWorldFrame;

        /* Suppose real game starting from menu. */
        /* The player either loads, starts, or quits
                for (int i = 0; i < input.length; i++) {
                    if (input[i].toUpperCase() == 'N') {
                        createNewGame(input.substring(i:));
                    }
                    if (input[i].upperCase() == 'L') {
                        loadPreviousGame(input.substring(i:));
                    }
                    if (input[i].toUpperCase() == 'Q') {
                        quit();
                    }
                }
        *
        * */

    }

    /* private _____ createNewGame(String restOfInput) {
        String seed = "" ;
        int index = 0;
        while (input[index] is an int) {
            seed += input[index];
        }
    *   generateWorld(Long.parseLong(seed));
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
     */
}
