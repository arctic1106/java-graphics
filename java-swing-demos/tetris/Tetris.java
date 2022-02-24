import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

public final class Tetris extends JFrame {

    private Tetris() {
        var statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        var board = new TetrisBoard(this, statusbar);
        add(board);
        setTitle("Tetris");
        setSize(420, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        board.start(200);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Tetris::new);
    }
}
