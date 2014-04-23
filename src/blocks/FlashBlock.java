package blocks;

import java.awt.*;

/**
 * This is not a game block, just an animation state.
 */
public class FlashBlock extends Block {
    @Override
    public Color getColor() {
        return new Color(255, 255, 255);
    }

    @Override
    protected int[][] getRotationA() {
        return new int[0][];
    }

    @Override
    protected int[][] getRotationB() {
        return new int[0][];
    }

    @Override
    protected int[][] getRotationC() {
        return new int[0][];
    }

    @Override
    protected int[][] getRotationD() {
        return new int[0][];
    }
}
