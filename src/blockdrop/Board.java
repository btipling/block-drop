package blockdrop;

import blockdrop.utils.GLog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.prefs.Preferences;

public class Board {

    private class GameKeyHandler implements GameFrame.GameKeyListener {
        @Override
        public void gameKeyTriggered(KeyEvent e) {
            GLog.info("Key pressed!");
            int pressedChar = e.getKeyCode();
            switch (pressedChar) {
                case 82: //r
                    state.rotate();
                    break;
                case 68: //d
                    state.drop();
                    break;
                case 80: //p
                    togglePause();
                    break;
                case 83: //s
                    toggleGame();
                    break;
            }
            updateGame();
        }
    }

    private class GameKeyDownHandler implements GameFrame.GameKeyListener {

        @Override
        public void gameKeyTriggered(KeyEvent e) {
            int pressedChar = e.getKeyCode();
            switch (pressedChar) {
                case 37: //left arrow
                    if (movingLeft) {
                        return;
                    }
                    movingDown = false;
                    movingRight = false;
                    state.moveLeft();
                    movingLeft = true;
                    updateGame();
                    startMoving();
                    break;
                case 39: //right arrow
                    if (movingRight) {
                        return;
                    }
                    movingLeft = false;
                    movingDown = false;
                    state.moveRight();
                    movingRight = true;
                    updateGame();
                    startMoving();
                    break;
                case 40: //down arrow
                    if (movingDown) {
                        return;
                    }
                    movingLeft = false;
                    movingRight = false;
                    GLog.info("Moving down");
                    state.moveDown();
                    movingDown = true;
                    updateGame();
                    startMoving();
                    break;
            }
        }
    }



    private class GameKeyUpHandler implements GameFrame.GameKeyListener {

        @Override
        public void gameKeyTriggered(KeyEvent e) {
            int pressedChar = e.getKeyCode();
            switch (pressedChar) {
                case 37: //left arrow
                    if (movingLeft) {
                        movingLeft = false;
                        stopMoving();
                    }
                    break;
                case 39: //right arrow
                    if (movingRight) {
                        movingRight = false;
                        stopMoving();
                    }
                    break;
                case 40: //down arrow
                    if (movingDown) {
                        movingDown = false;
                        stopMoving();
                    }
                    break;
            }
        }
    }

    final static String TOP_SCORE_PREF = "topscorepref";
    final static String SOUND_EFFECTS_PREF = "soundeffectspref";
    final static String MUSIC_EFFECTS_PREF = "musicpref";
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
    private JLabel scoreLabel;
    private JLabel topScoreLabel;
    private JLabel levelTitleLabel;
    private JLabel linesTitleLabel;
    private JTextArea instructionsLabel;
    private JLabel nextLabel;
    private JButton resetTopScoreButton;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectsCheckBox;
    private Timer timer;
    private State state;
    private Preferences prefs;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private Timer moveTimer;

