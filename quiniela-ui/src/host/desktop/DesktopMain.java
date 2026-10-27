package host.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import quiniela.QuinielaScreen;
import ui.Ui;

public class DesktopMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuinielaScreen screen = new QuinielaScreen();
            Ui gui = new Ui();
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics gr) {
                    Graphics2D g = (Graphics2D) gr.create();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                    gui.begin(new Java2DCanvas(g), getWidth(), getHeight());
                    screen.draw(gui);
                    gui.end();
                    g.dispose();
                }
            };
            MouseAdapter mouse = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    gui.pointerDown(e.getX(), e.getY());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    gui.pointerMove(e.getX(), e.getY());
                    panel.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    gui.pointerUp(e.getX(), e.getY());
                    panel.repaint();
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    gui.wheel((float) e.getPreciseWheelRotation() * 40);
                    panel.repaint();
                }
            };
            panel.addMouseListener(mouse);
            panel.addMouseMotionListener(mouse);
            panel.addMouseWheelListener(mouse);
            panel.setPreferredSize(new Dimension(390, 840));
            JFrame frame = new JFrame("Quiniela UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
