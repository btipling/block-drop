import dropblock.utils.GLog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Board implements GameFrame.GameKeyListener {
    private JButton stopButton;
    private JButton startButton;
    private JButton pauseButton;
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JLabel currentScore;
    private JLabel topScore;
    private JPanel nextPiecePanel;
    private JPanel contentPanel;
    private JLabel levelLabel;
    private JLabel linesLabel;
    private Timer timer;
    private State state;

    public Board() {
        startButton.addActionListener(e -> start());
        stopButton.addActionListener(e -> stop());
        pauseButton.addActionListener(e -> pause());
    }

    public void showBoard() {
        GameFrame frame = new GameFrame();
        frame.addGameKeyListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.setMinimumSize(new Dimension(520, 645));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setVisible(true);
    }

    private void createUIComponents() {
        state = new State();
        state.addBlockDroppedListener(() -> ((PreviewPanel) nextPiecePanel).drawNextBlock(state.getNextBlock()));
        gamePanel = new GamePanel(state);
        nextPiecePanel = new PreviewPanel();
        state.addAnimationStateListener(this::updateGame);
        state.addScoreStateListener(this::updateScore);
        state.addGameOverListener(this::pause);
    }

    private void updateScore() {
        currentScore.setText(String.valueOf(state.getScore()));
        levelLabel.setText(String.valueOf(state.getLevel()));
        linesLabel.setText(String.valueOf(state.getLines()));
        timer.stop();
        timer = null;
        start();
    }

    private void updateGame() {
        state.updateGameBoard();
        gamePanel.repaint();
    }

    private void start() {
        if (timer == null) {
            int speed = 0;
            switch (state.getLevel()) {
                case 0:
                    speed = 750;
                    break;
                case 1:
                    speed = 600;
                    break;
                case 2:
                    speed = 450;
                    break;
                case 3:
                    speed = 350;
                    break;
                case 4:
                    speed = 300;
                    break;
                case 5:
                    speed = 250;
                    break;
                case 6:
                    speed = 200;
                    break;
                case 7:
                    speed = 175;
                    break;
                case 8:
                    speed = 150;
                    break;
                case 9:
                    speed = 125;
                    break;
                case 10:
                    speed = 100;
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    speed = 90;
                    break;
                default:
                    speed = 80;
                    break;
            }
            timer = new Timer(speed, e -> {
                GLog.info("Timer.");
                state.tick();
                updateGame();
            });
        }
        if (timer.isRunning()) {
            return;
        }
        timer.start();
        GLog.info("Started.");
    }

    private void stop() {
        if (timer == null) {
            return;
        }
        pause();
        timer = null;
        state.endCurrentGame();
        updateGame();
        GLog.info("Stopped.");
    }

    private void pause() {
        if (timer == null) {
            return;
        }
        timer.stop();
        GLog.info("Paused.");
    }

    @Override
    public void gameKeyTriggered(KeyEvent e) {
        GLog.info("Key pressed!");
        int pressedChar = e.getKeyCode();
        switch(pressedChar) {
            case 37: //left arrow
                state.moveLeft();
                break;
            case 39: //right arrow
                state.moveRight();
                break;
            case 40: //down arrow
                state.moveDown();
                break;
            case 82: //4
                state.rotate();
                break;
            case 68: //d
                state.drop();
                break;
            case 80: //p
                pause();
                break;
            case 83: //s
                start();
                break;
            case 84: //t
                stop();
                break;
        }
        updateGame();
    }
}