    public Board(Preferences prefs, Font customFont) {
        this.prefs = prefs;
        startButton.addActionListener((e) -> this.toggleGame());
        pauseButton.addActionListener(e -> this.togglePause());
        soundEffectsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean soundEffects = soundEffectsCheckBox.isSelected();
                prefs.putBoolean(SOUND_EFFECTS_PREF, soundEffects);
                state.playSoundEffects(soundEffects);
            }
        });
        musicCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean musicEffects = musicCheckBox.isSelected();
                prefs.putBoolean(SOUND_EFFECTS_PREF, musicEffects);
                state.playMusic(musicEffects);
            }
        });
        resetTopScoreButton.addActionListener(e -> {
            prefs.putInt(TOP_SCORE_PREF, 0);
            topScore.setText("0");
        });
        customFont = customFont.deriveFont(15.0f);
        Object[] labels = new Object[]{topScore, scoreLabel, currentScore, linesLabel, linesTitleLabel, levelLabel,
            levelTitleLabel, instructionsLabel, topScoreLabel, nextLabel, startButton, pauseButton, resetTopScoreButton};
        for (Object label : labels) {
            JComponent component = (JComponent) label;
            component.setFont(customFont);
            component.setForeground(new Color(66, 66, 66));
            component.setBackground(Color.WHITE);
            component.setOpaque(true);
        }
        int score = prefs.getInt(TOP_SCORE_PREF, 0);
        topScore.setText(NumberFormat.getNumberInstance().format(score));
        ((GamePanel) gamePanel).setGameOverFont(customFont);
    }


    private void startMoving() {
        if (moveTimer != null) {
            stopMoving();
        }
        moveTimer = new Timer(175, null);
        moveTimer.addActionListener((e) -> {
            moveTimer.setDelay(25);
            GLog.info("Num listeners %d", moveTimer.getActionListeners().length);
            if (movingLeft) {
               state.moveLeft();
            }
            if (movingDown) {
               state.moveDown();
            }
            if (movingRight) {
               state.moveRight();
            }
            updateGame();
        });
        moveTimer.start();
    }

    private void stopMoving() {
        if (moveTimer == null) {
            return;
        }
        moveTimer.stop();
        moveTimer = null;
    }

    private void togglePause() {
        if (state.isPaused()) {
            pauseButton.setText("Pause");
            unPause();
        } else {
            pauseButton.setText("Unpause");
            pause();
        }
    }

    private void toggleGame() {
        if (state.isGameOver()) {
            start();
        } else {
            stop();
        }
    }

    public void showBoard() {
        GameFrame frame = new GameFrame();
        frame.addGameKeyListener(new GameKeyHandler());
        frame.addGameKeyUpListener(new GameKeyUpHandler());
        frame.addGameKeyDownListener(new GameKeyDownHandler());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.setMinimumSize(new Dimension(520, 645));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setVisible(true);
        musicCheckBox.setSelected(prefs.getBoolean(MUSIC_EFFECTS_PREF, true));
        soundEffectsCheckBox.setSelected(prefs.getBoolean(SOUND_EFFECTS_PREF, true));
    }

    private void createUIComponents() {
        contentPanel = new ContentPanel();
        state = new State(prefs.getBoolean(MUSIC_EFFECTS_PREF, true));
        state.addBlockDroppedListener(() -> ((PreviewPanel) nextPiecePanel).drawNextBlock(state.getNextBlock()));
        gamePanel = new GamePanel(state);
        nextPiecePanel = new PreviewPanel();
        state.addAnimationStateListener(this::updateGame);
        state.addScoreStateListener(this::updateScore);
        state.addGameOverListener(() -> {
            stop();
            ((GamePanel) gamePanel).setGameOver(true);
            updateGame();
        });
    }

    private void updateScore() {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        int score = state.getScore();
        int topScoreValue = prefs.getInt(TOP_SCORE_PREF, 0);
        String formattedScore = String.valueOf(numberFormatter.format(score));
        if (score > topScoreValue) {
            prefs.putInt(TOP_SCORE_PREF, score);
            topScore.setText(formattedScore);
        }
        currentScore.setText(formattedScore);
        levelLabel.setText(String.valueOf(numberFormatter.format(state.getLevel())));
        linesLabel.setText(String.valueOf(numberFormatter.format(state.getLines())));
        updateTimer();
        timer.start();
    }

    private void updateGame() {
        state.updateGameBoard();
        gamePanel.repaint();
    }

    private void updateTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
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

    private void start() {
        if (!state.isGameOver()) {
            return;
        }
        if (timer == null) {
            updateTimer();
        }
        if (timer.isRunning()) {
            return;
        }
        state.setPaused(false);
        pauseButton.setText("Pause");
        state.startGame();
        GLog.info("Started.");
        timer.start();
        startButton.setText("Stop");
        ((GamePanel) gamePanel).setGameOver(false);
        pauseButton.setEnabled(true);
        updateGame();
    }

    private void stop() {
        startButton.setText("Start");
        pauseButton.setEnabled(false);
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        if (state.isGameOver()) {
            return;
        }
        state.endCurrentGame();
        updateGame();
        GLog.info("Stopped.");
    }

    private void pause() {
        if (timer == null) {
            return;
        }
        state.setPaused(true);
        timer.stop();
        GLog.info("Paused.");
    }

    private void unPause() {
        if (timer == null) {
            return;
        }
        if (timer.isRunning()) {
            return;
        }
        state.setPaused(false);
        timer.start();
        GLog.info("unpaused");
    }

}
