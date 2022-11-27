package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TETile;
import java.util.Date;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

//TODO: REMOVE UNNECESSARY CODE


public class Game {
    private boolean gameOver;
    private Map map;
    private String input;
    private long seed;
    private boolean render;
    private boolean gameStarted;
    public boolean isLoadingForGui;

    public Game(long seed, boolean render) {
        /** This will create a new game */
        this.seed = seed;
        this.render = render;
        input = "";
        map = new Map(seed, render); // renders map
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
            while (map.getNumberOfFlowers() != 0) {
                map.render();
                if (inputSource.possibleNextInput()) {
                    while (!StdDraw.hasNextKeyTyped()) {
                        showTileAtMouse();
                    }
                    char c = Character.toUpperCase(inputSource.getNextKey());
                    switch (c) {
                        case 'W':
                            showTileAtMouse();
                            map.moveAvatarUp();
                            input += 'W';
                            break;
                        case 'A':
                            showTileAtMouse();
                            map.moveAvatarLeft();
                            input += 'A';
                            break;
                        case 'S':
                                showTileAtMouse();
                            map.moveAvatarDown();
                            input += 'S';
                            break;
                        case 'D':
                                showTileAtMouse();
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
            win();
        } else { // bc string input, cannot work in real time
            inputSource = new StringInputDevice(input);
            while (inputSource.possibleNextInput()) {
                char c = Character.toUpperCase(inputSource.getNextKey());
                switch (c) {
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
                    case 'Q':
                        if (isLoadingForGui) {
                            System.exit(0);
                        }
                    case ':':
                        if (isLoadingForGui) {
                            if (inputSource.possibleNextInput()) {
                                char ch = Character.toUpperCase(inputSource.getNextKey());
                                if (ch == 'Q') {
                                    saveGame();
                                }

                                System.exit(0);
                            }
                        }
                }
            }
        }
        return map.getWorld();
    }

    private void win() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font winningFont = new Font("Times New Roman", Font.BOLD, 40);
        StdDraw.setFont(winningFont);
        StdDraw.text(map.getWidth()/2, map.getHeight()/2,
                "Congratulations! You beat this donkey of a game!");
        StdDraw.show();
        StdDraw.pause(1000);
        System.exit(0);
    }

    private void showTileAtMouse() {
        map.render();
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0,map.getHeight()-2,
                map.tileAt((int)StdDraw.mouseX(), (int)StdDraw.mouseY()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        StdDraw.textRight(map.getWidth(), map.getHeight()-2,
                formatter.format(date));
        StdDraw.show();
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
