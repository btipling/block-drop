package blocks;

import java.awt.*;
import java.util.Random;

public abstract class Block {
    protected int[] rotationA;
    protected int[] rotationB;
    protected int[] rotationC;
    protected int[] rotationD;
    protected int[][] rotations;
    private int currentRotation = 0;
    protected final Color color = new Color(121, 121, 121);



    public Block() {
        rotations = new int[][]{
            rotationA,
            rotationB,
            rotationC,
            rotationD,
        } ;
        Random rand = new Random();
        currentRotation = rand.nextInt(5);
    }

    public int[] getCurrentRotation() {
       return rotations[currentRotation];
    }

    public void rotate() {
        currentRotation++;
        if (currentRotation >= rotations.length) {
            currentRotation = 0;
        }
    }

    public Color getColor() {
        return color;
    }

}
