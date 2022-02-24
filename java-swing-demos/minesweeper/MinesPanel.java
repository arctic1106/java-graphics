import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class MinesPanel extends JPanel {
    private final JLabel minesLabel;

    public MinesPanel() {
        setLayout(new BorderLayout(10, 0));
        minesLabel = new JLabel("  0  ", SwingConstants.CENTER);
        minesLabel.setFont(new Font("Serif", Font.BOLD, 20));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        minesLabel.setBorder(loweredetched);
        minesLabel.setBackground(new Color(110, 110, 255));
        minesLabel.setForeground(Color.white);
        minesLabel.setOpaque(true);
        JLabel mT = new JLabel("", SwingConstants.CENTER);
        mT.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("mine.png"))));
        add(minesLabel, BorderLayout.WEST);
        add(mT, BorderLayout.CENTER);
        setOpaque(false);
    }

    public JLabel getMinesLabel() {
        return minesLabel;
    }
}
