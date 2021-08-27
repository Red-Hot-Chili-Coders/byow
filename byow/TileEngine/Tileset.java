package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static final Color floorColor = new Color(15, 15, 15);
    public static final TETile AVATAR = new TETile('▲', new Color(86, 119, 252), floorColor, "you", "./pngs/avatar.png");
    public static final TETile WALL = new TETile(' ', new Color(121, 0, 201), new Color(121, 0, 201), "wall");
    public static final TETile FLOOR = new TETile(' ', floorColor, floorColor, "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, floorColor, "flower");
    public static final TETile SAND = new TETile('▲', Color.ORANGE, floorColor, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.white, floorColor, "mountain");
}


