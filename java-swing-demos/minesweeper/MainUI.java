import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainUI extends JFrame {
    private final ButtonGrid buttonGrid;
    private final TimerPanel timerPanel;
    private final MinesPanel minesPanel;
    private final MenuBarPanel menuBarPanel;

    public MainUI(int rows, int cols, int mines) {
        this.buttonGrid = new ButtonGrid(rows, cols);
        this.timerPanel = new TimerPanel();
        this.minesPanel = new MinesPanel();
        this.menuBarPanel = new MenuBarPanel();

        // Set up frame
        setSize(520, 550);
        setTitle("Minesweeper");
        setLocation(430, 50);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Set up content pane
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader().getResource("2.jpg")));
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(new BorderLayout());

        // Add components to content pane
        contentPane.add(menuBarPanel, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 14, 60));
        gamePanel.setOpaque(false);
        gamePanel.add(buttonGrid, BorderLayout.CENTER);
        JPanel tmPanel = new JPanel(new BorderLayout(0, 20));
        tmPanel.setOpaque(false);
        tmPanel.add(timerPanel, BorderLayout.WEST);
        tmPanel.add(minesPanel, BorderLayout.EAST);
        gamePanel.add(tmPanel, BorderLayout.SOUTH);
        contentPane.add(gamePanel, BorderLayout.CENTER);

        setContentPane(contentPane);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("mine.png")));

        pack();
    }

    // Getter methods for components
    public ButtonGrid getButtonGrid() {
        return buttonGrid;
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public MinesPanel getMinesPanel() {
        return minesPanel;
    }

    public MenuBarPanel getMenuBarPanel() {
        return menuBarPanel;
    }

    // Other methods remain unchanged as per the original UI class
    // Methods for starting/stopping timer, setting icons, etc.
}
