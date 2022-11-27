package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

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
    private TETile[][] cachedWorld = new TETile[w][h];

    private static TETile[][] activeWorld = new TETile[w][h];
    private Avatar avatar;
    private TERenderer ter;
    private int numberOfDoors;
    private boolean render;


    public Map(long seed, boolean render) {
        this.seed = seed;
        numberOfDoors = 5;
        random = new Random(seed);
        this.render = render;
        ter = new TERenderer();

//        ter.initialize(w, h);

        //make evryting outside initially
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                activeWorld[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                cachedWorld[x][y] = Tileset.NOTHING;
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
                if(activeWorld[x][y] == Tileset.NOTHING) {
                    if(activeWorld[x+1][y] == Tileset.FLOOR || activeWorld[x-1][y] == Tileset.FLOOR
                            || activeWorld[x][y+1] == Tileset.FLOOR || activeWorld[x][y-1] == Tileset.FLOOR
                            || activeWorld[x+1][y+1] == Tileset.FLOOR || activeWorld[x-1][y-1] == Tileset.FLOOR
                            || activeWorld[x-1][y+1] == Tileset.FLOOR || activeWorld[x+1][y-1] == Tileset.FLOOR) {
                        activeWorld[x][y] = Tileset.WALL;
                    }
                }
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(activeWorld[x][1] == Tileset.FLOOR || activeWorld[x+1][1] == Tileset.FLOOR || activeWorld[x-1][1] == Tileset.FLOOR) {
                activeWorld[x][0] = Tileset.WALL;
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(activeWorld[x][h-2] == Tileset.FLOOR || activeWorld[x+1][h-2] == Tileset.FLOOR || activeWorld[x-1][h-2] == Tileset.FLOOR) {
                activeWorld[x][h-1] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(activeWorld[1][y] == Tileset.FLOOR || activeWorld[1][y+1] == Tileset.FLOOR || activeWorld[1][y-1] == Tileset.FLOOR) {
                activeWorld[0][y] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(activeWorld[w-2][y] == Tileset.FLOOR || activeWorld[w-2][y+1] == Tileset.FLOOR || activeWorld[w-2][y-1] == Tileset.FLOOR) {
                activeWorld[w-1][y] = Tileset.WALL;
            }
        }

        generateAvatar();

        // create doors
        while (numberOfDoors != 0) {
            int randomW = RandomUtils.uniform(random, w);
            int randomH = RandomUtils.uniform(random, h);
            if (activeWorld[randomW][randomH] == Tileset.FLOOR) {
                activeWorld[randomW][randomH] = Tileset.UNLOCKED_DOOR;
                numberOfDoors--;
            }

        }


        if (render) {
            ter.renderFrame(activeWorld);
        }
    }

    public void render() {
        ter.renderFrame(activeWorld);
        render = true;
    }

    private class Avatar {
        private int column;
        private int row;
    }


    private void generateAvatar() {
        /** Creates an avatar at a random valid location */
        int avatarColumn = RandomUtils.uniform(random, w);
        int avatarRow = RandomUtils.uniform(random, h);
        while (activeWorld[avatarColumn][avatarRow] != Tileset.FLOOR) {
            avatarColumn = RandomUtils.uniform(random, w);
            avatarRow = RandomUtils.uniform(random, h);
        }
        activeWorld[avatarColumn][avatarRow] = Tileset.AVATAR;
        avatar = new Avatar();
        avatar.column = avatarColumn;
        avatar.row = avatarRow;
    }

    public void moveAvatarRight() {
        if (activeWorld[avatar.column + 1][avatar.row] == Tileset.FLOOR) {
            activeWorld[avatar.column][avatar.row] = Tileset.FLOOR;
            avatar.column += 1;
            activeWorld[avatar.column][avatar.row] = Tileset.AVATAR;
        }
    }
    public void moveAvatarLeft() {
        if (activeWorld[avatar.column-1][avatar.row] == Tileset.FLOOR) {
            activeWorld[avatar.column][avatar.row] = Tileset.FLOOR;
            avatar.column -= 1;
            activeWorld[avatar.column][avatar.row] = Tileset.AVATAR;
        }
    }
    public void moveAvatarUp() {
        if (activeWorld[avatar.column][avatar.row +1] == Tileset.FLOOR) {
            activeWorld[avatar.column][avatar.row] = Tileset.FLOOR;
            avatar.row += 1;
            activeWorld[avatar.column][avatar.row] = Tileset.AVATAR;
        } else if (activeWorld[avatar.column][avatar.row +1] == Tileset.UNLOCKED_DOOR) {
            int randomW = RandomUtils.uniform(random, w-20);
            int randomH = RandomUtils.uniform(random, h-15);
            for (int i = randomW; i < randomW + 20; i++) {
                for (int j = randomH; j < randomH + 15; j++) {
                    cachedWorld[randomW][randomH] = Tileset.WALL;
                }
            }
            for (int i = randomW+1; i < randomW + 19; i++) {
                for (int j = randomH+1; j < randomH + 14; j++) {
                    cachedWorld[randomW][randomH] = Tileset.WALL;
                }
            }
            ter.renderFrame(cachedWorld);
        }
    }
    public void moveAvatarDown() {
        if (activeWorld[avatar.column][avatar.row -1] == Tileset.FLOOR) {
            activeWorld[avatar.column][avatar.row] = Tileset.FLOOR;
            avatar.row -= 1;
            activeWorld[avatar.column][avatar.row] = Tileset.AVATAR;
        }
    }


    public String tileAt(int x, int y) {
        return activeWorld[x][y].description();
    }
    public int getWidth() {
        return w;
    }
    public int getHeight() {
        return h;
    }

    private int[] populate(int X, int Y, String D1, String D2, int D1L, int D2L) {
        int initialX = X;
        int initialY = Y;

        activeWorld[X][Y] = Tileset.FLOOR;

        switch (D1) {
            case "right" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y)[0] != 0) {
                        initialX = reBound(X, Y)[0];
                        X = reBound(X, Y)[0];
                    }
                    activeWorld[++X][Y] = Tileset.FLOOR;

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
                    activeWorld[--X][Y] = Tileset.FLOOR;

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
                    activeWorld[X][++Y] = Tileset.FLOOR;

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
                    activeWorld[X][--Y] = Tileset.FLOOR;

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
                    activeWorld[++X][Y] = Tileset.FLOOR;

                    System.out.println("D2 Right-- X position:" + X);
                }
                if (initialY < Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y >= initialY; y--) {
                            activeWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate1");
                }
                if (initialY > Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y <= initialY; y++) {
                            activeWorld[x][y] = Tileset.FLOOR;
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
                    activeWorld[--X][Y] = Tileset.FLOOR;

                    System.out.println("D2 Left-- X position:" + X);
                }
                if (initialY < Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y >= initialY; y--) {
                            activeWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate3");

                }
                if (initialY > Y && RandomUtils.uniform(random, 4) == 1) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y <= initialY; y++) {
                            activeWorld[x][y] = Tileset.FLOOR;
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
                    activeWorld[X][++Y] = Tileset.FLOOR;

                    System.out.println("D2 Up-- Y position:" + Y);
                }
                if (initialX < X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            activeWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate5");

                }
                if (initialX > X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            activeWorld[x][y] = Tileset.FLOOR;
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
                    activeWorld[X][--Y] = Tileset.FLOOR;

                    System.out.println("D2 Down-- Y position:" + Y);
                }
                if (initialX < X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            activeWorld[x][y] = Tileset.FLOOR;
                        }
                    }
                    System.out.println("real estate7");

                }
                if (initialX > X && RandomUtils.uniform(random, 4) == 1) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            activeWorld[x][y] = Tileset.FLOOR;
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
                activeWorld[X--][Y] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a high X");
            X = (w - 3 - 4);
        } else if (X < 4) {
            for (int i = 0; i < 4; i++) {
                activeWorld[X++][Y] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a low X");
            X = (3 + 4);
        }
        if (Y > h-4) {
            for (int i = 0; i < 4; i++) {
                activeWorld[X][Y--] = Tileset.FLOOR;
            }
            changed = true;
            System.out.println("rebounded a high Y");
            Y = (h - 3 - 4);
        } else if (Y < 4) {
            for (int i = 0; i < 4; i++) {
                activeWorld[X][++Y] = Tileset.FLOOR;
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
        return activeWorld;
    }
}


