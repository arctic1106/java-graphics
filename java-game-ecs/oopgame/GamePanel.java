
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GamePanel - Renders the game. MVC: the View.
 */
public class GamePanel extends JPanel {

    private final Game game;
    private final GameLoop gameLoop;

    public GamePanel() {
        this.game = new Game();

        setPreferredSize(new Dimension(game.map.getPixelWidth(), game.map.getPixelHeight()));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener(game.getInput());
        addMouseMotionListener(game.getInput());

        gameLoop = new GameLoop(this::update, this::repaint);
        gameLoop.start();
    }

    private void update() {
        game.update(GameLoop.FIXED_DT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        game.map.render(g2d);

        for (Unit unit : game.getUnits()) {
            unit.render(g2d);
        }

        // Selection box
        InputHandler input = game.getInput();
        if (input.isDragging()) {
            int x = Math.min(input.getDragStartX(), input.getMouseX());
            int y = Math.min(input.getDragStartY(), input.getMouseY());
            int w = Math.abs(input.getMouseX() - input.getDragStartX());
            int h = Math.abs(input.getMouseY() - input.getDragStartY());
            g2d.setColor(new Color(0, 255, 0, 50));
            g2d.fillRect(x, y, w, h);
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(x, y, w, h);
        }

        // HUD
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(5, 5, 200, 50);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("FPS: " + gameLoop.getFps() + " | UPS: " + gameLoop.getUps(), 10, 20);
        g2d.drawString("Units: " + game.getUnits().size(), 10, 35);
        g2d.drawString("Left-click: Select | Right-click: Move/Attack", 10, 50);

        // Selected info
        List<Unit> selected = game.getSelected();
        if (!selected.isEmpty()) {
            int panelY = getHeight() - 80;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(5, panelY, 250, 75);
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Selected: " + selected.size() + " units", 10, panelY + 15);
            if (selected.size() == 1) {
                Unit u = selected.get(0);
                g2d.setColor(Color.WHITE);
                g2d.drawString("Type: " + u.type.name(), 10, panelY + 30);
                g2d.drawString(String.format("HP: %.0f/%.0f", u.health, u.maxHealth), 10, panelY + 45);
                g2d.drawString(String.format("DMG: %.0f | SPD: %.1f", u.type.attackDamage, u.type.moveSpeed), 10, panelY + 60);
            }
        }
    }
}
