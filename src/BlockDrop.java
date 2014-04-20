import dropblock.utils.GLog;

import javax.swing.*;

public class BlockDrop {

    public static void main(String[] args) {
        GLog.info("Starting game");
        SwingUtilities.invokeLater(BlockDrop::run);
    }

    private static void run() {
        Board board = new Board();
        board.showBoard();
    }

}
