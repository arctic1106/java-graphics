package host.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import game.Game;

public class DesktopMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics gr) {
                    Graphics2D g = (Graphics2D) gr.create();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    game.frame(System.nanoTime());
                    game.render(new Java2DCanvas(g), getWidth(), getHeight());
                    g.dispose();
                }
            };
            MouseAdapter mouse = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) game.pointerDown(e.getX(), e.getY());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    game.pointerMove(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) game.pointerUp(e.getX(), e.getY());
                    else if (e.getButton() == MouseEvent.BUTTON3) game.command(e.getX(), e.getY());
                }
            };
            panel.addMouseListener(mouse);
            panel.addMouseMotionListener(mouse);
            panel.setPreferredSize(new Dimension(800, 600));
            new Timer(8, e -> panel.repaint()).start();

            JFrame frame = new JFrame("ECS Portable");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
