import javax.swing.*;
import java.awt.*;

public abstract class DrawPanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    protected abstract void doDrawing(Graphics g);

}
