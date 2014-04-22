import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {

    interface GameKeyListener {
        public void gameKeyTriggered(KeyEvent e);
    }

    private List<GameKeyListener> listeners = new ArrayList<>();

    private class CustomKeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                gameKeyPressed(e);
            }
            return false;
        }
    }

    public void addGameKeyListener(GameKeyListener gameKeyListener) {
        listeners.add(gameKeyListener);
    }

    public GameFrame() {
        super("Drop Block");
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomKeyDispatcher());
    }

    private void gameKeyPressed(KeyEvent e) {
        for (GameKeyListener listener : listeners) {
            listener.gameKeyTriggered(e);
        }
    }
}
