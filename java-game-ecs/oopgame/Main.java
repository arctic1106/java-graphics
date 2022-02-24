
import javax.swing.*;

/**
 * Main - Entry point for the OOP RTS.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel panel = new GamePanel();
            JFrame frame = new JFrame("RTS Game - OOP Version");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("=== OOP RTS Game ===");
            System.out.println("Same game, fewer files. Behavior lives IN the unit.");
            System.out.println("Left-click: Select | Right-click: Move/Attack");
        });
    }
}
