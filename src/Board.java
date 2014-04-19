import javax.swing.*;
import java.awt.*;

/**
 * Created by bjorn on 4/19/14.
 */
public class Board {
    private JButton stopButton;
    private JButton startButton;
    private JButton pauseButton;
    private JPanel gameBoard;
    private JPanel controlPanel;
    private JLabel currentScore;
    private JLabel topScore;
    private JPanel nextPiecePanel;
    private JPanel contentPanel;

    public void showBoard () {
        JFrame frame = new JFrame("Drop Block");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(contentPanel);
        frame.setMinimumSize(new Dimension(750, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
