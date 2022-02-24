import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class UI extends JFrame {
    private final JButton[][] buttons;
    private final int rows;
    private final int cols;
    private final JLabel minesLabel;
    private final JLabel timePassedLabel;
    private final JMenuItem newGame;
    private final JMenuItem statistics;
    private final JMenuItem exit;
    private final ClassLoader classLoader = getClass().getClassLoader();
    private int mines;
    private Thread timer;
    private int timePassed;
    private boolean stopTimer;
    private Icon redMine;
    private Icon mine;
    private Icon flag;
    private Icon tile;

    public UI(int r, int c, int m) {
        this.rows = r;
        this.cols = c;
        buttons = new JButton[rows][cols];
        // Set frame
        int FRAME_WIDTH = 520;
        int FRAME_HEIGHT = 550;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        // Frame settings
        String FRAME_TITLE = "Minesweeper";
        setTitle(FRAME_TITLE);
        int FRAME_LOC_X = 430;
        int FRAME_LOC_Y = 50;
        setLocation(FRAME_LOC_X, FRAME_LOC_Y);
        JPanel gameBoard;
        JPanel tmPanel;
        gameBoard = new JPanel();
        gameBoard.setLayout(new GridLayout(rows, cols, 0, 0));
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                buttons[x][y] = new JButton("");
                buttons[x][y].setName(Integer.toString(x) + "," + Integer.toString(y));
                buttons[x][y].setFont(new Font("Serif", Font.BOLD, 24));
                buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                gameBoard.add(buttons[x][y]);
            }
        }
        JPanel timePassedPanel = new JPanel();
        timePassedPanel.setLayout(new BorderLayout(10, 0));
        this.timePassedLabel = new JLabel("  0  ", SwingConstants.CENTER);
        timePassedLabel.setFont(new Font("Serif", Font.BOLD, 20));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        timePassedLabel.setBorder(loweredetched);
        timePassedLabel.setBackground(new Color(110, 110, 255));
        timePassedLabel.setForeground(Color.white);
        timePassedLabel.setOpaque(true);
        JLabel iT = new JLabel("", SwingConstants.CENTER);
        iT.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("clock.png"))));
        timePassedPanel.add(iT, BorderLayout.WEST);
        timePassedPanel.add(timePassedLabel, BorderLayout.CENTER);
        timePassedPanel.setOpaque(false);
        this.timePassed = 0;
        this.stopTimer = true;
        JPanel minesPanel = new JPanel();
        minesPanel.setLayout(new BorderLayout(10, 0));
        // Initialize mines label.
        this.minesLabel = new JLabel("  0  ", SwingConstants.CENTER);
        minesLabel.setFont(new Font("Serif", Font.BOLD, 20));
        minesLabel.setBorder(loweredetched);
        minesLabel.setBackground(new Color(110, 110, 255));
        minesLabel.setForeground(Color.white);
        minesLabel.setOpaque(true);
        setMines(m);
        JLabel mT = new JLabel("", SwingConstants.CENTER);
        mT.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("mine.png"))));
        minesPanel.add(minesLabel, BorderLayout.WEST);
        minesPanel.add(mT, BorderLayout.CENTER);
        minesPanel.setOpaque(false);
        tmPanel = new JPanel();
        tmPanel.setLayout(new BorderLayout(0, 20));
        tmPanel.add(timePassedPanel, BorderLayout.WEST);
        tmPanel.add(minesPanel, BorderLayout.EAST);
        tmPanel.setOpaque(false);
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        newGame = new JMenuItem("   New Game");
        statistics = new JMenuItem("   Statistics");
        exit = new JMenuItem("   Exit");
        newGame.setName("New Game");
        statistics.setName("Statistics");
        exit.setName("Exit");
        gameMenu.add(newGame);
        gameMenu.add(statistics);
        gameMenu.add(exit);
        menuBar.add(gameMenu);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(0, 10));
        p.add(gameBoard, BorderLayout.CENTER);
        p.add(tmPanel, BorderLayout.SOUTH);
        p.setBorder(BorderFactory.createEmptyBorder(60, 60, 14, 60));
        p.setOpaque(false);
        setLayout(new BorderLayout());
        JLabel background = new JLabel(new ImageIcon(Objects.requireNonNull(classLoader.getResource("2.jpg"))));
        add(background);
        background.setLayout(new BorderLayout(0, 0));
        background.add(menuBar, BorderLayout.NORTH);
        background.add(p, BorderLayout.CENTER);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(classLoader.getResource("mine.png")));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        return new ImageIcon(
                icon.getImage().getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH));
    }

    public void initGame() {
        hideAll();
        enableAll();
    }

    public void startTimer() {
        stopTimer = false;
        timer = new Thread(() -> {
            while (!stopTimer) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                timePassed++;
                timePassedLabel.setText("  " + timePassed + "  ");
            }
        });
        timer.start();
    }

    public void interruptTimer() {
        stopTimer = true;
        try {
            if (timer != null)
                timer.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void resetTimer() {
        timePassed = 0;
        timePassedLabel.setText("  " + timePassed + "  ");
    }

    public void enableAll() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].setEnabled(true);
            }
        }
    }

    public void hideAll() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].setText("");
                buttons[x][y].setBackground(new Color(0, 103, 200));
                buttons[x][y].setIcon(tile);
            }
        }
    }

    public void setButtonListeners(GameController listeners) {
        addWindowListener(listeners);
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].addMouseListener(listeners);
            }
        }
        newGame.addActionListener(listeners);
        statistics.addActionListener(listeners);
        exit.addActionListener(listeners);
        newGame.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        exit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        statistics.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public int getTimePassed() {
        return timePassed;
    }

    public void incMines() {
        mines++;
        setMines(mines);
    }

    public void decMines() {
        mines--;
        setMines(mines);
    }

    public void setMines(int m) {
        mines = m;
        minesLabel.setText("  " + m + "  ");
    }

    public void setIcons() {
        int bOffset = buttons[0][1].getInsets().left;
        int bWidth = buttons[0][1].getWidth();
        int bHeight = buttons[0][1].getHeight();
        var icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("redmine.png")));
        redMine = resizeIcon(icon, bWidth - bOffset, bHeight - bOffset);
        icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("mine.png")));
        mine = resizeIcon(icon, bWidth - bOffset, bHeight - bOffset);
        icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("flag.png")));
        flag = resizeIcon(icon, bWidth - bOffset, bHeight - bOffset);
        icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("tile.png")));
        tile = resizeIcon(icon, bWidth - bOffset, bHeight - bOffset);
    }

    public Icon getIconMine() {
        return mine;
    }

    public Icon getIconRedMine() {
        return redMine;
    }

    public Icon getIconFlag() {
        return flag;
    }

    public Icon getIconTile() {
        return tile;
    }

    public void setTextColor(JButton b) {
        switch (b.getText()) {
            case "1" -> b.setForeground(Color.blue);
            case "2" -> b.setForeground(new Color(76, 153, 0));
            case "3" -> b.setForeground(Color.red);
            case "4" -> b.setForeground(new Color(153, 0, 0));
            case "5" -> b.setForeground(new Color(153, 0, 153));
            case "6" -> b.setForeground(new Color(96, 96, 96));
            case "7" -> b.setForeground(new Color(0, 0, 102));
            case "8" -> b.setForeground(new Color(153, 0, 76));
        }
    }
}
