import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private State state;

    public GamePanel(State state) {
        this.state = state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        int width = new Double(size.getWidth()).intValue();
        int columnSize = width/State.NUM_COLS;
        int height = new Double(size.getHeight()).intValue();
        int rowSize = height/State.NUM_ROWS;
        int containerDimension = columnSize;
        if (rowSize < columnSize) {
            containerDimension = rowSize;
        }
        int boxDimension = containerDimension - 2;
        int[][] boardState = state.getBoardState();
        for(int row = 0; row < State.NUM_ROWS; row++) {
          for (int col = 0; col < State.NUM_COLS; col++)  {
              if (boardState[row][col] > 0) {
                  g2d.setColor(new Color(128, 13, 21));
              } else {
                  g2d.setColor(new Color(148, 148, 148));
              }
              g2d.fillRect(col * containerDimension + 1, row * containerDimension + 1, boxDimension, boxDimension);
          }
        }
    }
}