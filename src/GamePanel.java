import blocks.Block;

import java.awt.*;

public class GamePanel extends DrawPanel {

    private State state;

    public GamePanel(State state) {
        this.state = state;
    }

    @Override
    protected void doDrawing(Graphics g) {
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
        int level = state.getLevel();
        int index = (level % (Block.BLOCK_TYPES.length - 1)) + 1;
        Block bgColorBlock = Block.BLOCK_TYPES[index];
        Color bgColor = bgColorBlock.getColor();
        float[] hsb = bgColor.RGBtoHSB(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), null);
        hsb[2] = 1.0f;
        hsb[1] = 0.15f;
        bgColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        Color white = new Color(255, 255, 255, 127);
        GradientPaint gradientPaint = new GradientPaint(0, width, bgColor, 0, 1, white);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);
        for(int row = 0; row < numRows; row++) {
          int currentRow = row + State.DISPLAY_ROW_START;
          for (int col = 0; col < State.NUM_COLS; col++)  {
              int blockType = boardState[currentRow][col];
              if (blockType > 0) {
                  Block block = Block.getBlockForType(blockType);
                  block.drawBlock(g2d, col, row, containerDimension, boxDimension);
              }
          }
        }
    }
}
