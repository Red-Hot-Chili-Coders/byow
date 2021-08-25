package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Utils.getOffset;
import static byow.Core.WorldTree.RANDOM;

public class Room {


    static void drawBox(Container container, TETile[][] world){
        // if container is empty
        if (container.h == 0 || container.w == 0){
            return;
        }

        TETile t = Tileset.WALL;
        int offsetX = getOffset(container.w);
        int offsetY = getOffset(container.h);

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
