package input;

import java.awt.event.*;
import java.util.List;
import components.*;
import ecs.*;
import systems.SelectionSystem;

/**
 * InputHandler - Translates mouse input into game commands.
 * 
 * ARCHITECTURE PATTERN: Input abstraction
 * 
 * Mouse interactions:
 * - Left click: Select a single unit
 * - Left drag: Box select multiple units
 * - Right click on ground: Move selected units
 * - Right click on enemy: Attack with selected units
 * 
 * This handler doesn't directly modify entities — it tells
 * the SelectionSystem what to select, and adds components
 * to entities for commands.
 */
public class InputHandler implements MouseListener, MouseMotionListener {
    
    private final World world;
    private final SelectionSystem selectionSystem;
    
    // Mouse state
    private int mouseX, mouseY;
    private boolean mouseDown = false;
    private int dragStartX, dragStartY;
    private boolean isDragging = false;
    
    public InputHandler(World world, SelectionSystem selectionSystem) {
        this.world = world;
        this.selectionSystem = selectionSystem;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Handled by mousePressed/mouseReleased
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Left click - start selection
            mouseDown = true;
            dragStartX = mouseX;
            dragStartY = mouseY;
            isDragging = false;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Left click released
            if (!isDragging) {
                // Click selection
                selectionSystem.setClickSelection(mouseX, mouseY);
            } else {
                // Box selection
                selectionSystem.setBoxSelection(
                    dragStartX, dragStartY, mouseX, mouseY
                );
            }
            mouseDown = false;
            isDragging = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            // Right click - issue command
            handleRightClick(mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        
        if (mouseDown) {
            double dx = mouseX - dragStartX;
            double dy = mouseY - dragStartY;
            if (Math.sqrt(dx*dx + dy*dy) > 5) {
                isDragging = true;
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    /**
     * Handle right-click commands.
     */
    private void handleRightClick(int x, int y) {
        List<Entity> selected = selectionSystem.getSelectedEntities(world.getAllEntities());
        
        if (selected.isEmpty()) return;
        
        // Check if we clicked on an enemy unit
        Entity target = findEnemyAtPosition(x, y, selected);
        
        if (target != null) {
            // Attack command - move toward target and attack when in range
            Position targetPos = target.getComponent(Position.class);
            for (Entity unit : selected) {
                unit.removeComponent(MoveCommand.class);
                unit.addComponent(new AttackCommand(target.id));
                unit.addComponent(new MoveCommand(targetPos.x, targetPos.y));
            }
        } else {
            // Move command
            for (Entity unit : selected) {
                unit.removeComponent(AttackCommand.class);
                unit.addComponent(new MoveCommand(x, y));
            }
        }
    }
    
    /**
     * Find an enemy entity at the given position.
     */
    private Entity findEnemyAtPosition(int x, int y, List<Entity> friendlyUnits) {
        for (Entity entity : world.getAllEntities()) {
            Position pos = entity.getComponent(Position.class);
            Selectable sel = entity.getComponent(Selectable.class);
            Team team = entity.getComponent(Team.class);
            
            if (pos == null || sel == null || team == null) continue;
            
            // Check if it's an enemy (not in our selected units)
            boolean isFriendly = false;
            for (Entity friendly : friendlyUnits) {
                if (friendly.id == entity.id) {
                    isFriendly = true;
                    break;
                }
            }
            
            if (isFriendly) continue;
            
            // Check distance
            double dx = pos.x - x;
            double dy = pos.y - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if (dist < sel.radius + 5) {
                return entity;
            }
        }
        return null;
    }
    
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public int getDragStartX() { return dragStartX; }
    public int getDragStartY() { return dragStartY; }
    public boolean isDragging() { return isDragging; }
}
