package byow.Core;

public class Game {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private boolean gameOver;
    private long seed;

    public Game(long seed) {
        /** This will create a new game */
        this.seed = seed;
    }

    private void createNewGame() {}
    private void saveGame() {}
    private void loadPreviousGame() {}
}
