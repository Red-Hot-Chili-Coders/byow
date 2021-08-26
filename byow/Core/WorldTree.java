package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.Path.setPaths;
import static byow.Core.Room.drawBox;
import static byow.Core.Utils.splitSize;

public class WorldTree {
    /* Using a BSP to represent the world ,
    every partitioned container is the room,
    and the centre of partition is the door
    * */

    Container root;
    List<Container> leafNodes;
    static final int WIDTH = 40;
    static final int HEIGHT = 40;
    static Random RANDOM;
    static final boolean displayPartitions = false;
    static TETile pathTile = Tileset.FLOOR;
    static TETile wallTile = Tileset.WALL;

    WorldTree(){
        root = new Container(0,0, WIDTH, HEIGHT);
        leafNodes = new ArrayList<>();
        leafNodes.add(root);
    }


     void makeSplit(int iter){
        long direction = RANDOM.nextInt();

        // default split : vertical
        if (iter == 0){
            return;
        }

        ArrayList<Container> newLeafNodes = new ArrayList<>();
        for (Container c : leafNodes){
            if (c.w == 0 || c.h == 0){
                continue;
            }

            Container left;
            Container right;

            // if width is bigger than the height, split vertically
            // else horizontally

            if ((float) c.w/c.h > 1){
                int leftWidth = splitSize(c.w, c.h, true);
                if (leftWidth < 0){
                    continue;
                }
                int rightWidth = c.w - leftWidth;
                int leftHeight = c.h;
                int righttHeight= c.h;

                left = new Container(c.x, c.y, leftWidth, leftHeight);
                right = new Container(c.x + left.w, c.y, rightWidth , righttHeight);
            }else {
                int leftHeight = splitSize(c.h, c.w, false);
                if (leftHeight < 0){
                    continue;
                }
                int rightHeight = c.h - leftHeight;
                int leftWidth = c.w;
                int rightWidth = c.w;

                left = new Container(c.x, c.y, leftWidth, leftHeight);
                right = new Container(c.x , c.y + left.h, rightWidth, rightHeight);
            }
            c.lChild = left;
            c.rChild = right;
            newLeafNodes.add(left);
            newLeafNodes.add(right);
        }
        leafNodes = newLeafNodes;
        makeSplit(iter - 1);
    }

    // Draw the leaf nodes (rooms)
     void generateWorld(TETile[][] world){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                world[i][j] = Tileset.MOUNTAIN;
            }
        }

        setPaths(root, world);

        for (Container container: leafNodes) {
            drawBox(container, world);
        }

    }



    public static void main(String[] args) {
        TERenderer te = new TERenderer();
        te.initialize(WIDTH, HEIGHT);

        WorldTree tree = new WorldTree();
        RANDOM = new Random(69420);
        // splitting and forming the tree
        tree.makeSplit(5);

        // making the world array
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        tree.generateWorld(world);

        // rendering the world array
        te.renderFrame(world);
    }
}


