package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import static byow.Core.WorldTree.HEIGHT;
import static byow.Core.WorldTree.WIDTH;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            TETile[][] world = engine.interactWithInputString(args[1]);

            TERenderer te = new TERenderer();
            te.initialize(WIDTH, HEIGHT);

            te.renderFrame(world);
        } else if (args.length == 2 && args[0].equals("-p")) {
            System.out.println("Coming soon.");
        }
        else {
            // if no argument given , launch gui and allow user to type the seed
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
