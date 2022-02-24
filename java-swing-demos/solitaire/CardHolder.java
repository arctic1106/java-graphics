import javax.swing.*;
import java.awt.*;

public class CardHolder extends JPanel {

    public CardHolder() {
        setSize(Card.DIMENSIONES_CARTA);
        setBackground(Color.LIGHT_GRAY);
    }

    public String toString() {
        return "cardHolder";
    }

    public String saveAsString() {
        return "cardHolder True";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
