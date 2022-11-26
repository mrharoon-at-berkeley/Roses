package byow.InputDemo;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class TestMouseInput {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(40, 40);

        // initialize tiles
        TETile[][] world = new TETile[40][40];
        for (int x = 0; x < 40; x += 1) {
            for (int y = 0; y < 40; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 20; x < 35; x += 1) {
            for (int y = 5; y < 10; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setYscale(0, 40);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        Font fontSmall = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.text(40 / 2, 40 - 1, "Watch!");


        StdDraw.show();
    }
}
