package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Path {

    static TETile pathTile = Tileset.FLOOR;
    static TETile wallTile = Tileset.WALL;
    static final int HEIGHT = WorldTree.HEIGHT;
    static final int WIDTH = WorldTree.WIDTH;

    static void setPaths(Container base, TETile[][] world){
        if (base.rChild == null || base.lChild == null){
            return;
        }
        connectCenter(base.rChild, base.lChild, world);
        setPaths(base.lChild, world);
        setPaths(base.rChild, world);
    }

    static void connectCenter(Container r1, Container r2, TETile[][] world){
        int r1x = r1.center.x;
        int r1y = r1.center.y;
        int r2x = r2.center.x;
        int r2y = r2.center.y;

        if (r1x == r2x){
            joinVertical(r1x, r1y, r2y, world);
        }

        if (r1y == r2y){
            joinHorizontal(r1y, r1x, r2x, world);
        }

    }

    static void joinHorizontal(int pivot, int p1, int p2, TETile[][] world){
        int start = 0;
        int end = 0;
        if (p1 > p2) {
            start = p2;
            end = p1;
        }else{
            start = p1;
            end = p2;
        }

        if (pivot == HEIGHT - 1){
            pivot-=1;
        }else if (pivot == 0){
            pivot+=1;
        }

        for (int i = start; i <= end; i++){
            if (!world[i][pivot + 1].description().equals("floor")) {
                world[i][pivot + 1] = wallTile;
            }
            if (!world[i][pivot].description().equals("floor")){
                if (i==start || i==end){
                    world[i][pivot] = wallTile;
                }else{
                    world[i][pivot] = pathTile;
                }
            }
            if (!world[i][pivot - 1].description().equals("floor")){
                world[i][pivot - 1] = wallTile;
            }
        }
    }

    static void joinVertical(int pivot, int p1, int p2, TETile[][] world){
        int start = 0;
        int end = 0;
        if (p1 > p2) {
            start = p2;
            end = p1;
        }else{
            start = p1;
            end = p2;
        }

        if (pivot == WIDTH - 1){
            pivot-=1;
        }else if (pivot == 0){
            pivot+=1;
        }

        for (int i = start; i <= end; i++){
            if (!world[pivot + 1][i].description().equals("floor")) {
                world[pivot + 1][i] = wallTile;
            }
            if (!world[pivot][i].description().equals("floor")){
                if (i==start || i==end){
                    world[pivot][i] = wallTile;
                }else{
                    world[pivot][i] = pathTile;
                }
            }
            if (!world[pivot - 1][i].description().equals("floor")){
                world[pivot - 1][i] = wallTile;
            }
        }
    }
}
