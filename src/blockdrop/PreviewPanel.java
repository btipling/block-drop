package blockdrop;

import blockdrop.blocks.Block;

import java.awt.*;

public class PreviewPanel extends DrawPanel {

    Block nextBlock;
    private final int NUM_COLS = 6;

    @Override
    protected void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        int width = new Double(size.getWidth()).intValue();
        int columnSize = width/NUM_COLS;
        int height = new Double(size.getHeight()).intValue();
        g2d.setColor(new Color(255, 255, 255));
        g2d.fillRect(0, 0, width, height);
        if (nextBlock == null) {
            return;
        }
        int[][] rotation = nextBlock.getDefaultRotation();
        for (int i = 0; i < rotation.length; i++) {
            for (int j = 0; j < rotation[i].length; j++) {
                if (rotation[i][j] == 0) {
                    continue;
                }
                nextBlock.drawBlock(g2d, j + 1, i + 1, columnSize, columnSize - 2);
            }
        }
    }

    public void drawNextBlock(Block block) {
        nextBlock = block;
        repaint();
    }
}
