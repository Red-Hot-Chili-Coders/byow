package byow.Core;

import static byow.Core.WorldTree.RANDOM;

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
    static int splitSize(int leftVariable, int leftConstant, boolean isVertical) {

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
    static int getOffset(int size){
        int factor = 2;
        if (size/factor != 0){
            return RANDOM.nextInt(size/factor);
        }else{
            return 0;
        }
    }
}
