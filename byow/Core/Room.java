package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static byow.Core.Utils.getOffset;

public class Room {


    static void drawBox(Container container, TETile[][] world, Random RANDOM){
        // if container is empty
        if (container.h == 0 || container.w == 0){
            return;
        }

        int offsetX = getOffset(container.w, RANDOM);
        int offsetY = getOffset(container.h, RANDOM);

        int boxX = container.x + offsetX;
        int boxY = container.y + offsetY;
        int boxW = container.w - (boxX - container.x);
        int boxH = container.h - (boxY - container.y);

        // drawing the box/room , starting from the bottom to the top
        for (int i = boxY; i < boxY+boxH - 1; i++){
            for (int j = boxX; j < boxX+boxW - 1; j++) {
                if ((i == boxY || i == boxY + boxH - 2 || j == boxX || j == boxX + boxW - 2) && !world[j][i].description().equals("floor")){
                    world[j][i] = Tileset.WALL;
                }else{
                    world[j][i] = Tileset.FLOOR;
                }
            }
        }



    }
}
