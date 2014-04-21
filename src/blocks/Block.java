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
        currentRotation = getNextRotationIndex();
    }

    public void rotateBack() {
        currentRotation = getPreviousRotationIndex();
    }

    private int getPreviousRotationIndex() {
        int previous = currentRotation - 1;
        if (previous < 0) {
            return rotations.length - 1;
        }
        return previous;
    }

    private int getNextRotationIndex() {
        int next = currentRotation + 1;
        if (next >= rotations.length) {
            return 0;
        }
        return next;
    }

    public int getBlockType() {
        return 0;
    }

    /**
     * Returns the row in the block that has the last actual block piece.
     * @return The bottom border row.
     */
    public int getBottomBorderRow() {
       int[][] rotation = getCurrentRotation();
       for (int r = rotation.length - 1; r >= 0; r--) {
           for (int c = 0; c < rotation[r].length; c++) {
               if (rotation[r][c] > 0) {
                   return r;
               }
           }
       }
       return 0;
    }

    /**
     * Returns the left most row with an actual block piece.
     * @return Return the left most border column.
     */
    public int getLeftBorderColumn() {
        int[][] rotation = getCurrentRotation();
        for (int c = 0; c < rotation[0].length; c++) {
            for (int r = 0; r < rotation.length; r++) {
                if (rotation[r][c] > 0) {
                    return c;
                }
            }
        }
        return 0;
    }

    /**
     * Returns the right most row with an actual block piece.
     * @return The right most border column.
     */
    public int getRightBorderColumn() {
        int[][] rotation = getCurrentRotation();
        for (int c = rotation[0].length - 1; c >= 0; c--) {
            for (int r = 0; r < rotation.length; r++) {
                if (rotation[r][c] > 0) {
                    return c;
                }
            }
        }
        return 0;
    }

    public abstract Color getColor();
    protected abstract int[][] getRotationA();
    protected abstract int[][] getRotationB();
    protected abstract int[][] getRotationC();
    protected abstract int[][] getRotationD();

}
