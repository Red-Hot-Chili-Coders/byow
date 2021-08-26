package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Interaction {
    TETile[][] world;
    Player player;
    TERenderer te;
    int mouseX;
    int mouseY;

    public Interaction() {
        te = new TERenderer();
        te.initialize(WorldTree.WIDTH, WorldTree.HEIGHT+4);
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
        boolean gameOver = false;

        WorldTree worldTree = new WorldTree("n123s");
        // WorldTree worldTree = new WorldTree(enterSeed());

        world = worldTree.generateWorld();
        player = worldTree.generatePlayer();

        updatePlayerPos();
        te.renderFrame(world);

        while (!gameOver) {
            showMouseInfo();
            String move = listenForCommand();
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

    public void showMouseInfo() throws InterruptedException {
        //when the user lets go of the button, send the mouse coordinates to the variables.
        // the game loops stays frozen for 500 ms , i fried my brains out , but i cant implement async function.
        // #StdDraw.thisLibraryIsShitAlthoughIstillRespectJava

        if (!StdDraw.isMousePressed()){
            return;
        }

        if (mouseX == StdDraw.mouseX() && mouseY == StdDraw.mouseY()){
            return;
        }

        mouseX = (int) StdDraw.mouseX();
        mouseY = (int) StdDraw.mouseY();

        if (mouseX < WorldTree.WIDTH && mouseY < WorldTree.HEIGHT){
            System.out.println("set" + mouseX + " " + mouseY);
            StdDraw.text(3, WorldTree.HEIGHT + 3, world[mouseX][mouseY].description());
            StdDraw.show();
            Thread.sleep(350);
            StdDraw.clear();
        }
    }

    public String listenForCommand (){
        StringBuilder s = new StringBuilder();
            if (StdDraw.hasNextKeyTyped()) {
                s.append(StdDraw.nextKeyTyped());
            }
        return s.toString();
    }
}

