import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tablero extends JPanel {
    private static final int IMG_SIZE = 50;
    private static final int COL_SIZE = 6;
    private static final int COLS = 7;
    private final List<Columna> columnas;
    private final boolean finJuego = false;
    private String colorTurno = "negro";

    private static class Columna implements Iterable<Celda> {
        private final List<Celda> columna = new ArrayList<>();

        public Columna() {
            for (var i = 0; i < COL_SIZE; i++) {
                columna.add(new Celda(Celda.Tipo.VACIA));
            }
        }

        @Override
        public Iterator<Celda> iterator() {
            return columna.iterator();
        }
    }

    public Tablero() {
        columnas = new ArrayList<>();
        setLayout(new GridLayout(COL_SIZE, COLS));
        setOpaque(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!finJuego) {
                    if (e.getY() < IMG_SIZE * COL_SIZE && e.getX() < IMG_SIZE * COLS) {
                        System.out.println("X: " + e.getX() + " Y: " + e.getY());
                        ponerFichaEnColumna(e.getX() / IMG_SIZE, colorTurno);
                        colorTurno = "rojo";
                    }
                }
            }
        });
        initColumnas();
        addComponentes();
    }

    private void initColumnas() {
        for (int i = 0; i < COLS; i++) {
            columnas.add(new Columna());
        }
    }

    private void addComponentes() {
        for (Columna col : columnas) {
            for (Celda c : col) {
                add(c.getComponent());
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("This is my custom Panel!", 100, 200);
    }

    private void ponerFichaEnColumna(int colNumber, String color) {
        var columna = columnas.get(colNumber);
        for (var celda : columna) {
            if (celda.esVacia()) {
                switch (color) {
                    case "rojo" -> celda = new Celda(Celda.Tipo.ROJA);
                    case "negro" -> celda = new Celda(Celda.Tipo.NEGRA);
                }
            }
        }
    }
}
