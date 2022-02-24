import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.EventListener;
import java.util.Objects;

public class Card extends JPanel {
    public static final Dimension DIMENSIONES_CARTA = new Dimension(100, 145);
    private final int value;
    private final Suit suit;
    private final BufferedImage image;
    private boolean mostrar = true;
    private Pile containingPile;

    public Card(Suit suit, int value) {
        this.value = value;
        this.suit = suit;

        try (var stream = getClass().getClassLoader().getResourceAsStream("cards/" + cardFileName() + ".png")) {
            image = ImageIO.read(Objects.requireNonNull(stream));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        setSize(DIMENSIONES_CARTA);
    }

    public void ocultar() {
        mostrar = false;
    }

    public void mostrar() {
        mostrar = true;
    }

    public boolean mostrada() {
        return mostrar;
    }

    public int value() {
        return value;
    }

    public Suit suit() {
        return suit;
    }

    public Suit.Color color() {
        return suit().color();
    }

    @Override
    public String toString() {
        return valueString(value) + " of " + suit.name().toLowerCase();
    }

    private String cardFileName() {
        return suit.name() + value;
    }

    private static String valueString(int value) {
        return switch (value) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> Integer.toString(value);
        };
    }

    public static int valueInt(String value) {
        return switch (value) {
            case "A" -> 1;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            default -> Integer.parseInt(value);
        };
    }

    public String saveAsString() {
        return valueString(value) + " of " + suit.name() + " of " + mostrar;
    }

    public void addMouseListeners(EventListener listenersImpl) {
        this.addMouseListener((MouseListener) listenersImpl);
        this.addMouseMotionListener(((MouseMotionListener) listenersImpl));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mostrada()) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            setBorder(null);
        } else {
            setBackground(Color.GRAY);
            setBorder(new LineBorder(Color.BLACK, 2, true));
        }
    }

    public Pile containingPile() {
        return containingPile;
    }

    public void setContainingPile(Pile pile) {
        containingPile = pile;
    }
}
