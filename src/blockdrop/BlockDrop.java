package blockdrop;

import blockdrop.utils.GLog;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.prefs.Preferences;

public class BlockDrop {

    public static void main(String[] args) {
        GLog.info("Starting game");
        SwingUtilities.invokeLater(BlockDrop::run);
        new JFXPanel(); //This does some kind of initialization needed to play mp3s.
    }

    private static void run() {
        Preferences prefs = Preferences.userRoot().node("BlockDrop");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        URL url = BlockDrop.class.getResource("../fonts/Action_Man_Bold.ttf");
        File file = new File(url.getPath());
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (Exception e) {
            GLog.info("Couldn't load font. %s", e);
            return;
        }
        Board board = new Board(prefs, customFont);
        board.showBoard();
    }

}
