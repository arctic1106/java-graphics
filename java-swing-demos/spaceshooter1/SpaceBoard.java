import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpaceBoard extends JPanel implements ActionListener {
    private final SpaceShip spaceShip;
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;

    public SpaceBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        spaceShip = new SpaceShip();
        Timer timer = new Timer(40, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).drawImage(spaceShip.image, spaceShip.x, spaceShip.y, this);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateSpaceShipPosition();
        repaint();
    }

    private void updateSpaceShipPosition() {
        var x = spaceShip.x;
        var y = spaceShip.y;

        if (x < 0)
            spaceShip.x = 0;
        else if (x + spaceShip.width > BOARD_WIDTH)
            spaceShip.x = BOARD_WIDTH - spaceShip.width;

        if (y < 0)
            spaceShip.y = 0;
        else if (y + spaceShip.height > BOARD_HEIGHT)
            spaceShip.y = BOARD_HEIGHT - spaceShip.height;

        spaceShip.move();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            spaceShip.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Q)
                System.exit(0);
            spaceShip.keyPressed(e);
        }
    }
}
