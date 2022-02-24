import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class TimerPanel extends JPanel {
    private final JLabel timePassedLabel;

    public TimerPanel() {
        setLayout(new BorderLayout(10, 0));
        timePassedLabel = new JLabel("  0  ", SwingConstants.CENTER);
        timePassedLabel.setFont(new Font("Serif", Font.BOLD, 20));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        timePassedLabel.setBorder(loweredetched);
        timePassedLabel.setBackground(new Color(110, 110, 255));
        timePassedLabel.setForeground(Color.white);
        timePassedLabel.setOpaque(true);
        JLabel iT = new JLabel("", SwingConstants.CENTER);
        iT.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("clock.png"))));
        add(iT, BorderLayout.WEST);
        add(timePassedLabel, BorderLayout.CENTER);
        setOpaque(false);
    }

    public JLabel getTimePassedLabel() {
        return timePassedLabel;
    }
}
