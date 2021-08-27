package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.util.Random;
public class Utils {

    // checks if move is valid using validate helper
    // player movement process
    // => reverse existing tile to floor
    // => render player again (based on its new position)
    public static void validateAndMakeMove(String direction, Player character, TETile[][] world) {
        if (validateMove(direction, character, world)) {
            reversePlayerTile(character, world);
            switch (direction) {
                case "w" -> character.y += 1;
                case "a" -> character.x -= 1;
                case "s" -> character.y -= 1;
                case "d" -> character.x += 1;
            }
        }
    }

    public static boolean validateMove (String direction, Player character, TETile[][] world){
        int x;
        int y;

        switch (direction) {
            case "w":
                x = character.x;
                y = character.y + 1;
                break;
            case "a":
                x = character.x - 1;
                y = character.y;
                break;
            case "s":
                x = character.x;
                y = character.y - 1;
                break;
            case "d":
                x = character.x + 1;
                y = character.y;
                break;
            default:
                return false;
        }

        if (0 > x || WorldTree.WIDTH <= x || 0 > y || WorldTree.HEIGHT <= y){
            return false;
        }

        if (world[x][y] == Tileset.WALL){
            return false;
        }

        return true;
    }

    // reversed player tile to floor tile
    // intermediate function in making move
    public static void reversePlayerTile(Player character, TETile[][] world){
        world[character.x][character.y] = Tileset.FLOOR;
    }

    public static void showCollectibleStats(TETile[][] world, int CollectiblesLeft, int Collectibles){
        StdDraw.text(10, WorldTree.HEIGHT + 3, "Collectibles left to collect : " + CollectiblesLeft + "/" + Collectibles);
        StdDraw.show();
        StdDraw.clear();
    }

    public static void showMouseInfo(TETile[][] world) throws InterruptedException {
        //when the user lets go of the button, send the mouse coordinates to the variables.
        // the game loops stays frozen for 500 ms , i fried my brains out , but i cant implement async function.
        // #StdDraw.thisLibraryIsShitAlthoughIstillRespectJava

        if (!StdDraw.isMousePressed()){
            return;
        }

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        if (mouseX < WorldTree.WIDTH && mouseY < WorldTree.HEIGHT){
            StdDraw.text(3, WorldTree.HEIGHT + 3, world[mouseX][mouseY].description());
            StdDraw.show();
            Thread.sleep(350);
            StdDraw.clear();
        }
    }

    public static String listenForCommand (){
        StringBuilder s = new StringBuilder();
        if (StdDraw.hasNextKeyTyped()) {
            s.append(StdDraw.nextKeyTyped());
        }
        return s.toString();
    }


    /**
     * Calculates proper split size for the left subchild
     * if it can't find the size according to the ratios , it returns -1
     * (it happens when iterations are over 10)
     * @param leftVariable - leftchild's height or width, which needs to be randomly generated
     * @param leftConstant - height or width , whichever is constant
     * @param isVertical - is split is vertical
     * @return split size for the left child , -1 if not possible (unfit according to the ratio)
     */
    static int splitSize(int leftVariable, int leftConstant, boolean isVertical, Random RANDOM) {

        float ratio1;
        float ratio2;

        int iterations = 0;
        while (iterations <= 10){
            int leftSize = RANDOM.nextInt(leftVariable);
            int rightSize = leftVariable - leftSize;

            if (isVertical){
                ratio1 = (float) leftSize/leftConstant;
                ratio2 = (float) rightSize/leftConstant;
            }else{
                ratio1 = (float) leftConstant/leftSize;
                ratio2 = (float) leftConstant/rightSize;
            }

            if (ratio1 > 0.45 && ratio2 > 0.45){
                return leftSize;
            }
            iterations++;
        }

        return -1;
    }

    static int getOffset(int size, Random RANDOM){
        int factor = 2;
        if (size/factor != 0){
            return RANDOM.nextInt(size/factor);
        }else{
            return 0;
        }
    }


    static int validateSeed(String seed){
        int numericSeed = 69420;
        // default seed
        if (seed == null){
            return numericSeed;
        }

        seed = seed.toUpperCase();

        // validating the format
        if (seed.charAt(0) != 'N' && seed.charAt(seed.length() - 1) == 'S'){
            System.out.println("Invalid seed (format - \"N<numeric seed>S\"  )");
            System.exit(0);
        }

        // obtaining the numeric value from the seed , and validating it
        seed = seed.substring(1,seed.length() - 1);

        try {
            numericSeed = Integer.parseInt(seed);
        } catch (NumberFormatException e){
            System.out.println("Seed should be numeric ! (format - \"N<numeric seed>S\"  )");
            System.exit(0);
        }

        return numericSeed;
    }

}
