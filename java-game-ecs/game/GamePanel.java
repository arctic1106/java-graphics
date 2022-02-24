package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import components.*;
import ecs.*;
import input.InputHandler;
import map.GameMap;
import systems.*;

/**
 * GamePanel - The rendering surface for the RTS.
 * 
 * ARCHITECTURE PATTERN: MVC separation
 * 
 * This panel:
 * - Receives input (via InputHandler)
 * - Triggers game logic updates
 * - Renders the current game state
 * 
 * It does NOT contain game logic — that's in the Systems.
 */
public class GamePanel extends JPanel implements ActionListener {
    
    private final World world;
    private final GameMap gameMap;
    private final InputHandler inputHandler;
    private final SelectionSystem selectionSystem;
    private final RenderSystem renderSystem;
    private final GameLoop gameLoop;
    
    // Selection box rendering
    private Rectangle selectionBox = null;
    
    public GamePanel() {
        // Create world and systems
        world = new World();
        gameMap = new GameMap(20, 15, 40); // 20x15 tiles, 40px each
        
        // Create systems
        selectionSystem = new SelectionSystem();
        renderSystem = new RenderSystem();
        
        // Add systems to world (order matters!)
        world.addSystem(selectionSystem);   // 1. Handle selection
        world.addSystem(new MovementSystem()); // 2. Move units
        world.addSystem(new CombatSystem(world)); // 3. Combat
        world.addSystem(new DeathSystem(world));  // 4. Remove dead
        
        // Create input handler
        inputHandler = new InputHandler(world, selectionSystem);
        
        // Setup panel
        setPreferredSize(new Dimension(
            gameMap.getPixelWidth(), 
            gameMap.getPixelHeight()
        ));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Add input listeners
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        
        // Spawn some test units
        spawnTestUnits();
        
        // Create and start game loop
        gameLoop = new GameLoop(this::update, this::repaint);
        gameLoop.start();
    }
    
    /**
     * Spawn test units for demonstration.
     */
    private void spawnTestUnits() {
        // Player's units (team 0) - blue
        createUnit(100, 100, UnitType.Type.SOLDIER, 0);
        createUnit(150, 120, UnitType.Type.SOLDIER, 0);
        createUnit(200, 100, UnitType.Type.ARCHER, 0);
        createUnit(120, 180, UnitType.Type.TANK, 0);
        
        // Enemy units (team 1) - red
        createUnit(600, 300, UnitType.Type.SOLDIER, 1);
        createUnit(650, 320, UnitType.Type.SOLDIER, 1);
        createUnit(700, 300, UnitType.Type.ARCHER, 1);
        createUnit(680, 380, UnitType.Type.TANK, 1);
    }
    
    /**
     * Create a unit entity with all required components.
     */
    private Entity createUnit(double x, double y, UnitType.Type type, int teamId) {
        Entity unit = world.createEntity();
        unit.addComponent(new Position(x, y))
            .addComponent(new Velocity(0, 0))
            .addComponent(new Health(type.baseHealth))
            .addComponent(new Selectable(type.size + 5))
            .addComponent(new UnitType(type))
            .addComponent(new Team(teamId));
        return unit;
    }
    
    /**
     * Game logic update. Called 60 times/second.
     */
    private void update() {
        world.update(GameLoop.FIXED_DT);
    }
    
    /**
     * Render the game. Called as fast as possible.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        // Draw map
        gameMap.render(g2d);
        
        // Draw units
        List<Entity> allEntities = world.getAllEntities();
        renderSystem.render(g2d, allEntities);
        
        // Draw selection box (if dragging)
        if (inputHandler.isDragging()) {
            drawSelectionBox(g2d);
        }
        
        // Draw UI
        drawUI(g2d);
    }
    
    /**
     * Draw the selection box while dragging.
     */
    private void drawSelectionBox(Graphics2D g2d) {
        int x = Math.min(inputHandler.getDragStartX(), inputHandler.getMouseX());
        int y = Math.min(inputHandler.getDragStartY(), inputHandler.getMouseY());
        int w = Math.abs(inputHandler.getMouseX() - inputHandler.getDragStartX());
        int h = Math.abs(inputHandler.getMouseY() - inputHandler.getDragStartY());
        
        g2d.setColor(new Color(0, 255, 0, 50));
        g2d.fillRect(x, y, w, h);
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, w, h);
    }
    
    /**
     * Draw the HUD/UI overlay.
     */
    private void drawUI(Graphics2D g2d) {
        // Performance stats
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(5, 5, 200, 50);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("FPS: " + gameLoop.getFps() + " | UPS: " + gameLoop.getUps(), 10, 20);
        g2d.drawString("Entities: " + world.getAllEntities().size(), 10, 35);
        g2d.drawString("Left-click: Select | Right-click: Move/Attack", 10, 50);
        
        // Selected units info
        List<Entity> selected = selectionSystem.getSelectedEntities(world.getAllEntities());
        if (!selected.isEmpty()) {
            int panelY = getHeight() - 80;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(5, panelY, 250, 75);
            
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Selected: " + selected.size() + " units", 10, panelY + 15);
            
            // Show first unit's stats
            if (selected.size() == 1) {
                Entity unit = selected.get(0);
                UnitType type = unit.getComponent(UnitType.class);
                Health health = unit.getComponent(Health.class);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString("Type: " + type.type.name(), 10, panelY + 30);
                g2d.drawString(String.format("HP: %.0f/%.0f", health.current, health.max), 
                             10, panelY + 45);
                g2d.drawString(String.format("DMG: %.0f | SPD: %.1f", 
                             type.type.attackDamage, type.type.moveSpeed), 
                             10, panelY + 60);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used - game loop handles timing
    }
}
