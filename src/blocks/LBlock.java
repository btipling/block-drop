package blocks;

import java.awt.*;

public class LBlock extends Block {
    protected int[][] rotationA = new int[][]{
        {0, 0, 0,},
        {1, 1, 1,},
        {1, 0, 0,},
    };
    protected int[][] rotationB = new int[][]{
        {1, 1, 0,},
        {0, 1, 0,},
        {0, 1, 0,},
    };
    protected int[][] rotationC = new int[][]{
        {0, 0, 1,},
        {1, 1, 1,},
        {0, 0, 0,},
    };
    protected int[][] rotationD = new int[][]{
        {0, 1, 0,},
        {0, 1, 0,},
        {0, 1, 1,},
    };
    @Override
    public Color getColor() {
        return new Color(128, 41, 0);
    }

    @Override
    protected int[][] getRotationA() {
        return rotationA;
    }

    @Override
    protected int[][] getRotationB() {
        return rotationB;
    }

    @Override
    protected int[][] getRotationC() {
        return rotationC;
    }

    @Override
    protected int[][] getRotationD() {
        return rotationD;
    }
}
