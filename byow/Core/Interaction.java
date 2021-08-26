package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Interaction {
    WorldTree t;
    TETile[][] world;
    Player player;
    TERenderer te;

    public Interaction() {
        te = new TERenderer();
        te.initialize(WorldTree.WIDTH, WorldTree.HEIGHT);
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
            input = listenForCommand(1).toUpperCase();
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
            // specify length of input expected
            String initCommand = listenForCommand(1);
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
        // renders game initially , gameOver boolean added for future
        boolean gameOver = false;

        // todo: change this !, seed is hardcoded for convience
        WorldTree worldTree = new WorldTree("n123s");
        // WorldTree worldTree = new WorldTree(enterSeed());
        world = worldTree.generateWorld();
        player = worldTree.generatePlayer();

        updatePlayerPos();
        te.renderFrame(world);

        //Continuously listens for movement commands and
        //validatesAndUpdates player positon
        while (!gameOver) {
            String move = listenForCommand(1);
            System.out.println(move);
            validateAndMakeMove(move);
            te.renderFrame(world);
        }

    }

    // renders player on map
    // player position is updated within validateAndMakeMove
    private void updatePlayerPos() {
        world[player.x][player.y] = player.tile;
    }

    // checks if move is valid using validate helper
    // player movement process
    // => reverse existing tile to floor
    // => render player again (based on its new position)
    private void validateAndMakeMove(String direction) {
        if (validateMove(direction)) {
            switch (direction) {
                case "w":
                    reversePlayerTile();
                    player.y += 1;
                    break;
                case "a":
                    reversePlayerTile();
                    player.x -= 1;
                    break;
                case "s":
                    reversePlayerTile();
                    player.y -= 1;
                    break;
                case "d":
                    reversePlayerTile();
                    player.x += 1;
                    break;
            }
            updatePlayerPos();
        }
    }

    // checks if move is possible , that is a tile exists
    // at the intended position
    // statement 1 : validates if move goes out of map or not
    // statement 2 : if intended move will land on a floor
    private boolean validateMove (String direction){
        return switch (direction) {
            case "w" -> player.y + 1 < WorldTree.HEIGHT &&
                    world[player.x][player.y + 1].description().equals("floor");
            case "a" -> player.x - 1 >= 0 &&
                    world[player.x - 1][player.y].description().equals("floor");
            case "s" -> player.y - 1 >= 0 &&
                    world[player.x][player.y - 1].description().equals("floor");
            case "d" -> player.x + 1 < WorldTree.WIDTH &&
                    world[player.x + 1][player.y].description().equals("floor");
            default -> false;
        };
    }

    // reversed player tile to floor tile
    // intermediate function in making move
    private void reversePlayerTile(){
        world[player.x][player.y] = Tileset.FLOOR;
    }

    // Listens from "n" inputs and returns the whole string
    public String listenForCommand ( int n){
        StringBuilder s = new StringBuilder();
        int total = n;
        while (total != 0) {
            if (StdDraw.hasNextKeyTyped()) {
                s.append(StdDraw.nextKeyTyped());
                total -= 1;
            }
        }
        return s.toString();
    }
}

