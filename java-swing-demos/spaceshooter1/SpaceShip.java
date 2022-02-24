import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class SpaceShip {
    public int dx;
    public int dy;
    public int x = 40;
    public int y = 60;
    public final int width;
    public final int height;
    public final Image image;

    public SpaceShip() {
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("star.png")))
                .getImage()
                .getScaledInstance(15, 15, Image.SCALE_DEFAULT);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> dx = -2;
            case KeyEvent.VK_RIGHT -> dx = 2;
            case KeyEvent.VK_UP -> dy = -2;
            case KeyEvent.VK_DOWN -> dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> dx = 0;
            case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> dy = 0;
        }
    }
}
