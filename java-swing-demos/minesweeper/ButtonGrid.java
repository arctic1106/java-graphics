import javax.swing.*;
import java.awt.*;

public class ButtonGrid extends JPanel {
    private final JButton[][] buttons;

    public ButtonGrid(int rows, int cols) {
        buttons = new JButton[rows][cols];
        setLayout(new GridLayout(rows, cols, 0, 0));
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                buttons[x][y] = new JButton("");
                buttons[x][y].setName(Integer.toString(x) + "," + Integer.toString(y));
                buttons[x][y].setFont(new Font("Serif", Font.BOLD, 24));
                buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                add(buttons[x][y]);
            }
        }
    }

    public JButton[][] getButtons() {
        return buttons;
    }
}
