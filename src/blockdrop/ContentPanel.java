package blockdrop;

import java.awt.*;

public class ContentPanel extends DrawPanel {
    @Override
    protected void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        int width = new Double(size.getWidth()).intValue();
        int height = new Double(size.getHeight()).intValue();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
    }
}
