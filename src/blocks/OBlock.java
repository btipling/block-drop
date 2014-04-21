package blocks;

import java.awt.*;

public class OBlock extends Block {
    protected int[][] rotation = new int[][]{
        {0, 0, 0, 0},
        {0, 1, 1, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0},
    };
    @Override
    public Color getColor() {
        return new Color(128, 114, 0);
    }

    @Override
    protected int[][] getRotationA() {
        return rotation;
    }

    @Override
    protected int[][] getRotationB() {
        return rotation;
    }

    @Override
    protected int[][] getRotationC() {
        return rotation;
    }

    @Override
    protected int[][] getRotationD() {
        return rotation;
    }
}