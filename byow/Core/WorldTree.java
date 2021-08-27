package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.Path.setPaths;
import static byow.Core.Room.drawBox;
import static byow.Core.Utils.*;

public class WorldTree {
    /* Using a BSP to represent the world ,
    every partitioned container is the room,
    and the centre of partition is the door
    * */

    Container root;
    List<Container> leafNodes;
    static final int WIDTH = 80;
    static final int HEIGHT = 40;
    int seed;
    TETile[][] world;
    Random RANDOM;

    WorldTree(String seed){
        this.seed = validateSeed(seed);
        RANDOM = new Random(this.seed);
        world = new TETile[WIDTH][HEIGHT];
        root = new Container(0,0, WIDTH, HEIGHT);
        leafNodes = new ArrayList<>();
        leafNodes.add(root);
    }


     void makeSplit(int iter){
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
                int leftWidth = splitSize(c.w, c.h, true, RANDOM);
                if (leftWidth < 0){
                    continue;
                }
                int rightWidth = c.w - leftWidth;
                int leftHeight = c.h;
                int righttHeight= c.h;

                left = new Container(c.x, c.y, leftWidth, leftHeight);
                right = new Container(c.x + left.w, c.y, rightWidth , righttHeight);
            }else {
                int leftHeight = splitSize(c.h, c.w, false, RANDOM);
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
     public TETile[][] generateWorld(){
        makeSplit(5);
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                world[i][j] = Tileset.NOTHING;
            }
        }

        setPaths(root, world);

        for (Container container: leafNodes) {
            drawBox(container, world, RANDOM);
        }

        return world;
    }

    public Player generatePlayer(){
        // if selected room has no floors (might happen in some rare case)
        // then call the functino recursively, until we find better room
        int[] coordinates = new int[] {-1, -1};
        while(coordinates[0] == -1){
            coordinates = getValidCoordinates(getRandomRoom());
        }

        return new Player(coordinates[0], coordinates[1], Tileset.AVATAR);
    }

    private int[] getValidCoordinates(Container randomRoom){
        int playerX = -1;
        int playerY = -1;
        for (int i = randomRoom.x; i < randomRoom.w; i++){
            for (int j = randomRoom.y; j < randomRoom.h; j++) {
                if (world[i][j].description().equals("floor")){
                    return new int[]{i, j};
                }
            }
        }
        return new int []{playerX, playerY};
    }

    public ArrayList<Player> generateNPC(){
        ArrayList<Player> npcArray = new ArrayList<>();
        System.out.println(leafNodes);
        for (int i = 0; i < 10; i++) {
            int[] coordinates = getRandomCoordinates();
            Player npc = new Player(coordinates[0], coordinates[1], Tileset.MOUNTAIN);
            npcArray.add(npc);
        }
        return npcArray;
    }

    private int[] getRandomCoordinates(){
        int x = RANDOM.nextInt(WIDTH);
        int y = RANDOM.nextInt(HEIGHT);

        while (world[x][y] != Tileset.FLOOR){
            x = RANDOM.nextInt(WIDTH);
            y = RANDOM.nextInt(HEIGHT);
        }

        return new int[]{x, y};

    }

    private Container getRandomRoom(){
        /*gets valid room where our initial character can be*/
        Container randomRoom = leafNodes.get(RANDOM.nextInt(leafNodes.size()));
        return randomRoom;
    }

    public static void main(String[] args) {
        WorldTree tree = new WorldTree("N69420S");
        TETile[][] world = tree.generateWorld();

        TERenderer te = new TERenderer();
        te.initialize(WIDTH, HEIGHT);
        te.renderFrame(world);
    }
}


