import javax.swing.*;

public class AppSpace extends JFrame {

    public static void main(String[] args) {
        var frame = new JFrame("Space Shooter");
        var spaceBoard = new SpaceBoard();
        frame.add(spaceBoard);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
