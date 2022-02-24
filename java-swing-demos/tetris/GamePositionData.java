import java.awt.Color;

public final class GamePositionData {
    private final int columnas;
    private final int filas;
    private final TipoPieza[] tablaPiezas;

    public GamePositionData(int ancho, int alto) {
        columnas = ancho;
        filas = alto;
        tablaPiezas = new TipoPieza[filas * columnas];
        for (var i = 0; i < filas * columnas; i++) {
            tablaPiezas[i] = TipoPieza.NoShape;
        }
    }

    private int calcOffset(int x, int y) {
        return y * columnas + x;
    }

    public TipoPieza tipoPiezaAt(int x, int y) {
        return tablaPiezas[calcOffset(x, y)];
    }

    public void setTipoPiezaAt(int x, int y, TipoPieza tipoPieza) {
        tablaPiezas[calcOffset(x, y)] = tipoPieza;
    }

    public Color colorAt(int x, int y) {
        return PiezaStaticData.getColor(tipoPiezaAt(x, y));
    }

    public boolean hayPiezaEn(int i, int j) {
        return tipoPiezaAt(i, j) != TipoPieza.NoShape;
    }
}
