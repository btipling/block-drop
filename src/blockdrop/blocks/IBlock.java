package blockdrop.blocks;

import java.awt.*;

public class IBlock extends Block {
    protected int[][] rotationA = new int[][]{
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0},
    };
    protected int[][] rotationB = new int[][]{
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
    };
    protected int[][] rotationC = new int[][]{
        {0, 1, 0, 0},
        {0, 1, 0, 0},
        {0, 1, 0, 0},
        {0, 1, 0, 0},
    };
    @Override
    public Color getColor() {
        return new Color(69, 165, 197);
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
        return rotationA;
    }

    @Override
    protected int[][] getRotationD() {
        return rotationC;
    }
}
