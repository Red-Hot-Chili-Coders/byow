package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldTree {
    /* Using a BSP to represent the world ,
    every partitioned container is the room,
    and the centre of partition is the door
    * */

    Container root;
    List<Container> leafNodes;
    static final int WIDTH = 100;
    static final int HEIGHT = 50;
    static Random RANDOM;
    static final boolean displayPartitions = false;

    WorldTree(Container r){
        root = r;
        leafNodes = new ArrayList<>();
        leafNodes.add(root);
    }

    /**
     * Calculates proper split size for the left subchild
     * if it can find the size according to the raitos , it returns -1
     * (it happens when iterations are over 10)
     * @param leftVariable - leftchild's height or width, which needs to be randomly generated
     * @param leftConstant - height or width , whichever is constant
     * @param isVertical - is split is vertical
     * @return split size for the left child , -1 if not possible (unfit according to the ratio)
     */
    private static int splitSize(int leftVariable, int leftConstant, boolean isVertical) {

        float ratio1;
        float ratio2;

        int iterations = 0;
        while (iterations <= 10){
            int leftSize = RANDOM.nextInt(leftVariable);
            int rightSize = leftVariable - leftSize;

            if (isVertical){
                ratio1 = (float) leftSize/leftConstant;
                ratio2 = (float) rightSize/leftConstant;
            }else{
                ratio1 = (float) leftConstant/leftSize;
                ratio2 = (float) leftConstant/rightSize;
            }

            if (ratio1 > 0.45 && ratio2 > 0.45){
                return leftSize;
            }
            iterations++;
        }

        return -1;
    }

    private void makeSplit(int iter){
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

            // if width is bigger than the height, split vertically
            if ((float) c.w/c.h > 1){

                int leftWidth = splitSize(c.w, c.h, true);
                if (leftWidth < 0){
                    continue;
                }
                int rightWidth = c.w - leftWidth;
                int leftHeight = c.h;
                int righttHeight= c.h;

                Container left = new Container(c.x, c.y, leftWidth, leftHeight);
                Container right = new Container(c.x + left.w, c.y, rightWidth , righttHeight);
                c.lChild = left;
                c.rChild = right;
                newLeafNodes.add(left);
                newLeafNodes.add(right);

                // if width is smaller than the height, split horizontally
            }else {
                int leftWidth = c.w;
                int rightWidth = c.w;
                int leftHeight = splitSize(c.h, c.w, false);
                if (leftHeight < 0){
                    continue;
                }
                int rightHeight = c.h - leftHeight;

                Container left = new Container(c.x, c.y, leftWidth, leftHeight);
                Container right = new Container(c.x , c.y + left.h, rightWidth, rightHeight);
                c.lChild = left;
                c.rChild = right;
                newLeafNodes.add(left);
                newLeafNodes.add(right);
            }
        }
        leafNodes = newLeafNodes;
        makeSplit(iter - 1);
    }

    // Draw the leaf nodes (rooms)
    private void generateWorld(TETile[][] world){

        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                world[i][j] = Tileset.NOTHING;
            }
        }

        for (Container container: leafNodes) {
            drawBox(container, world);
        }
    }

    private static int getOffset(int size){
        if (size/3 != 0){
            return RANDOM.nextInt(size/3);
        }else{
            return 0;
        }
    }

    void drawBox(Container container, TETile[][] world){
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
                world[j][i] = Tileset.WALL;
            }
        }

        // change the boolean displayPartitions to true
        // if you want to see the partitions
        if (displayPartitions){
            world[container.center.x][container.center.y] = Tileset.LOCKED_DOOR;
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

    }

    void setRandom(long seed){
        RANDOM = new Random(seed);
    }

    public static void main(String[] args) {
        TERenderer te = new TERenderer();
        te.initialize(WIDTH, HEIGHT);

        Container root = new Container(0,0,WIDTH,HEIGHT);
        WorldTree tree = new WorldTree(root);
        tree.setRandom(69420);
        // splitting and forming the tree
        tree.makeSplit(6);

        // making the world array
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        tree.generateWorld(world);

        // rendering the world array
        te.renderFrame(world);
    }
}


// make splittree
// make rooms insteed of graph
// change draw box