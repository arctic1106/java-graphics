import javax.swing.*;

public class Celda {
    private static final String EMPTY_ICON = "empty.png";
    private static final String RED_ICON = "red.png";
    private static final String BLACK_ICON = "black.png";

    private final JLabel jLabel;
    private final Tipo tipo;

    public Celda(Tipo tipo) {
        this.jLabel = new JLabel(tipo.getImage());
        this.tipo = tipo;
    }

    public JLabel getComponent() {
        return jLabel;
    }

    public boolean esVacia() {
        return tipo == Tipo.VACIA;
    }

    public enum Tipo {
        VACIA(new JLabel(new ImageIcon(EMPTY_ICON))),
        ROJA(new JLabel(new ImageIcon(RED_ICON))),
        NEGRA(new JLabel(new ImageIcon(BLACK_ICON)));

        private final JLabel imagen;

        Tipo(JLabel jLabel) {
            this.imagen = jLabel;
        }

        Icon getImage() {
            return imagen.getIcon();
        }
    }
}
