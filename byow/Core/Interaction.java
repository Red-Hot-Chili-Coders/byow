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
import static java.lang.Thread.sleep;

public class Interaction {
    Random RANDOM;
    WorldTree t;
    TETile[][] world;
    Player player;
    ArrayList<Player> npcArray;
    TERenderer te;
    int numberOfCollectibles = 7;
    int numberOfCollectiblesLeft = numberOfCollectibles;
    long nano;
    boolean gameOver;
    boolean won;

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
        sleep(300);
        return seed.toString();
    }

    void restartingScreen(String msg) throws InterruptedException {
        for (int i = 5; i > 0; i--) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setPenRadius();
            StdDraw.text((double) WorldTree.WIDTH / 2, (double) WorldTree.HEIGHT / 4, msg + " , restarting in " + i + " seconds");
            StdDraw.show();
            sleep(1000);
            StdDraw.clear();
        }
    }

    void startGame() throws InterruptedException {
        boolean inMenu = true;
        while (inMenu) {
            welcomeScreen();
            String initCommand = listenForCommand();
            switch (initCommand) {
                case "n":
                    numberOfCollectiblesLeft = numberOfCollectibles;
                    renderWorld();
                    break;
                case "L":
                    //load game
                    break;
                case "q":
                    // exit
                    restartingScreen("Game exited");
                    break;
                case "r":
                    welcomeScreen();
                    break;
            }
        }
    }

    void renderWorld() throws InterruptedException {
        gameOver = false;

        WorldTree worldTree = new WorldTree("n1234s");
        // WorldTree worldTree = new WorldTree(enterSeed());

        world = worldTree.generateWorld();

        player = worldTree.generatePlayer();
        updatePlayerPos();

        npcArray = worldTree.generateNPC();
        updateNPCPos();

        worldTree.generateCollectibles(numberOfCollectibles);
        te.renderFrame(world);

        while (!gameOver) {
            // HUD
            showMouseInfo(world);
            showCollectibleStats(world, numberOfCollectiblesLeft, numberOfCollectibles);

            // move npcs
            moveNPCs();

            // player movement+
            String move = listenForCommand();
            validateAndMakeMove(move, player, world);
            updatePlayerPos();

            te.renderFrame(world);
        }

        sleep(500);
        if (won){
            restartingScreen("you won !");
        }else{
            restartingScreen("You were caught !");
        }
    }

    // renders player on map
    // player position is updated within validateAndMakeMove
    private void updatePlayerPos() {
        // checks if walking into a npc : if yes game over with players grave being shown
        TETile onTile = world[player.x][player.y];
        if (onTile == Tileset.MOUNTAIN){
            gameOver = true;
            world[player.x][player.y] = Tileset.SAND;
            won = false;
            return;
        }else if (onTile == Tileset.FLOWER){
            numberOfCollectiblesLeft--;
            if (numberOfCollectiblesLeft == 0){
                gameOver = true;
                won = true;
            }
        }

        world[player.x][player.y] = Tileset.AVATAR;
    }

    private void updateNPCPos(){
        for (Player npc : npcArray){
            if (world[npc.x][npc.y] == player.tile){
                gameOver = true;
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

