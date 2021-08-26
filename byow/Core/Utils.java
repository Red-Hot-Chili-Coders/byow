package byow.Core;

import byow.TileEngine.TERenderer;

import java.util.Random;

import static byow.Core.WorldTree.HEIGHT;
import static byow.Core.WorldTree.WIDTH;

public class Utils {
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
