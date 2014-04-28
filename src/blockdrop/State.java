package blockdrop;

import blockdrop.sound.MP3Sound;
import blockdrop.sound.WavSound;
import blockdrop.blocks.Block;
import blockdrop.utils.GLog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {


    interface StateChangeListener {
        public void stateChanged();
    }

    private List<StateChangeListener> newBlockListeners = new ArrayList<>();
    private List<StateChangeListener> animationListeners = new ArrayList<>();
    private List<StateChangeListener> scoreListeners = new ArrayList<>();
    private List<StateChangeListener> gameOverListeners = new ArrayList<>();


    public static final int DISPLAY_ROW_START = 2;
    private boolean paused;
    private boolean gameOver = true;
    private int score = 0;
    private int level = 0;
    private int lines = 0;
    private int linesThisLevel = 0;
    private int linesToIncreaseLevel = 0;
    private int[][] boardState;
    private int[][] storedBoardState;
    private Block currentDroppingBlock = null;
    private Block nextBlock = null;
    private int[] currentBlockPos;
    public static final int NUM_ROWS = 22;
    public static final int NUM_COLS = 10;
    private int animationState = 0;
    private WavSound dropSound;
    private WavSound rotateSound;
    private WavSound moveSound;
    private WavSound moveDownSound;
    private WavSound endGameSound;
    private WavSound startGameSound;
    private WavSound pauseSound;
    private WavSound linesSound;
    private WavSound bonusSound;
    private WavSound levelUpSound;
    private MP3Sound music;
    private boolean playSound = true;
    private boolean playMusic = true;

    public State(boolean playMusic) {
        zeroBoardState();
        dropSound = new WavSound("drop.wav");
        rotateSound = new WavSound("rotate.wav");
        moveSound = new WavSound("move.wav");
        moveDownSound = new WavSound("movedown.wav");
        endGameSound = new WavSound("gameover.wav");
        startGameSound = new WavSound("start.wav");
        pauseSound = new WavSound("pause.wav");
        linesSound = new WavSound("lines.wav");
        bonusSound = new WavSound("bonus.wav");
        levelUpSound = new WavSound("levelup.wav");
        music = new MP3Sound("music.mp3");
        music.setUp();
        this.playMusic = playMusic;
        if (playMusic) {
            music.play();
        }
    }

    public void playSoundEffects(boolean soundEffects) {
        playSound = soundEffects;
    }

    public void playMusic(boolean musicEffects) {
        playMusic = musicEffects;
        if (playMusic) {
            music.play();
        } else {
            music.stop();
        }
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused  = paused;
        if (playSound) {
            pauseSound.play();
        }
    }

    private Block getNewRandomBlock() {
        currentBlockPos = new int[]{3, 0};
        if (nextBlock == null) {
            nextBlock = Block.getRandomBlock();
        }
        Block block = nextBlock;
        nextBlock = Block.getRandomBlock();
        block.setupBlock();
        fireStateChangeListners(newBlockListeners);
        if (playSound) {
            moveDownSound.play();
        }
        return block;
    }

    private void fireStateChangeListners(List<StateChangeListener> listeners) {
        for (StateChangeListener listener : listeners) {
            listener.stateChanged();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void startGame() {
        level = 0;
        score = 0;
        lines = 0;
        gameOver = false;
        fireStateChangeListners(scoreListeners);
        zeroBoardState();
        fireStateChangeListners(animationListeners);
        if (playSound) {
            startGameSound.play();
        }
    }

    public void addBlockDroppedListener(StateChangeListener listener) {
        newBlockListeners.add(listener);
    }

    public void addAnimationStateListener(StateChangeListener listener) {
        animationListeners.add(listener);
    }

    public void addScoreStateListener(StateChangeListener listener) {
        scoreListeners.add(listener);
    }

    public void addGameOverListener(StateChangeListener listener) {
        gameOverListeners.add(listener);
    }

    public void tick() {
        if (animationState > 0) {
            return;
        }
        GLog.info("ticking");
        if (linesToIncreaseLevel == 0) {
            linesToIncreaseLevel = level * 10 + 10;
        }
        if (currentDroppingBlock == null) {
            currentDroppingBlock = getNewRandomBlock();
        } else {
            moveDown();
        }
    }

    private void scoreGame() {
        int roundScore = 0;
        int lineMultiples = 0;
        boolean scored = false;
        boolean hasBonus = false;
        List<Integer> rowsToKill = new ArrayList<>();
        for (int r = 0; r < boardState.length; r++) {
            boolean isComplete = true;
            for (int c = 0; c < boardState[r].length; c++) {
                if (boardState[r][c] == 0) {
                    roundScore += scoreMultiples(lineMultiples);
                    lineMultiples = 0;
                    isComplete = false;
                    break;
                }
            }
            if (isComplete) {
                rowsToKill.add(r);
                scored = true;
                lineMultiples++;
                lines++;
                linesThisLevel++;
            }
            if (lineMultiples == 4) {
                roundScore += scoreMultiples(lineMultiples);
                hasBonus = true;
                lineMultiples = 0;
            }
        }
        if (!scored) {
            return;
        }
        score += roundScore + scoreMultiples(lineMultiples);
        fireStateChangeListners(scoreListeners);
        killRows(rowsToKill);
        if (playSound) {
            if (hasBonus) {
                bonusSound.play();
            } else {
                linesSound.play();
            }
        }
    }

    private void checkLevel() {
        if (linesThisLevel < linesToIncreaseLevel) {
            return;
        }
        linesThisLevel = 0;
        linesToIncreaseLevel = 10;
        level++;
        fireStateChangeListners(scoreListeners);
        if (playSound) {
            levelUpSound.play();
        }
    }

    private void killRows(List<Integer> rowsToKill) {
        if (rowsToKill.size() < 1) {
            return;
        }
        animationState++;
        GLog.info("kill row animating starting.");
        for (Integer r : rowsToKill) {
            for (int c = 0; c < boardState[r].length; c++) {
                boardState[r][c] = Block.BLOCK_TYPES.length - 1; //The flash block type.
            }
        }
        copyBoardState(boardState, storedBoardState);
        fireStateChangeListners(animationListeners);
        final Timer timer = new Timer(75, null);
        timer.addActionListener(e -> {
            GLog.info("animating.");
            timer.stop();
            removeRows(rowsToKill);
            copyBoardState(boardState, storedBoardState);
            fireStateChangeListners(animationListeners);
            animationState--;
            GLog.info("animating finished.");
            checkLevel();
            return;
        });
        timer.start();
        GLog.info("Started.");
    }

    private void removeRows(List<Integer> rowsToKill) {
        int size = rowsToKill.size();
        Integer[] rows = rowsToKill.toArray(new Integer[size]);
        Arrays.sort(rows);
        // Starts at bottom and moves up.
        int iterations = 0;
        for (int i = rows.length - 1; i >= 0; i--) {
            // Each row to be removed is one line down for each iteration.
            removeRow(rows[i] + iterations);
            iterations++;
        }
    }

    private void removeRow(int r) {
        while (r > 0) {
            System.arraycopy(boardState[r - 1], 0, boardState[r], 0, boardState[r].length);
            r--;
        }
        for (int c = 0; c < boardState[0].length; c++) {
            boardState[0][c] = 0;
        }
    }

    private int scoreMultiples(int lineMultiples) {
        switch (lineMultiples) {
            case 1:
                return 40 * (level + 1);
            case 2:
                return 100 * (level + 1);
            case 3:
                return 300 * (level + 1);
            case 4:
                return 1200 * (level + 1);
        }
        return 0;
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
                    // Block rotations can have negative positions because of rotation padding.
                    continue;
                }
                if (yPos >= boardState.length) {
                    break;
                }
                if (xPos >= boardState[yPos].length) {
                    continue;
                }
                if (row[x] > 0) {
                    boardState[yPos][xPos] = Block.getBlockType(currentDroppingBlock);
                }
            }
        }
    }

    public int[][] getBoardState() {
        return boardState;
    }

    public void endCurrentGame() {
        gameOver = true;
        currentDroppingBlock = null;
        score = 0;
        lines = 0;
        level = 0;
        killBoard();
        if (playSound) {
            endGameSound.play();
        }
    }

    private void killBoard() {
        animationState++;
        final Timer timer = new Timer(5, null);
        GLog.info("kill board animating starting.");
        final int[] coords = new int[]{0, 0};
        timer.addActionListener(e -> {
            storedBoardState[coords[1]][coords[0]] = Block.getRandomBlockType();
            fireStateChangeListners(animationListeners);
            coords[0]++;
            if (coords[0] >= boardState[0].length) {
                coords[1]++;
                coords[0] = 0;
            }
            if (coords[1] >= boardState.length) {
                timer.stop();
                animationState--;
                fireStateChangeListners(gameOverListeners);
            }
        });
        timer.start();
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
        if (!canMove()) {
            return;
        }
        currentDroppingBlock.rotate();
        int previousXPos = currentBlockPos[0];
        int previousYPos = currentBlockPos[1];
        int [][] rotation = currentDroppingBlock.getCurrentRotation();
        int max_moves = rotation[0].length;
        int max_upward_movements = rotation.length - 1;
        int movements = 0;
        while (movements < max_moves && (pastLeftEdge() || isObstructed())) {
            // Possibly obstructed to the left, move right a bit.
            forceRight();
            movements++;
        }
        while (movements < max_moves && (pastRightEdge() || isObstructed())) {
            // Maybe obstructed to the right, revert any rightward movements and move left a bit.
            currentBlockPos[0] = previousXPos;
            forceLeft();
            movements++;
        }
        if (!isObstructed()) {
            if (playSound) {
                rotateSound.play();
            }
            return;
        }
        // Still obstructed, revert any left or right movements and try moving up a bit.
        currentBlockPos[0] = previousXPos;
        // First get away from edge if any before moving up:
        while(pastLeftEdge()) {
            forceRight();
        }
        while(pastRightEdge()) {
            forceLeft();
        }
        // Now move up.
        movements = 0;
        while (isObstructed() && movements < max_upward_movements) {
            currentBlockPos[1]--;
            movements++;
        }
        if (isObstructed()) {
            // Can't rotate, revert rotate and any moves.
            currentBlockPos[0] = previousXPos;
            currentBlockPos[1] = previousYPos;
            currentDroppingBlock.rotateBack();
            return;
        }
        if (playSound) {
            rotateSound.play();
        }
    }

    private boolean pastLeftEdge() {
        int offset = currentDroppingBlock.getLeftBorderColumn();
        return currentBlockPos[0] + offset < 0;
    }

    private boolean pastRightEdge() {
        int offset = currentDroppingBlock.getRightBorderColumn();
        return currentBlockPos[0] + offset >= NUM_COLS;
    }

    /**
     * Check if block is currently obstructed.
     * @return true if it is.
     */
    private boolean isObstructed() {
        if (pastLeftEdge()) {
            return true;
        }
        if (pastRightEdge()) {
            return true;
        }
        int[][] rotation = currentDroppingBlock.getCurrentRotation();
        for(int i = 0; i < rotation.length; i++) {
            for (int j = 0; j < rotation[i].length; j++) {
                if (rotation[i][j] == 0) {
                    continue;
                }
                int xPos = currentBlockPos[0] + j;
                int yPos = currentBlockPos[1] + i;
                try {
                    if (storedBoardState[yPos][xPos] > 0) {
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }
            }
        }
        return false;
    }

    public void drop() {
        if (!canMove()) {
            return;
        }
        GLog.info("dropping");
        animationState++;
        final Timer timer = new Timer(2, null);
        GLog.info("drop animating starting.");
        timer.addActionListener(e -> {
            GLog.info("animating.");
            fireStateChangeListners(animationListeners);
            if (currentDroppingBlock == null) {
                animationState--;
                timer.stop();
                GLog.info("animating finished.");
                return;
            }
            moveBlockDown();
        });
        timer.start();
        GLog.info("Started.");
    }


    private boolean canMove() {
        return !gameOver && currentBlockPos != null && currentDroppingBlock != null && !paused;
    }

    public void moveLeft() {
        if (!canMove()) {
            return;
        }
        forceLeft();
        if (isObstructed()) {
            forceRight();
            return;
        }
        if (playSound) {
            moveSound.play();
        }
    }

    private void forceLeft() {
        currentBlockPos[0]--;
    }

    public void moveRight() {
        if (!canMove()) {
            return;
        }
        forceRight();
        if (isObstructed()) {
            forceLeft();
            return;
        }
        if (playSound) {
            moveSound.play();
        }
    }

    private void forceRight() {
        currentBlockPos[0]++;
    }

    public void moveDown() {
        if (!canMove()) {
            return;
        }
        moveBlockDown();
        if (playSound) {
            moveDownSound.play();
        }
    }


    public void moveBlockDown() {
        if (!canMove()) {
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

    public int getScore() {
        return score;
    }

    public int getLines() {
        return lines;
    }

    public int getLevel() {
        return level;
    }

    private void hitBottom() {
        if (playSound) {
            dropSound.play();
        }
        copyBoardState(boardState, storedBoardState);
        currentDroppingBlock = null;
        scoreGame();
        if (currentBlockPos[1] < 1) {
            endCurrentGame();
        }
    }

}
