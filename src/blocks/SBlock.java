package blocks;

import java.awt.*;

public class SBlock extends Block {
    protected int[] rotationA = {
        0, 0, 0,
        1, 1, 0,
        0, 1, 1
    };
    protected int[] rotationB = {
        0, 0, 1,
        0, 1, 1,
        0, 1, 0
    };
    protected int[] rotationC = rotationA;
    protected int[] rotationD = rotationB;
    protected Color color = new Color(128, 13, 21);
}
