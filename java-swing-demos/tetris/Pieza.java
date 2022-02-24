import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public final class Pieza {
    private final TipoPieza tipoPieza;
    private final TileData tileData;
    private final Color color;
    private Point posicionActual;

    private Pieza(TipoPieza tipo, TileData data, Point pos) {
        tipoPieza = tipo;
        tileData = data;
        color = PiezaStaticData.getColor(tipo);
        posicionActual = pos;
    }

    public Pieza(TipoPieza tipo) {
        tipoPieza = tipo;
        tileData = new TileData(tipo);
        color = PiezaStaticData.getColor(tipo);
        posicionActual = new Point(0, 0);
    }

    public static Pieza newEmpty() {
        var pieza = new Pieza(TipoPieza.NoShape);
        return pieza;
    }

    public static Pieza newRandom(int posX, int posY) {
        var x = new Random().nextInt(1, 8);
        var pieza = new Pieza(TipoPieza.values()[x]);
        pieza.posicionActual = new Point(posX, posY);
        return pieza;
    }

    public void paint(Graphics g, int curX, int curY, int tileWidth, int boardTop, int boardHeight, int tileHeight) {
        if (getShape() != TipoPieza.NoShape) {
            for (var i = 0; i < 4; i++) {
                var x = curX + getX(i);
                var y = curY - getY(i);
                drawSquare(g, x * tileWidth, boardTop + (boardHeight - y - 1) * tileHeight, tileHeight, getColor());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, int tileHeight, Color color) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, tileHeight - 2, tileHeight - 2);
    }

    public TipoPieza getShape() {
        return tipoPieza;
    }

    public Color getColor() {
        return color;
    }

    public Pieza rotateLeft() {
        if (tipoPieza == TipoPieza.SquareShape) {
            return this;
        }
        return new Pieza(tipoPieza, tileData.rotateLeft(), posicionActual);
    }

    public Pieza rotateRight() {
        if (tipoPieza == TipoPieza.SquareShape) {
            return this;
        }
        return new Pieza(tipoPieza, tileData.rotateRight(), posicionActual);
    }

    public int getX(int index) {
        return tileData.getPoint(index).x;
    }

    public int getY(int index) {
        return tileData.getPoint(index).y;
    }

    public int minY() {
        return tileData.minY();
    }

    public String getCoordString() {
        return tileData.getCoordString();
    }

    public Point getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(Point posicionActual) {
        this.posicionActual = posicionActual;
    }
}
