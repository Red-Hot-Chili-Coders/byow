package byow.Core;

public class Container {
    int x;
    int y;
    int w;
    int h;
    Point center;
    Container lChild;
    Container rChild;

    Container(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        center = new Point(
            this.x+(this.w/2),
            this.y+(this.h/2)
        );
        rChild = null;
        lChild = null;
    }

    class Point {
        int x;
        int y;
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

}
