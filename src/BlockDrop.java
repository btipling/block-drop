import javax.swing.*;

public class BlockDrop {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SwingUtilities.invokeLater(BlockDrop::run);
    }

    private static void run() {
        Board board = new Board();
        board.showBoard();
    }

}
