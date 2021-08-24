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
    List<Container> cache;
    static final int WIDTH = 80;
    static final int HEIGHT = 40;

    WorldTree(Container root){
        this.root = root;
        this.cache = new ArrayList<>();
        cache.add(root);
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
        ArrayList<Container> newcache = new ArrayList<>();
        for (Container c : cache){
            System.out.println(c.w);
            if (c.w == 0 || c.h == 0){
                continue;
            }
            if (direction == 1){
                Container left = new Container(c.x, c.y, r.nextInt(c.w), c.h);
                Container right = new Container(c.x + left.w, c.y, c.w - left.w , c.h);
                c.lChild = left;
                c.rChild = right;
                newcache.add(left);
                newcache.add(right);
            }else {
                Container left = new Container(c.x, c.y, c.w , r.nextInt(c.h));
                Container right = new Container(c.x , c.y + left.h  , c.w, c.h - left.h );
                c.lChild = left;
                c.rChild = right;
                newcache.add(left);
                newcache.add(right);
            }
        }
        cache = newcache;
        makeSplit(iter - 1);
        System.out.println(cache);
    }

    // lets make and render rooms
    void renderWorld(){
        // do a preorder traversal of rooms
        // add door at center points

        // rendering parts
        int iter = 2;
        TERenderer te = new TERenderer();
        te.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        Stack<Container> roomStack = new Stack<>();
        roomStack.push(root);
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                world[i][j] = Tileset.NOTHING;
            }
        }
        while (!roomStack.isEmpty()){
            Container tmp = roomStack.pop();
            System.out.println(tmp.x + " " + tmp.y + " " + tmp.h + " " + tmp.w);
            drawBox(tmp, world);
            if (tmp.rChild != null){
                roomStack.push(tmp.rChild);
            }
            if (tmp.lChild != null){
                roomStack.push(tmp.lChild);
            }
        }

        te.renderFrame(world);
    }

    void drawBox(Container tmp, TETile[][] world){
        TETile t = Tileset.WALL;
        if (tmp.h == 0 || tmp.w == 0){
            return;
        }
        for (int i = tmp.x; i < tmp.x + tmp.w; i++){
            world[i][tmp.y] = t;
        }
        for (int i = tmp.y; i < tmp.y + tmp.h; i++){
            world[tmp.x][i] = t;
        }
        for (int i = tmp.x; i < tmp.x + tmp.w; i++){
            world[i][tmp.y + tmp.h - 1] = t;
        }
        for (int i = tmp.y; i < tmp.y + tmp.h; i++){
            world[tmp.x + tmp.w - 1][i] = t;
        }
    }

    void drawDividers(Container tmp, TETile[][] world){
        System.out.println(tmp.x + " " + tmp.y);

    }

    public static void main(String[] args) {
        Container root = new Container(0,0,WIDTH,HEIGHT);
        WorldTree tree = new WorldTree(root);
        tree.makeSplit(3);
        tree.renderWorld();
    }


}
