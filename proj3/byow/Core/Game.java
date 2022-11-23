package byow.Core;

public class Game {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private boolean gameOver;
    private Map map;
    private long seed;

    public Game(long seed) {
        /** This will create a new game */
        this.seed = seed;
    }

    public void createNewGame() {}
    public void saveGame() {}
    public void loadPreviousGame() {}
    public void startGame() {
        //TODO: waiting for segej to make player movement
    }
}
