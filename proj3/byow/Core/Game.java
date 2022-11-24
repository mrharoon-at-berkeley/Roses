package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Game {
    private boolean gameOver;
    private Map map;
    private String input;
    private long seed;

    public Game() {
        /** This will create a new game */
        map = new Map();
    }

    public void addGameInput(String s) {
        input += s;
    }
    public String getGameInput() {
        return input;
    }

    public void createNewGame() {
    }

    public void saveGame() {
    }
    public long getSeed() {
        return seed;
    }

    public TETile getTile(int x, int y) {
        return map.getTile(x, y);
    }

    public int getMapHeight() {
        return map.getHeight();
    }

    public int getMapWidth() {
        return map.getWidth();
    }

    public int avatarLocX() {
        return map.avatarLocX();
    }

    public int avatarLocY() {
        return map.avatarLocY();
    }

    public void startGame() {
        //TODO: waiting for segej to make player movement
        drawHUD();
    }

    private void drawHUD() {
        /*
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
    }*/


    }
}