package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class Interaction {
    WorldTree t;
    TETile[][] world;
    public Interaction() {
        TERenderer te = new TERenderer();
        te.initialize(WorldTree.WIDTH, WorldTree.HEIGHT, 2, 2);

        t = new WorldTree();
        WorldTree.RANDOM = new Random(69420);
        t.makeSplit(5);
        world = new TETile[WorldTree.WIDTH][WorldTree.HEIGHT];
        t.generateWorld(world);

    }

    void startGame() throws InterruptedException {
        while(true){
            welcomeScreen();
            // specify length of input expected
            String initCommand = listenForCommand(1);
            System.out.println(initCommand);
            if (initCommand == "n"){
                // new game
            }else if (initCommand == "L"){
                //load game
            }else if (initCommand.equals("q")){
                // exit
                for (int i = 5; i > 0; i--){
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setPenRadius();
                    StdDraw.text((double)WorldTree.WIDTH/2,(double) WorldTree.HEIGHT/4,"Game Exited , restarting in " + i + " seconds");
                    StdDraw.show();
                    Thread.sleep(1000);
                    StdDraw.clear();
                }
            }else if (initCommand.equals("r")){
                welcomeScreen();
            }
        }
    }

    void welcomeScreen(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double)WorldTree.WIDTH/2,30,"CS61B The Game");
        StdDraw.text((double)WorldTree.WIDTH/2,25,"New Game (N)");
        StdDraw.text((double)WorldTree.WIDTH/2,20,"Load Game (L)");
        StdDraw.text((double)WorldTree.WIDTH/2,15,"Quit (Q)");

        StdDraw.show();
    }


    public String listenForCommand(int n) {
        //TODO: Read n letters of player input
        StringBuilder s = new StringBuilder();
        int total = n;
        while (total!=0){
            if (StdDraw.hasNextKeyTyped()){
                s.append(StdDraw.nextKeyTyped());
                total-=1;
            }
        }
        return s.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        Interaction game = new Interaction();
        game.startGame();

    }
}
