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
    private List<GameKeyListener> keyDownListeners = new ArrayList<>();
    private List<GameKeyListener> keyUpListeners = new ArrayList<>();

    private class CustomKeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                gameKeyPressed(e);
                gameKeyDown(e);
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                gameKeyUp(e);
            }
            return false;
        }
    }

    public void addGameKeyListener(GameKeyListener gameKeyListener) {
        listeners.add(gameKeyListener);
    }

    public void addGameKeyDownListener(GameKeyListener gameKeyListener) {
        keyDownListeners.add(gameKeyListener);
    }

    public void addGameKeyUpListener(GameKeyListener gameKeyListener) {
        keyUpListeners.add(gameKeyListener);
    }

    public GameFrame() {
        super("BlockDrop");
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomKeyDispatcher());
    }

    private void gameKeyPressed(KeyEvent e) {
        for (GameKeyListener listener : listeners) {
            listener.gameKeyTriggered(e);
        }
    }

    private void gameKeyDown(KeyEvent e) {
        for (GameKeyListener listener : keyDownListeners) {
            listener.gameKeyTriggered(e);
        }
    }

    private void gameKeyUp(KeyEvent e) {
        for (GameKeyListener listener : keyUpListeners) {
            listener.gameKeyTriggered(e);
        }
    }
}
