package byow.Core;

import byow.TileEngine.TETile;

/*
    - int x
    - int y
    - character tile
    - further possible additions :
        - health
        - powers
        - . . .
* */

public class Player {
    int x;
    int y;
    TETile tile;

    Player(int x, int y, TETile tile){
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

}
