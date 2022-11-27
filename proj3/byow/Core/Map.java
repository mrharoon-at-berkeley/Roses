package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
//TODO: REMOVE UNNECESSARY CODE

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Map {
    public static final int w = 70; //j a decent width size
    public static final int h = 35; //j a decent height size
    static final String[] directions = new String[]{"up", "down", "left", "right"}; //directions

    private Random random;
    private long seed;
    private TETile[][] encounterWorld = new TETile[w][h];

    private static TETile[][] mainWorld = new TETile[w][h];
    private Avatar mainAvatar;
    private Avatar encounterAvatar;
    private TERenderer ter;
    private int numberOfDoors;
    private int numberOfFlowers;
    private boolean encounter;
    private boolean render;


    public Map(long seed, boolean render) {
        this.seed = seed;
        numberOfFlowers = 25;
        numberOfDoors = 5;
        encounter = false;
        random = new Random(seed);
        this.render = render;
        ter = new TERenderer();

//        ter.initialize(w, h);

        //make evryting outside initially
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                mainWorld[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                encounterWorld[x][y] = Tileset.NOTHING;
            }
        }

        // make encounter World without flowers
        for (int x = 17; x < 35; x += 1) {
            for (int y = 7; y < 15; y += 1) {
                encounterWorld[x][y] = Tileset.WALL;
            }
        }
        for (int x = 18; x < 34; x += 1) {
            for (int y = 8; y < 14; y += 1) {
                encounterWorld[x][y] = Tileset.FLOOR;
            }
        }

        //find us a random tile
        int randomX = RandomUtils.uniform(random, w);
        int randomY = RandomUtils.uniform(random, h);

        //pick a random direction
        String randomD1 = directions[RandomUtils.uniform(random, 4)];
        String randomD2 = directions[RandomUtils.uniform(random, 4)];

        //now choose random lengths lol
        int randomD1L = RandomUtils.uniform(random, 3);
        int randomD2L = RandomUtils.uniform(random, 5);

        System.out.println("X: " + randomX + " Y: " + randomY + " D1: " + randomD1 + " D2: " + randomD2 + " D1L: " + randomD1L + " D2L: " + randomD2L);

        int[] firstpop = populate(randomX, randomY, randomD1, randomD2, randomD1L, randomD2L);

        int nowX = firstpop[0];

        System.out.println(nowX);

        int nowY = firstpop[1];

        System.out.println(nowY);

        for(int i = 0; i < 560; i++) {
            String nowD1 = directions[RandomUtils.uniform(random, 4)];
            String nowD2 = directions[RandomUtils.uniform(random, 4)];

            int nowD1L = RandomUtils.uniform(random, 3);
            int nowD2L = RandomUtils.uniform(random, 5);

            System.out.println("X: " + nowX + " Y: " + nowY + " D1: " + nowD1 + " D2: " + nowD2 + " D1L: " + nowD1L + " D2L: " + nowD2L);

            int[] secpop = populate(nowX, nowY, nowD1, nowD2, nowD1L, nowD2L);

            nowX = secpop[0];
            System.out.println(nowX);

            nowY = secpop[1];
            System.out.println(nowY);
        }

        for (int x = 1; x < w-1; x++) {
            for (int y = 1; y < h-1; y++) {
                if(mainWorld[x][y] == Tileset.NOTHING) {
                    if(mainWorld[x+1][y] == Tileset.FLOOR || mainWorld[x-1][y] == Tileset.FLOOR
                            || mainWorld[x][y+1] == Tileset.FLOOR || mainWorld[x][y-1] == Tileset.FLOOR
                            || mainWorld[x+1][y+1] == Tileset.FLOOR || mainWorld[x-1][y-1] == Tileset.FLOOR
                            || mainWorld[x-1][y+1] == Tileset.FLOOR || mainWorld[x+1][y-1] == Tileset.FLOOR) {
                        mainWorld[x][y] = Tileset.WALL;
                    }
                }
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(mainWorld[x][1] == Tileset.FLOOR || mainWorld[x+1][1] == Tileset.FLOOR || mainWorld[x-1][1] == Tileset.FLOOR) {
                mainWorld[x][0] = Tileset.WALL;
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(mainWorld[x][h-2] == Tileset.FLOOR || mainWorld[x+1][h-2] == Tileset.FLOOR || mainWorld[x-1][h-2] == Tileset.FLOOR) {
                mainWorld[x][h-1] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(mainWorld[1][y] == Tileset.FLOOR || mainWorld[1][y+1] == Tileset.FLOOR || mainWorld[1][y-1] == Tileset.FLOOR) {
                mainWorld[0][y] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(mainWorld[w-2][y] == Tileset.FLOOR || mainWorld[w-2][y+1] == Tileset.FLOOR || mainWorld[w-2][y-1] == Tileset.FLOOR) {
                mainWorld[w-1][y] = Tileset.WALL;
            }
        }
        // before doors and avatars are generated,
        // clear top 3 rows for HUD
        for (int x = 0; x < w; x++) {
            for (int y = h-1; y > h-4; y--) {
                mainWorld[x][y] = Tileset.NOTHING;
            }
        }
        // then create walls in the top 4th row
        for (int x=0; x < w;x++) {
            if (mainWorld[x][h-4] == Tileset.FLOOR) {
                mainWorld[x][h-4] = Tileset.WALL;
            }
            //||
            //            mainWorld[x][h-4] == Tileset.WALL
        }

        generateAvatars();

        // create doors
        while (numberOfDoors != 0) {
            int randomW = RandomUtils.uniform(random, w);
            int randomH = RandomUtils.uniform(random, h);
            if (mainWorld[randomW][randomH] == Tileset.FLOOR) {
                mainWorld[randomW][randomH] = Tileset.UNLOCKED_DOOR;
                numberOfDoors--;
            }

        }


        if (render) {
            ter.renderFrame(mainWorld);
        }
    }

    public void render() {
        if (!encounter) {
            ter.renderFrame(mainWorld);
        } else {
            ter.renderFrame(encounterWorld);
        }
        render = true;
    }

    private class Avatar {
        private int col;
        private int row;


    }


    private void generateAvatars() {
        /** Creates an avatar at a random valid location */
        int avatarColumn = RandomUtils.uniform(random, w);
        int avatarRow = RandomUtils.uniform(random, h);
        while (mainWorld[avatarColumn][avatarRow] != Tileset.FLOOR) {
            avatarColumn = RandomUtils.uniform(random, w);
            avatarRow = RandomUtils.uniform(random, h);
        }
        mainWorld[avatarColumn][avatarRow] = Tileset.AVATAR;
        mainAvatar = new Avatar();
        mainAvatar.col = avatarColumn;
        mainAvatar.row = avatarRow;
        while (encounterWorld[avatarColumn][avatarRow] != Tileset.FLOOR) {
            avatarColumn = RandomUtils.uniform(random, 18,34);
            avatarRow = RandomUtils.uniform(random, 8, 14);
        }
        encounterWorld[avatarColumn][avatarRow] = Tileset.AVATAR;
        encounterAvatar = new Avatar();
        encounterAvatar.col = avatarColumn;
        encounterAvatar.row = avatarRow;
    }

    public void moveAvatarRight() {
        moveAvatar(1, 0);
//        if (mainWorld[mainAvatar.col + 1][mainAvatar.row] == Tileset.FLOOR) {
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.FLOOR;
//            mainAvatar.col += 1;
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.AVATAR;
//        }
    }
    public void moveAvatarLeft() {
        moveAvatar(-1, 0);
//        if (mainWorld[mainAvatar.col -1][mainAvatar.row] == Tileset.FLOOR) {
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.FLOOR;
//            mainAvatar.col -= 1;
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.AVATAR;
//        }
    }
    public void moveAvatarUp() {
        moveAvatar(0, 1);
//        if (!encounter) {
//            if (mainWorld[mainAvatar.col][mainAvatar.row + 1] == Tileset.FLOOR) {
//                mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.FLOOR;
//                mainAvatar.row += 1;
//                mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.AVATAR;
//            } else if (mainWorld[mainAvatar.col][mainAvatar.row + 1] == Tileset.UNLOCKED_DOOR) {
//                mainWorld[mainAvatar.col][mainAvatar.row + 1] = Tileset.LOCKED_DOOR;
//                for (int i = 0; i < 5; i++) { // randomly generate flowers
//                    int randomW = RandomUtils.uniform(random, 18, 34);
//                    int randomH = RandomUtils.uniform(random, 8, 14);
//                    if (encounterWorld[randomW][randomH] == Tileset.FLOOR) {
//                        encounterWorld[randomW][randomH] = Tileset.FLOWER;
//                    } else {
//                        i--;
//                    }
//                }
//                //TODO: generate encounter
//                encounter = true;
//                ter.renderFrame(encounterWorld);
//            }
//        } else {
//            if (encounterWorld[encounterAvatar.col][encounterAvatar.row +1] == Tileset.FLOWER ||
//                    encounterWorld[encounterAvatar.col][encounterAvatar.row +1] == Tileset.FLOOR) {
//                if (encounterWorld[encounterAvatar.col][encounterAvatar.row +1] == Tileset.FLOWER) {
//                    numberOfFlowers--;
//                    if (numberOfFlowers % 5 == 0) {
//                        encounter = false;
//                        ter.renderFrame(mainWorld);
//                    }
//                }
//                encounterWorld[encounterAvatar.col][encounterAvatar.row] = Tileset.FLOOR;
//                encounterAvatar.row += 1;
//                encounterWorld[encounterAvatar.col][encounterAvatar.row] = Tileset.AVATAR;
//            }
//        }
    }
    public void moveAvatarDown() {
        moveAvatar(0, -1);
//        if (mainWorld[mainAvatar.col][mainAvatar.row -1] == Tileset.FLOOR) {
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.FLOOR;
//            mainAvatar.row -= 1;
//            mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.AVATAR;
//        }
    }

    private void moveAvatar(int x, int y) {
        if (!encounter) {
            if (mainWorld[mainAvatar.col+x][mainAvatar.row+y] == Tileset.FLOOR) {
                mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.FLOOR;
                if (x == 0) {
                    mainAvatar.row += y;
                } else {
                    mainAvatar.col += x;
                }
                mainWorld[mainAvatar.col][mainAvatar.row] = Tileset.AVATAR;
            } else if (mainWorld[mainAvatar.col+x][mainAvatar.row + y] == Tileset.UNLOCKED_DOOR) {
                mainWorld[mainAvatar.col+x][mainAvatar.row + y] = Tileset.LOCKED_DOOR;
                for (int i = 0; i < 5; i++) { // randomly generate flowers
                    int randomW = RandomUtils.uniform(random, 18, 34);
                    int randomH = RandomUtils.uniform(random, 8, 14);
                    if (encounterWorld[randomW][randomH] == Tileset.FLOOR) {
                        encounterWorld[randomW][randomH] = Tileset.FLOWER;
                    } else {
                        i--;
                    }
                }
                //TODO: generate encounter
                encounter = true;
                ter.renderFrame(encounterWorld);
            }
        } else {
            if (encounterWorld[encounterAvatar.col+x][encounterAvatar.row +y] == Tileset.FLOWER ||
                    encounterWorld[encounterAvatar.col+x][encounterAvatar.row +y] == Tileset.FLOOR) {
                if (encounterWorld[encounterAvatar.col+x][encounterAvatar.row +y] == Tileset.FLOWER) {
                    numberOfFlowers--;
                    if (numberOfFlowers % 5 == 0) {
                        encounter = false;
                        ter.renderFrame(mainWorld);
                    }
                }
                encounterWorld[encounterAvatar.col][encounterAvatar.row] = Tileset.FLOOR;
                if (x==0) {
                    encounterAvatar.row += y;
                } else {
                    encounterAvatar.col += x;
                }
                encounterWorld[encounterAvatar.col][encounterAvatar.row] = Tileset.AVATAR;
            }
        }
    }


    public String tileAt(int x, int y) {
        return mainWorld[x][y].description();
    }
    public int getWidth() {
        return w;
    }
    public int getHeight() {
        return h;
    }

    public int getNumberOfFlowers() {
        return numberOfFlowers;
    }

    private int[] populate(int X, int Y, String D1, String D2, int D1L, int D2L) {
        int initialX = X;
        int initialY = Y;

        mainWorld[X][Y] = Tileset.FLOOR;

        switch (D1) {
            case "right" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y)[0] != 0) {
                        initialX = reBound(X, Y)[0];
                        X = reBound(X, Y)[0];
                    }
                    mainWorld[++X][Y] = Tileset.FLOOR;

                    System.out.println("D1 Right-- X position:" + X);
                }
                break;
            }
            case "left" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y)[0] != 0) {
                        initialX = reBound(X, Y)[0];
                        X = reBound(X, Y)[0];
                    }
                    mainWorld[--X][Y] = Tileset.FLOOR;

                    System.out.println("D1 Left-- X position:" + X);
                }
                break;
            }
            case "up" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y)[1] != 0) {
                        initialY = reBound(X, Y)[1];
                        Y = reBound(X, Y)[1];
                    }
                    mainWorld[X][++Y] = Tileset.FLOOR;

                    System.out.println("D1 Up-- Y position:" + Y);
                }
                break;
            }
            case "down" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y)[1] != 0) {
                        initialY = reBound(X, Y)[1];
                        Y = reBound(X, Y)[1];
                    }
                    mainWorld[X][--Y] = Tileset.FLOOR;

                    System.out.println("D1 Down-- Y position:" + Y);
                }
                break;
            }
        }

        switch(D2) {
            case "right" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y)[0] != 0) {
                        X = reBound(X, Y)[0];
                    }
                    mainWorld[++X][Y] = Tileset.FLOOR;

                    System.out.println("D2 Right-- X position:" + X);
                }
                if (initialY < Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y >= initialY; y--) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate1");
                }
                if (initialY > Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y <= initialY; y++) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate2");

                }
                break;
            }

            case "left" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y)[0] != 0) {
                        X = reBound(X, Y)[0];
                    }
                    mainWorld[--X][Y] = Tileset.FLOOR;

                    System.out.println("D2 Left-- X position:" + X);
                }
                if (initialY < Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y >= initialY; y--) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate3");

                }
                if (initialY > Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y <= initialY; y++) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate4");

                }
                break;
            }

            case "up" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y)[1] != 0) {
                        Y = reBound(X, Y)[1];
                    }
                    mainWorld[X][++Y] = Tileset.FLOOR;

                    System.out.println("D2 Up-- Y position:" + Y);
                }
                if (initialX < X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate5");

                }
                if (initialX > X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate6");

                }
                break;
            }
            case "down" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y)[1] != 0) {
                        Y = reBound(X, Y)[1];
                    }
                    mainWorld[X][--Y] = Tileset.FLOOR;

                    System.out.println("D2 Down-- Y position:" + Y);
                }
                if (initialX < X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate7");

                }
                if (initialX > X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            mainWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate8");
                }
            }
            break;
        }

        return new int[] {X,Y};
    }

    private static int[] reBound(int X, int Y) {
        boolean changed = false;

        if(X > w-4) {
            for (int i = 0; i < (X-w); i++) {
                mainWorld[X--][Y] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a high X");
            X = (w - 3 - 4);
        } else if (X < 4) {
            for (int i = 0; i < 4; i++) {
                mainWorld[X++][Y] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a low X");
            X = (3 + 4);
        }
        if (Y > h-4) {
            for (int i = 0; i < 4; i++) {
                mainWorld[X][Y--] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a high Y");
            Y = (h - 3 - 4);
        } else if (Y < 4) {
            for (int i = 0; i < 4; i++) {
                mainWorld[X][++Y] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a low Y");
            Y = (3 + 4);
        }
        if (!changed) {
            return new int[] {0,0};
        }
        return new int[] {X,Y};
    }

    public static TETile[][] getWorld() {
        return mainWorld;
    }
}


