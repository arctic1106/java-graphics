import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    private static final Dimension WINDOW_DIMENSIONS = new Dimension(900, 700);
    private static final Dimension MINIMUM_DIMENSIONS = new Dimension(200, 900);
    private Map<String, String> displayText;
    private final JPanel columns;
    private final JPanel topColumns;
    private Point mouseOffset;
    private Pile tempPile;
    private Engine engine;
    private final MouseListenerImpl mouseListenerImpl;

    public GUI(Engine engine) {
        mouseListenerImpl = new MouseListenerImpl();
        this.engine = engine;
        createTextMap();
        setTitle("Solitaire");
        setSize(WINDOW_DIMENSIONS);
        setContentPane((new JPanel()));
        setLayout(new BorderLayout());
        var gameArea = new JPanel();
        gameArea.setOpaque(false);
        gameArea.setLayout(new BoxLayout(gameArea, BoxLayout.PAGE_AXIS));
        // Center the window
        setLocationRelativeTo(null);
        // Window close event
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Add GUI elements
        createTopMenu();
        // Flow layout to display multiple columns on the same row
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
        flow.setAlignOnBaseline(true);
        // Add the columns panel
        columns = new JPanel();
        columns.setOpaque(false);
        columns.setLayout(flow);
        columns.setMinimumSize(MINIMUM_DIMENSIONS);
        // Add the top columns panel
        FlowLayout topFlow = new FlowLayout(FlowLayout.LEFT);
        topFlow.setAlignOnBaseline(true);
        topColumns = new JPanel();
        topColumns.setOpaque(false);
        topColumns.setLayout(topFlow);
        gameArea.add(topColumns);
        gameArea.add(columns);
        add(gameArea);
        setVisible(true);
        // Auxiliarry elements
        mouseOffset = new Point(0, 0);
        initialize();
    }

    private void initialize() {
        topColumns.removeAll();
        columns.removeAll();
        // Add a listener for each card
        engine.setupGame(this);
        for (var pile : engine.getMainPiles()) {
            columns.add(pile);
        }
        topColumns.add(engine.getDrawPile());
        topColumns.add(engine.getPile());
        for (var pile : engine.finalPiles()) {
            topColumns.add(pile);
        }
        validate();
    }

    public void reset() {
        this.engine = new Engine(new Deck());
        initialize();
        repaint();
    }

    private void createTextMap() {
        displayText = new HashMap<>();
        displayText.put("File", "File");
        displayText.put("New", "New");
        displayText.put("Save", "Save");
        displayText.put("Load", "Load");
        displayText.put("Exit", "Exit");
    }

    private void createTopMenu() {
        var FileMenu = new JMenu("File");
        FileMenu.setMnemonic(KeyEvent.VK_F);
        var menuBar = new JMenuBar();
        menuBar.add(FileMenu);
        MenuOption[] fileOptions = {
                new MenuOption(displayText.get("New"), KeyEvent.VK_N),
                new MenuOption(displayText.get("Save"), KeyEvent.VK_S),
                new MenuOption(displayText.get("Load"), KeyEvent.VK_L),
                new MenuOption(displayText.get("Exit"), KeyEvent.VK_X)
        };
        for (var option : fileOptions) {
            var menuitem = new JMenuItem(option.name());
            if (option.shorcut() != 0)
                menuitem.setMnemonic(option.shorcut());
            menuitem.addActionListener(this);
            FileMenu.add(menuitem);
        }
        setJMenuBar(menuBar);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseListenerImpl.mouseClicked(e, engine);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseListenerImpl.mousePressed(e, this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseListenerImpl.mouseReleased(e, this, engine);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseListenerImpl.mouseDragged(e, this);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public Point mouseOffset() {
        return mouseOffset;
    }

    public Pile tempPile() {
        return tempPile;
    }

    public void setTempPile(Pile pile) {
        tempPile = pile;
    }

    public void setMouseOffset(Point point) {
        mouseOffset = point;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem item)
            handleMenuInteraction(item);
    }

    private void handleMenuInteraction(JMenuItem item) {
        switch (item.getText()) {
            case "Exit" -> dispose();
            case "New" -> reset();
            case "Save" -> {
                new Persistence(engine).save();
                JOptionPane.showMessageDialog(this, "Game saved!");
            }
            case "Load" -> {
                new Persistence(engine).load();
                validate();
            }
        }
    }
}
