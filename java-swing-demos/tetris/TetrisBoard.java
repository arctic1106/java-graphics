import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class TetrisBoard extends JPanel {
    private static final int BOARD_WIDTH = 15; // Ancho pantalla en tiles
    private static final int BOARD_HEIGHT = 20; // Alto pantalla en tiles
    private static int TILE_WIDTH; // Ancho del tile en píxeles
    private static int TILE_HEIGHT; // Alto del tile en píxeles
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0; // Puntuación (líneas completadas)
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Pieza piezaActiva;
    private final GamePositionData gamePositionData; // Tabla con el tipo de pieza en cada pixel?

    public TetrisBoard(Tetris parent, JLabel statusbar) {
        gamePositionData = new GamePositionData(BOARD_WIDTH, BOARD_HEIGHT);
        this.statusbar = statusbar;
        setFocusable(true);
        piezaActiva = Pieza.newEmpty();
        newPiece();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (piezaActiva.getShape() == TipoPieza.NoShape) {
                    return;
                }
                var keycode = e.getKeyCode();
                switch (keycode) {
                    case KeyEvent.VK_P -> pause();
                    case KeyEvent.VK_LEFT -> tryMove(piezaActiva, curX - 1, curY);
                    case KeyEvent.VK_RIGHT -> tryMove(piezaActiva, curX + 1, curY);
                    case KeyEvent.VK_DOWN -> tryMove(piezaActiva.rotateRight(), curX, curY);
                    case KeyEvent.VK_UP -> tryMove(piezaActiva.rotateLeft(), curX, curY);
                    case KeyEvent.VK_SPACE -> dropDown();
                    case KeyEvent.VK_D -> oneLineDown();
                }
            }
        });
    }

    void start(final int PERIOD_INTERVAL) {
        TILE_WIDTH = (int) getSize().getWidth() / BOARD_WIDTH;
        TILE_HEIGHT = (int) getSize().getHeight() / BOARD_HEIGHT;
        timer = new Timer(PERIOD_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusbar.setText("paused");
        } else {
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    private boolean tryMove(Pieza nuevaPieza, int newX, int newY) {
        for (var i = 0; i < 4; i++) {
            var x = newX + nuevaPieza.getX(i);
            var y = newY - nuevaPieza.getY(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            // detección de colisión
            if (gamePositionData.hayPiezaEn(x, y)) {
                return false;
            }
        }
        piezaActiva = nuevaPieza;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var boardTop = (int) getSize().getHeight() - BOARD_HEIGHT * TILE_HEIGHT;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (gamePositionData.hayPiezaEn(j, BOARD_HEIGHT - i - 1)) {
                    drawSquare(
                            g,
                            j * TILE_WIDTH, boardTop + i * TILE_HEIGHT, TILE_HEIGHT,
                            gamePositionData.colorAt(j, BOARD_HEIGHT - i - 1));
                }
            }
        }
        piezaActiva.paint(g, curX, curY, TILE_WIDTH, boardTop, BOARD_HEIGHT, TILE_HEIGHT);
    }

    private void drawSquare(Graphics g, int x, int y, int tileHeight, Color color) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, tileHeight - 2, tileHeight - 2);
    }

    private void dropDown() {
        var newY = curY;
        while (newY > 0) {
            if (!tryMove(piezaActiva, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(piezaActiva, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (var i = 0; i < 4; i++) {
            var x = curX + piezaActiva.getX(i);
            var y = curY - piezaActiva.getY(i);
            gamePositionData.setTipoPiezaAt(x, y, piezaActiva.getShape());
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        piezaActiva = Pieza.newRandom(BOARD_WIDTH / 2 + 1, BOARD_HEIGHT - 1 + piezaActiva.minY());
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + piezaActiva.minY();
        if (!tryMove(piezaActiva, curX, curY)) {
            piezaActiva = Pieza.newEmpty();
            timer.stop();
            statusbar.setText(String.format("Game over. Score: %d", numLinesRemoved));
        }
    }

    private void removeFullLines() {
        var numFullLines = 0;
        for (var i = BOARD_HEIGHT - 1; i >= 0; i--) {
            var lineIsFull = true;
            for (var j = 0; j < BOARD_WIDTH; j++) {
                if (gamePositionData.tipoPiezaAt(j, i) == TipoPieza.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (var k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (var j = 0; j < BOARD_WIDTH; j++) {
                        gamePositionData.setTipoPiezaAt(j, k, gamePositionData.tipoPiezaAt(j, k + 1));
                    }
                }
            }
        }
        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            piezaActiva = Pieza.newEmpty();
        }
    }
}
