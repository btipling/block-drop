import blocks.Block;

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
        int numRows = (State.NUM_ROWS - State.DISPLAY_ROW_START);
        int rowSize = height/numRows;
        int containerDimension = columnSize;
        if (rowSize < columnSize) {
            containerDimension = rowSize;
        }
        int boxDimension = containerDimension - 2;
        int[][] boardState = state.getBoardState();
        g2d.setColor(new Color(148, 148, 148));
        g2d.fillRect(0, 0, width, height);
        for(int row = 0; row < numRows; row++) {
          int currentRow = row + State.DISPLAY_ROW_START;
          for (int col = 0; col < State.NUM_COLS; col++)  {
              int blockType = boardState[currentRow][col];
              if (blockType > 0) {
                  Block.BLOCK_TYPES[blockType].drawBlock(g2d, col, row, containerDimension, boxDimension);
              }
          }
        }
    }
}
