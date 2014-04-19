import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by bjorn on 4/19/14.
 */
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

    public void showBoard () {
        JFrame frame = new JFrame("Drop Block");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.setMinimumSize(new Dimension(520, 600));
        frame.setLocationRelativeTo(null);
        frame.pack();
        controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setVisible(true);
    }

    private void createUIComponents() {
        gamePanel = new GamePanel();
    }
}
