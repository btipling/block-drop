import blocks.Block;
import blocks.SBlock;

import java.util.ArrayList;

public class State {
    private int[][] boardState;
    private Block currentDroppingBlock = null;
    private int[] currentBlockPos;
    public static final int NUM_ROWS = 24;
    public static final int NUM_COLS = 10;

    public State() {
        zeroBoardState();
    }

    private Block getNewRandomBlock() {
        currentBlockPos = new int[]{4, 0};
        Block block = new SBlock();
        block.setupBlock();
        return block;
    }
    
    public void tick() {
        if (currentDroppingBlock == null) {
            currentDroppingBlock = getNewRandomBlock();
        } else {
            moveBlockDown();
        }
        updateGameBoard();
    }

    private void updateGameBoard() {
        zeroBoardState();
        if (currentDroppingBlock == null) {
            return;
        }
        int[][] position = currentDroppingBlock.getCurrentRotation();
        for (int y = 0; y < position.length; y++) {
            int[] row = position[y];
            for (int x = 0; x < row.length; x++) {
                int yPos = currentBlockPos[1] + y;
                int xPos = currentBlockPos[0] + x;
                if (yPos >= boardState.length) {
                    break;
                }
                if (xPos >= boardState[yPos].length) {
                    continue;
                }
                if (row[x] > 0) {
                    boardState[yPos][xPos] = currentDroppingBlock.getBlockType();
                }
            }
        }
    }

    private void moveBlockDown() {
        currentBlockPos[1]++;
        if (currentBlockPos[1] >= NUM_ROWS) {
            currentDroppingBlock = null;
        }
    }

    public int[][] getBoardState() {
        return boardState;
    }

    private void zeroBoardState() {
        ArrayList<int[]> setUpList = new ArrayList<int[]>();
        for (int i = 0; i < NUM_ROWS; i++) {
            setUpList.add(new int[]{
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            });
        }
        boardState = setUpList.toArray(new int[NUM_COLS][NUM_ROWS]);
    }
}
