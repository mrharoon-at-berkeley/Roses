package byow.Core;

import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Game {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;
    private boolean gameOver;
    private Map map;
    private long seed;

    public Game(long seed) {
        /** This will create a new game */
        this.seed = seed;
        map = new Map();
    }

    public void createNewGame() {}
    public void saveGame() {}
    public void loadPreviousGame() {}
    public void startGame() {
        //TODO: waiting for segej to make player movement
        drawHUD();
    }
    private void drawHUD() {
        if (!gameOver) {
            Font fontSmall = new Font("Monaco", Font.PLAIN, 15);
            StdDraw.setFont(fontSmall);
            StdDraw.textLeft(0.0, HEIGHT - 1, tileAtMouse());
            StdDraw.textRight(width - 1, height - 1,
                    ENCOURAGEMENT[this.rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.text(this.width / 2, height - 1, playerTurn ? "Type!" : "Watch!");

        }
    }
    private Tileset tileAtMouse() {
        return map.tileAt(StdDraw.mouseX(), StdDraw.mouseY());
    }
}
