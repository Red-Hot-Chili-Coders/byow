package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class WorldTree {
    /* Using a BSP to represent the world ,
    every partitioned container is the room,
    and the centre of partition is the door
    * */

    Container root;
    // null node cache
    List<Container> leafNodes;
    static final int WIDTH = 80;
    static final int HEIGHT = 40;

    WorldTree(Container r){
        root = r;
        leafNodes = new ArrayList<>();
        leafNodes.add(root);
    }

    // direction :  1 for vertical , 2 for horizontal
    // iter : split iterations
    // cache null nodes and create splits directly
    void makeSplit(int iter){
        Random r = new Random((long) (Math.sqrt(iter)*1000));
        int direction = r.nextInt(5);

        // default split : vertical
        if (iter == 0){
            return;
        }

        ArrayList<Container> newLeafNodes = new ArrayList<>();
        for (Container c : leafNodes){
            System.out.println(c.w);
            if (c.w == 0 || c.h == 0){
                continue;
            }

            if (direction == 1){
                Container left = new Container(c.x, c.y, r.nextInt(c.w), c.h);
                Container right = new Container(c.x + left.w, c.y, c.w - left.w , c.h);
                c.lChild = left;
                c.rChild = right;
                newLeafNodes.add(left);
                newLeafNodes.add(right);
            }else {
                Container left = new Container(c.x, c.y, c.w , r.nextInt(c.h));
                Container right = new Container(c.x , c.y + left.h  , c.w, c.h - left.h );
                c.lChild = left;
                c.rChild = right;
                newLeafNodes.add(left);
                newLeafNodes.add(right);
            }
        }
        leafNodes = newLeafNodes;
        makeSplit(iter - 1);
        System.out.println(leafNodes);
    }

    // Draw the leaf nodes (rooms)
    void generateWorld(TETile[][] world){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                world[i][j] = Tileset.NOTHING;
            }
        }

        for (Container container: leafNodes) {
            drawBox(container, world);
        }
    }

    void drawBox(Container container, TETile[][] world){
        TETile t = Tileset.WALL;

        // if container is empty
        if (container.h == 0 || container.w == 0){
            return;
        }

        // drawing both horizontal lines of the box
        for (int i = container.x; i < container.x + container.w; i++){
            world[i][container.y] = t;

            int upperHorizontalLineY = container.y + container.h - 1;
            world[i][upperHorizontalLineY] = t;
        }

        // drawing both vertical lines of the box
        for (int i = container.y; i < container.y + container.h; i++){
            world[container.x][i] = t;

            int rightVerticalLineX = container.x + container.w - 1;
            world[rightVerticalLineX][i] = t;
        }
    }

    void drawDividers(Container tmp, TETile[][] world){
        System.out.println(tmp.x + " " + tmp.y);

    }

    public static void main(String[] args) {
        TERenderer te = new TERenderer();
        te.initialize(WIDTH, HEIGHT);

        Container root = new Container(0,0,WIDTH,HEIGHT);
        WorldTree tree = new WorldTree(root);

        // splitting and forming the tree
        tree.makeSplit(3);

        // making the world array
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        tree.generateWorld(world);

        // rendering the world array
        te.renderFrame(world);
    }
}
