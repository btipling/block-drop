package blocks;

import dropblock.utils.GLog;

import java.awt.*;
import java.util.Random;

public abstract class Block {

    protected int[][][] rotations;
    private int currentRotation = 0;
    public static final Block[] BLOCK_TYPES = new Block[]{
        null,
        new IBlock(),
        new OBlock(),
        new TBlock(),
        new SBlock(),
        new ZBlock(),
        new JBlock(),
        new LBlock(),
    };

    public static Block getRandomBlock() {
        Random rand = new Random();
        Block block = BLOCK_TYPES[rand.nextInt(BLOCK_TYPES.length - 1) + 1];
        @SuppressWarnings("unchecked")
        Class<Block> BlockType = (Class<Block>) block.getClass();
        try {
            return BlockType.newInstance();
        } catch (Exception e) {
            return new SBlock();
        }
    }

    public static int getBlockType(Block blockToCheck) {
        for(int i = 1; i < BLOCK_TYPES.length; i++) {
            Block block = BLOCK_TYPES[i];
            if (blockToCheck.getClass().equals(block.getClass())) {
                return i;
            }
        }
        return 0;
    }

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

    public void drawBlock(Graphics2D g2d, int col, int row, int containerDimension, int boxDimension) {
        g2d.setColor(getColor());
        g2d.fillRect(col * containerDimension + 1, row * containerDimension + 1, boxDimension, boxDimension);
    }

    public int[][] getDefaultRotation() {
        return getRotationA();
    }

    public abstract Color getColor();
    protected abstract int[][] getRotationA();
    protected abstract int[][] getRotationB();
    protected abstract int[][] getRotationC();
    protected abstract int[][] getRotationD();
}
