import dropblock.utils.GLog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Board {
    private JButton stopButton;
    private JButton startButton;
    private JButton pauseButton;
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JLabel currentScore;
    private JLabel topScore;
    private JPanel nextPiecePanel;
    private JPanel contentPanel;
    private JTextArea useArrowKeysToTextArea;
    private Timer timer;
    private State state;

    public Board() {
        startButton.addActionListener(e -> start());
        stopButton.addActionListener(e -> stop());
    }

    public void showBoard () {
        JFrame frame = new JFrame("Drop Block");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.setMinimumSize(new Dimension(520, 649));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setVisible(true);
    }

    private void createUIComponents() {
        state = new State();
        gamePanel = new GamePanel(state);
    }

    private void start() {
        stop();
        if (timer == null) {
            timer = new Timer(1000, e -> {
                GLog.info("Timer.");
                state.tick();
                gamePanel.repaint();
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
        GLog.info("Stopped.");
    }

    private void pause() {
        if (timer == null) {
            return;
        }
        timer.stop();
        GLog.info("Paused.");
    }
}
