package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game {
    private boolean gameOver;
    private Map map;
    private String input;
    private long seed;
    private boolean render;

    public Game(long seed, boolean render) {
        /** This will create a new game */
        this.seed = seed;
        input = "";
        map = new Map(seed, render);
    }

    public String getGameInput() {
        return input;
    }
    private void writeFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(input);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {
        try {
            File file = new File("saved_game.txt");
            if (file.createNewFile()) {
                writeFile(file);
            } else {
                if (file.delete()) {
                    saveGame();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void addInput(String s) {
        input += s;
    }

    public void startGame() {
        //TODO: When does game end?
        InputSource inputSource = new KeyboardInputSource();
        while (!gameOver) {
            if (inputSource.possibleNextInput()) {
                char c = Character.toUpperCase(inputSource.getNextKey());
                switch (c) {
                    case 'W':
                        map.moveAvatarUp();
                        input += 'W';
                        break;
                    case 'A':
                        map.moveAvatarLeft();
                        input += 'A';
                        break;
                    case 'S':
                        map.moveAvatarDown();
                        input += 'S';
                        break;
                    case 'D':
                        map.moveAvatarRight();
                        input += 'D';
                        break;
                    case 'Q':
                        input += 'Q';
                        System.exit(0);
                    case ':':
                        if (inputSource.possibleNextInput()) {
                            char ch = Character.toUpperCase(inputSource.getNextKey());
                            if (ch == 'Q') {
                                input += ":Q";
                                saveGame();
                            }

                            System.exit(0);
                        }
                }
            }
        }
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