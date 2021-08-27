package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Utils.*;

public class Interaction {
    Random RANDOM;
    WorldTree t;
    TETile[][] world;
    Player player;
    ArrayList<Player> npcArray;
    TERenderer te;
    int mouseX;
    int mouseY;

    long nano;
    boolean gameOver;

    public Interaction() {
        te = new TERenderer();
        te.initialize(WorldTree.WIDTH, WorldTree.HEIGHT+4);
        RANDOM = new Random();
    }

    void welcomeScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double) WorldTree.WIDTH / 2, 30, "CS61B The Game");
        StdDraw.text((double) WorldTree.WIDTH / 2, 25, "New Game (N)");
        StdDraw.text((double) WorldTree.WIDTH / 2, 20, "Load Game (L)");
        StdDraw.text((double) WorldTree.WIDTH / 2, 15, "Quit (Q)");

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

    void seedScreen(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double) WorldTree.WIDTH / 2, 30, "Enter a random seed, (start with N and end with S!)");
        StdDraw.text((double) WorldTree.WIDTH / 2, 25, seed);
        StdDraw.show();
    }

    // lets the user enter the seed , returns the seed to the switch
    String enterSeed() throws InterruptedException {
        StringBuilder seed = new StringBuilder();
        String input = "";

        seedScreen("");
        while (!input.equals("S")) {
            input = listenForCommand().toUpperCase();
            seed.append(input);
            seedScreen(seed.toString());
        }
        Thread.sleep(300);
        return seed.toString();
    }

    void startGame() throws InterruptedException {
        boolean inMenu = true;
        while (inMenu) {
            welcomeScreen();
            String initCommand = listenForCommand();
            System.out.println(initCommand);
            switch (initCommand) {
                case "n":
                    renderWorld();
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

    void renderWorld() throws InterruptedException {
        gameOver = false;

        WorldTree worldTree = new WorldTree("n123s");
        // WorldTree worldTree = new WorldTree(enterSeed());

        world = worldTree.generateWorld();

        player = worldTree.generatePlayer();
        updatePlayerPos();

        npcArray = worldTree.generateNPC();
        updateNPCPos();

        worldTree.generateCollectibles();
        te.renderFrame(world);

        while (!gameOver) {
            // check if user has clicked any tile for info
            showMouseInfo(world);

            // move npcs
            moveNPCs();

            // player movement+
            String move = listenForCommand();
            validateAndMakeMove(move, player, world);
            updatePlayerPos();

            te.renderFrame(world);
        }
    }

    // renders player on map
    // player position is updated within validateAndMakeMove
    private void updatePlayerPos() {
        // checks if walking into a npc : if yes game over with players grave being shown
        if (world[player.x][player.y] == Tileset.MOUNTAIN){
            gameOver = true;
            world[player.x][player.y] = Tileset.SAND;
        }else {
            world[player.x][player.y] = Tileset.AVATAR;
        }
    }

    private void updateNPCPos(){
        for (Player npc : npcArray){
            System.out.println(npc.x + " " + npc.y);
            if (world[npc.x][npc.y] == player.tile){
                continue;
            }
            world[npc.x][npc.y] = npc.tile;
        }
    }

    public void moveNPCs(){
        LocalDateTime date = LocalDateTime.now();
        long currentTime = date.toLocalTime().toNanoOfDay();
        if (currentTime - nano < 500000000){
            return;
        }
        nano = currentTime;

        String[] moves = new String[]{"w", "a", "s", "d"};
        for (Player npc : npcArray){
            String move = moves[RANDOM.nextInt(4)];
            validateAndMakeMove(move, npc, world);
        }
        updateNPCPos();
    }

}

