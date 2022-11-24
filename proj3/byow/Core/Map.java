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
    static final int seed = 7; //just a random seed for testin'
    static final int w = 70; //j a decent width size
    static final int h = 35; //j a decent height size
    static final String[] directions = new String[]{"up", "down", "left", "right"}; //directions

    static final Random random = new Random(seed);

    static final TETile[][] world = new TETile[w][h];
    private Avatar avatar;

    public Map() {

        TERenderer ter = new TERenderer();
        ter.initialize(w, h);

        //make evryting outside initially
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < 35; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //find us a random tile
        int randomX = RandomUtils.uniform(random, w);
        int randomY = RandomUtils.uniform(random, h);

        //pick a random direction
        String randomD1 = directions[RandomUtils.uniform(random, 4)];
        String randomD2 = directions[RandomUtils.uniform(random, 4)];

        //now choose random lengths lol
        int randomD1L = RandomUtils.uniform(random, w/10);
        int randomD2L = RandomUtils.uniform(random, h/10);

        System.out.println("X: " + randomX + " Y: " + randomY + " D1: " + randomD1 + " D2: " + randomD2 + " D1L: " + randomD1L + " D2L: " + randomD2L);

        int[] firstpop = populate(randomX, randomY, randomD1, randomD2, randomD1L, randomD2L);

        int nowX = firstpop[0];

        System.out.println(nowX);

        int nowY = firstpop[1];

        System.out.println(nowY);

        for(int i = 0; i < 350; i++) {
            String nowD1 = directions[RandomUtils.uniform(random, 4)];
            String nowD2 = directions[RandomUtils.uniform(random, 4)];

            int nowD1L = RandomUtils.uniform(random, w/10);
            int nowD2L = RandomUtils.uniform(random, h/10);

            System.out.println("X: " + nowX + " Y: " + nowY + " D1: " + nowD1 + " D2: " + nowD2 + " D1L: " + nowD1L + " D2L: " + nowD2L);

            int[] secpop = populate(nowX, nowY, nowD1, nowD2, nowD1L, nowD2L);

            nowX = secpop[0];
            System.out.println(nowX);

            nowY = secpop[1];
            System.out.println(nowY);
        }

        /**for (int x = 1; x < w - 1; x += 1) {
         if (world[x][0] == Tileset.INSIDE) {
         world[--x][0] = Tileset.WALL;
         world[++x][0] = Tileset.WALL;
         world[++x][0] = Tileset.WALL;
         }
         }
         for (int x = 1; x < w - 1; x += 1) {
         if (world[x][h-1] == Tileset.INSIDE) {
         world[--x][0] = Tileset.WALL;
         world[++x][0] = Tileset.WALL;
         world[++x][0] = Tileset.WALL;
         }
         }
         for (int y = 1; y < h-1; y++) {
         if (world[0][y] == Tileset.INSIDE) {
         world[--y][0] = Tileset.WALL;
         world[++y][0] = Tileset.WALL;
         world[++y][0] = Tileset.WALL;
         }
         }
         for (int y = 1; y < h-1; y++) {
         if (world[w-1][y] == Tileset.INSIDE) {
         world[--y][0] = Tileset.WALL;
         world[++y][0] = Tileset.WALL;
         world[++y][0] = Tileset.WALL;
         }
         }**/

        for (int x = 1; x < w-1; x++) {
            for (int y = 1; y < h-1; y++) {
                if(world[x][y] == Tileset.OUTSIDE) {
                    if(world[x+1][y] == Tileset.INSIDE || world[x-1][y] == Tileset.INSIDE
                            || world[x][y+1] == Tileset.INSIDE || world[x][y-1] == Tileset.INSIDE
                            || world[x+1][y+1] == Tileset.INSIDE || world[x-1][y-1] == Tileset.INSIDE
                            || world[x-1][y+1] == Tileset.INSIDE || world[x+1][y-1] == Tileset.INSIDE) {
                        world[x][y] = Tileset.WALL;
                        Tileset.
                    }
                }
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(world[x][1] == Tileset.INSIDE || world[x+1][1] == Tileset.INSIDE || world[x-1][1] == Tileset.INSIDE) {
                world[x][0] = Tileset.WALL;
            }
        }

        for (int x = 1; x < w - 1; x++) {
            if(world[x][h-2] == Tileset.INSIDE || world[x+1][h-2] == Tileset.INSIDE || world[x-1][h-2] == Tileset.INSIDE) {
                world[x][h-1] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(world[1][y] == Tileset.INSIDE || world[1][y+1] == Tileset.INSIDE || world[1][y-1] == Tileset.INSIDE) {
                world[0][y] = Tileset.WALL;
            }
        }

        for (int y = 1; y < h - 1; y++) {
            if(world[w-2][y] == Tileset.INSIDE || world[w-2][y+1] == Tileset.INSIDE || world[w-2][y-1] == Tileset.INSIDE) {
                world[w-1][y] = Tileset.WALL;
            }
        }

        generateAvatar();

        ter.renderFrame(world);
    }

    private class Avatar {
        private int X;
        private int Y;
    }


    //  Newly added work by Michael
    private void generateAvatar() {
        /** Creates an avatar at a random valid location */
        int avatarX = RandomUtils.uniform(random, w);
        int avatarY = RandomUtils.uniform(random, h);
        while (world[avatarX][avatarY] != Tileset.FLOOR) {
            avatarX = RandomUtils.uniform(random, w);
            avatarY = RandomUtils.uniform(random, h);
        }
        world[avatarX][avatarY] = Tileset.AVATAR;
        avatar = new Avatar();
        avatar.X = avatarX;
        avatar.Y = avatarY;
    }

    public void moveAvatarRight() {
        if (world[avatar.X+1][avatar.Y] == Tileset.FLOOR) {
            world[avatar.X][avatar.Y] = Tileset.FLOOR;
            avatar.X += 1;
            world[avatar.X][avatar.Y] = Tileset.AVATAR;
        }
    }
    public void moveAvatarLeft() {
        if (world[avatar.X-1][avatar.Y] == Tileset.FLOOR) {
            world[avatar.X][avatar.Y] = Tileset.FLOOR;
            avatar.X -= 1;
            world[avatar.X][avatar.Y] = Tileset.AVATAR;
        }
    }
    public void moveAvatarUp() {
        if (world[avatar.X][avatar.Y+1] == Tileset.FLOOR) {
            world[avatar.X][avatar.Y] = Tileset.FLOOR;
            avatar.Y += 1;
            world[avatar.X][avatar.Y] = Tileset.AVATAR;
        }
    }
    public void moveAvatarDown() {
        if (world[avatar.X][avatar.Y-1] == Tileset.FLOOR) {
            world[avatar.X][avatar.Y] = Tileset.FLOOR;
            avatar.Y -= 1;
            world[avatar.X][avatar.Y] = Tileset.AVATAR;
        }
    }
    // End of newly added work from Michael.

    private static int[] populate(int X, int Y, String D1, String D2, int D1L, int D2L) {
        int initialX = X;
        int initialY = Y;

        world[X][Y] = Tileset.INSIDE;

        switch (D1) {
            case "right" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y) != 0) {
                        initialX = reBound(X, Y);
                        X = reBound(X, Y);
                    }
                    world[++X][Y] = Tileset.INSIDE;

                    System.out.println("D1 Right-- X position:" + X);
                }
                break;
            }
            case "left" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y) != 0) {
                        initialX = reBound(X, Y);
                        X = reBound(X, Y);
                    }
                    world[--X][Y] = Tileset.INSIDE;

                    System.out.println("D1 Left-- X position:" + X);
                }
                break;
            }
            case "up" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y) != 0) {
                        initialY = reBound(X, Y);
                        Y = reBound(X, Y);
                    }
                    world[X][++Y] = Tileset.INSIDE;

                    System.out.println("D1 Up-- Y position:" + Y);
                }
                break;
            }
            case "down" : {
                for (int i = 0; i < D1L; i++) {
                    if (reBound(X, Y) != 0) {
                        initialY = reBound(X, Y);
                        Y = reBound(X, Y);
                    }
                    world[X][--Y] = Tileset.INSIDE;

                    System.out.println("D1 Down-- Y position:" + Y);
                }
                break;
            }
        }

        switch(D2) {
            case "right" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y) != 0) {
                        X = reBound(X, Y);
                    }
                    world[++X][Y] = Tileset.INSIDE;

                    System.out.println("D2 Right-- X position:" + X);
                }
                if (initialY < Y) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y >= initialY; y--) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                if (initialY > Y) {
                    for (int x = X; x >= initialX; x--) {
                        for (int y = Y; y <= initialY; y++) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                break;
            }

            case "left" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y) != 0) {
                        X = reBound(X, Y);
                    }
                    world[--X][Y] = Tileset.INSIDE;

                    System.out.println("D2 Left-- X position:" + X);
                }
                if (initialY < Y) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y >= initialY; y--) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                if (initialY > Y) {
                    for (int x = X; x <= initialX; x++) {
                        for (int y = Y; y <= initialY; y++) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                break;
            }

            case "up" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y) != 0) {
                        Y = reBound(X, Y);
                    }
                    world[X][++Y] = Tileset.INSIDE;

                    System.out.println("D2 Up-- Y position:" + Y);
                }
                if (initialX < X) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                if (initialX > X) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                break;
            }
            case "down" : {
                for (int i = 0; i < D2L; i++) {
                    if (reBound(X, Y) != 0) {
                        Y = reBound(X, Y);
                    }
                    world[X][--Y] = Tileset.INSIDE;

                    System.out.println("D2 Down-- Y position:" + Y);
                }
                if (initialX < X) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x <= initialX; x++) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
                if (initialX > X) {
                    for (int y = Y; y >= initialY; y--) {
                        for (int x = X; x >= initialX; x--) {
                            world[x][y] = Tileset.INSIDE;
                        }
                    }
                }
            }
            break;
        }

        return new int[] {X,Y};
    }

    private static int reBound(int X, int Y) {
        if(X > w-2) {
            for (int i = 0; i < 11; i++) {
                world[X--][Y] = Tileset.INSIDE;
            }
            System.out.println("rebounded a high X");
            return w - 2 - 11;
        } else if (X < 2) {
            for (int i = 0; i < 11; i++) {
                world[X++][Y] = Tileset.INSIDE;
            }
            System.out.println("rebounded a low X");
            return 2 + 4;
        } else if (Y > h-2) {
            for (int i = 0; i < 11; i++) {
                world[X][Y--] = Tileset.INSIDE;
            }
            System.out.println("rebounded a high Y");
            return h - 2 - 11;
        } else if (Y < 2) {
            for (int i = 0; i < 11; i++) {
                world[X][++Y] = Tileset.INSIDE;
            }

            System.out.println("rebounded a low Y");
            return 2 + 4;
        }
        return 0;
    }
}


