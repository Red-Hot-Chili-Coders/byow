package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Locale;

public class Interaction {
    WorldTree t;
    TETile[][] world;
    TERenderer te;
    public Interaction() {
        te = new TERenderer();
        te.initialize(WorldTree.WIDTH, WorldTree.HEIGHT);
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

    void exit() throws InterruptedException {
        for (int i = 5; i > 0; i--) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setPenRadius();
            StdDraw.text((double) WorldTree.WIDTH / 2, (double) WorldTree.HEIGHT / 4, "Game Exited , restarting in " + i + " seconds");
            StdDraw.show();
            Thread.sleep(1000);
            StdDraw.clear();
        }
    }

    void seedScreen(String seed){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double)WorldTree.WIDTH/2,30,"Enter a random seed, (start with N and end with S!)");
        StdDraw.text((double)WorldTree.WIDTH/2,25,seed);
        StdDraw.show();
    }

    // lets the user enter the seed , returns the seed to the switch
    String enterSeed() throws InterruptedException {
        StringBuilder seed = new StringBuilder();
        String input = "";

        seedScreen("");
        while (!input.equals("S")){
            input = listenForCommand(1).toUpperCase();
            seed.append(input);
            seedScreen(seed.toString());
        }
        Thread.sleep(300);
        return seed.toString();
    }

    void startGame() throws InterruptedException {
        boolean inMenu = true;
        while(inMenu){
            welcomeScreen();
            // specify length of input expected
            String initCommand = listenForCommand(1);
            System.out.println(initCommand);
            switch (initCommand) {
                case "n":
                    WorldTree worldTree = new WorldTree(enterSeed());
                    TETile[][] world = worldTree.generateWorld();
                    te.renderFrame(world);

                    // todo: add the methods for player interactivity here
                    inMenu = false;
                    break;
                case "L":
                    //load game
                    break;
                case "q":
                    // exit
                    exit();
                    break;
                case "r":
                    welcomeScreen();
                    break;
            }
        }
    }

    // Listens from "n" inputs and returns the whole string
    public String listenForCommand(int n) {
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
}
