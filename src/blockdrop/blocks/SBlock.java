package blockdrop.blocks;

import java.awt.*;

public class SBlock extends Block {
    protected int[][] rotationA = new int[][]{
        {0, 0, 0,},
        {0, 1, 1,},
        {1, 1, 0,},
    };
    protected int[][] rotationB = new int[][]{
        {0, 1, 0,},
        {0, 1, 1,},
        {0, 0, 1,}
    };
    protected int[][] rotationC = new int[][]{
        {1, 0, 0,},
        {1, 1, 0,},
        {0, 1, 0,}
    };
    protected static Color color = new Color(69, 197, 69);

    @Override
    public Color getColor() {
        return color;
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
