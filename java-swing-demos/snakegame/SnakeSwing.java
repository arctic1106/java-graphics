import javax.swing.*;

public class SnakeSwing extends JFrame {

    private SnakeSwing() {
        this.add(new GamePanel());
        this.setTitle("SNAKE GAME");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new SnakeSwing();
    }
}
