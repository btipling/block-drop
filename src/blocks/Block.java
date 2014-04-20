package blocks;

import dropblock.utils.GLog;

import java.awt.*;
import java.util.Random;

public abstract class Block {
    public static enum BLOCK_TYPES {
        NONE,
        SBLOCK,
        ZBLOCK,
        LBLOCK,
        JBLOCK,
        TBLOCK,
        IBLOCK,
        OBLOCK,
    };
    protected int[][][] rotations;
    private int currentRotation = 0;


    public Block() {
       GLog.info("Setting up a block.");
    };

    public void setupBlock() {
        rotations = new int[][][]{
            getRotationA(),
            getRotationB(),
            getRotationC(),
            getRotationD(),
        };
        Random rand = new Random();
        currentRotation = rand.nextInt(4);
    }

    public int[][] getCurrentRotation() {
       return rotations[currentRotation];
    }

    public void rotate() {
        currentRotation++;
        if (currentRotation >= rotations.length) {
            currentRotation = 0;
        }
    }

    public int getBlockType() {
        return 0;
    }

    public abstract Color getColor();
    protected abstract int[][] getRotationA();
    protected abstract int[][] getRotationB();
    protected abstract int[][] getRotationC();
    protected abstract int[][] getRotationD();

}
