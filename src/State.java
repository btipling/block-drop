import blocks.Block;
import blocks.SBlock;
import dropblock.utils.GLog;

import java.util.ArrayList;

public class State {
    public static final int DISPLAY_ROW_START = 2;
    private int[][] boardState;
    private int[][] storedBoardState;
    private Block currentDroppingBlock = null;
    private int[] currentBlockPos;
    public static final int NUM_ROWS = 22;
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
            moveDown();
        }
    }

    public void updateGameBoard() {
        copyBoardState(storedBoardState, boardState);
        if (currentDroppingBlock == null) {
            return;
        }
        int[][] position = currentDroppingBlock.getCurrentRotation();
        for (int y = 0; y < position.length; y++) {
            int[] row = position[y];
            for (int x = 0; x < row.length; x++) {
                int yPos = currentBlockPos[1] + y;
                int xPos = currentBlockPos[0] + x;
                if (xPos < 0) {
                    // Block rotations can have negative margins.
                    continue;
                }
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

    public int[][] getBoardState() {
        return boardState;
    }

    public void endCurrentGame() {
        currentDroppingBlock = null;
        zeroBoardState();
    }

    private void zeroBoardState() {
        boardState = newSetupList().toArray(new int[NUM_COLS][NUM_ROWS]);
        storedBoardState = newSetupList().toArray(new int[NUM_COLS][NUM_ROWS]);
    }

    private ArrayList<int[]> newSetupList() {
        ArrayList<int[]> setUpList = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            setUpList.add(new int[]{
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            });
        }
        return setUpList;
    }

    private void copyBoardState(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; i++) {
            System.arraycopy(a[i], 0, b[i], 0, a[i].length);
        }
    }

    public void rotate() {
        GLog.info("rotate");
        if (currentBlockPos == null || currentDroppingBlock == null) {
            return;
        }
        currentDroppingBlock.rotate();
        int offset = currentDroppingBlock.getLeftBorderColumn();
        while(currentBlockPos[0] + offset < 0) {
           moveRight();
        }
        offset = currentDroppingBlock.getRightBorderColumn();
        while (currentBlockPos[0] + offset > NUM_COLS) {
            moveLeft();
        }
    }

    public void drop() {
        GLog.info("dropping");
    }

    public void moveLeft() {
        if (currentBlockPos == null || currentDroppingBlock == null) {
            return;
        }
        int offset = currentDroppingBlock.getLeftBorderColumn();
        if (currentBlockPos[0] + offset == 0) {
            // On left edge.
            return;
        }
        int[][] rotation = currentDroppingBlock.getCurrentRotation();
        for (int i = 0; i < rotation.length; i++) {
            for (int j = 0; j <= offset; j++) {
                if (rotation[i][j] == 0) {
                    continue;
                }
                int yPos = currentBlockPos[1] + i;
                int xPos = currentBlockPos[0] + j - 1;
                if (storedBoardState[yPos][xPos] > 0) {
                    // Block is blocked to the left.
                    return;
                }
            }
        }
        currentBlockPos[0]--;
    }

    public void moveRight() {
        if (currentBlockPos == null || currentDroppingBlock == null) {
            // No current block;
            return;
        }
        int offset = currentDroppingBlock.getRightBorderColumn();
        if (currentBlockPos[0] + offset + 1 >= NUM_COLS) {
            // On right edge.
            return;
        }
        int[][] rotation = currentDroppingBlock.getCurrentRotation();
        for (int i = 0; i < rotation.length; i++) {
            for (int j = 0; j <= offset; j++) {
                if (rotation[i][j] == 0) {
                    continue;
                }
                int yPos = currentBlockPos[1] + i;
                int xPos = currentBlockPos[0] + j + 1;
                if (storedBoardState[yPos][xPos] > 0) {
                    // Block is blocked to the right.
                    return;
                }
            }
        }
        currentBlockPos[0]++;
    }

    public void moveDown() {
        if (currentBlockPos == null || currentDroppingBlock == null) {
            return;
        }
        int offset = currentDroppingBlock.getBottomBorderRow();
        if (currentBlockPos[1] + offset + 1 >= NUM_ROWS) {
            hitBottom();
            return;
        }
        int[][] rotation = currentDroppingBlock.getCurrentRotation();
        for (int i = 0; i <= offset; i++) {
            for (int j = 0; j < rotation[i].length; j++) {
                if (rotation[i][j] == 0) {
                    continue;
                }
                int yPos = currentBlockPos[1] + i + 1;
                int xPos = currentBlockPos[0] + j;
                if (storedBoardState[yPos][xPos] > 0) {
                    hitBottom();
                    return;
                }
            }
        }
        currentBlockPos[1]++;
    }

    private void hitBottom() {
        copyBoardState(boardState, storedBoardState);
        currentDroppingBlock = null;
    }

}
