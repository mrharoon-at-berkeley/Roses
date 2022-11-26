package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
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
    private boolean gameStarted;

    public Game(long seed, boolean render) {
        /** This will create a new game */
        this.seed = seed;
        this.render = render;
        input = "";
        map = new Map(seed, render);
        gameStarted = true;
    }

    public void render() {
        map.render();
        render = true;
        startGame();
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
    public void addInput(String s) {
        input += s;
    }


    public TETile[][] startGame() {
        //TODO?: When does game end?
        InputSource inputSource;
        if (render) {
            inputSource = new KeyboardInputSource();
            while (inputSource.possibleNextInput()) {
                map.render();
                StdDraw.setPenColor(Color.WHITE);
                Font fontSmall = new Font("Monaco", Font.BOLD, 20);
                StdDraw.setFont(fontSmall);
                while (!StdDraw.isMousePressed()) { // this condition is just to test
                    StdDraw.pause(100);
                }
//                } else {
//                    StdDraw.textLeft(map.getWidth(), map.getHeight() - 1,
//                            map.tileAt(0, 0));
//                }
                StdDraw.textLeft(map.getWidth(), map.getHeight() - 1,
                        map.tileAt((int) StdDraw.mouseX(), (int) StdDraw.mouseY()));
                StdDraw.line(0, 0, 40, 40);
                StdDraw.show();
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
        } else {
            inputSource = new StringInputDevice(input);
            while (inputSource.possibleNextInput()) {
                char c = Character.toUpperCase(inputSource.getNextKey());
                switch (c) { // will it skip numbers?
                    case 'W':
                        map.moveAvatarUp();
                        break;
                    case 'A':
                        map.moveAvatarLeft();
                        break;
                    case 'S':
                        map.moveAvatarDown();
                        break;
                    case 'D':
                        map.moveAvatarRight();
                        break;
//                    case 'Q':
//                        System.exit(0);
//                    case ':':
//                        if (inputSource.possibleNextInput()) {
//                            char ch = Character.toUpperCase(inputSource.getNextKey());
//                            if (ch == 'Q') {
//                                saveGame();
//                            }
//
//                            System.exit(0);
//                        }
                }
            }
        }
        return map.getWorld();
    }


    private boolean mouseLocationNotDefault(int x, int y) {
        return x != 0.0 || y != 0.0;
    }

    private void drawHUD() {
        StdDraw.setPenColor(Color.white);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0.0, map.getHeight() - 1, "hi");
        StdDraw.show();
    }
}
