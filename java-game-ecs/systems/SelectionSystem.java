package systems;

import java.util.ArrayList;
import java.util.List;
import components.*;
import ecs.*;

/**
 * SelectionSystem - Manages unit selection state.
 * 
 * ARCHITECTURE PATTERN: System processes entities with specific components.
 * 
 * Required components: Position, Selectable
 * 
 * This system:
 * - Deselects all units
 * - Selects units within a click radius
 * - Selects units within a box selection area
 */
public class SelectionSystem extends GameSystem {
    
    // Selection state (managed by InputHandler, read by this system)
    private double selectionX = -1;
    private double selectionY = -1;
    private boolean selectionActive = false;
    
    // Box selection state
    private double boxStartX, boxStartY, boxEndX, boxEndY;
    private boolean boxSelectionActive = false;
    
    public SelectionSystem() {
        super(Position.class, Selectable.class);
    }
    
    /**
     * Set click selection point.
     */
    public void setClickSelection(double x, double y) {
        this.selectionX = x;
        this.selectionY = y;
        this.selectionActive = true;
        this.boxSelectionActive = false;
    }
    
    /**
     * Set box selection area.
     */
    public void setBoxSelection(double x1, double y1, double x2, double y2) {
        this.boxStartX = x1;
        this.boxStartY = y1;
        this.boxEndX = x2;
        this.boxEndY = y2;
        this.boxSelectionActive = true;
        this.selectionActive = false;
    }
    
    @Override
    public void update(List<Entity> entities, double dt) {
        if (!selectionActive && !boxSelectionActive) return;
        
        // First, deselect all
        for (Entity entity : entities) {
            Selectable sel = entity.getComponent(Selectable.class);
            sel.isSelected = false;
        }
        
        if (boxSelectionActive) {
            // Box selection - select all within the box
            for (Entity entity : entities) {
                Position pos = entity.getComponent(Position.class);
                if (isInBox(pos.x, pos.y)) {
                    entity.getComponent(Selectable.class).isSelected = true;
                }
            }
            boxSelectionActive = false;
        } else if (selectionActive) {
            // Click selection - select closest unit within radius
            Entity closest = null;
            double closestDist = Double.MAX_VALUE;
            
            for (Entity entity : entities) {
                Position pos = entity.getComponent(Position.class);
                Selectable sel = entity.getComponent(Selectable.class);
                
                double dx = pos.x - selectionX;
                double dy = pos.y - selectionY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                
                if (dist < sel.radius && dist < closestDist) {
                    closest = entity;
                    closestDist = dist;
                }
            }
            
            if (closest != null) {
                closest.getComponent(Selectable.class).isSelected = true;
            }
            
            selectionActive = false;
        }
    }
    
    private boolean isInBox(double x, double y) {
        double minX = Math.min(boxStartX, boxEndX);
        double maxX = Math.max(boxStartX, boxEndX);
        double minY = Math.min(boxStartY, boxEndY);
        double maxY = Math.max(boxStartY, boxEndY);
        
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
    
    /**
     * Get all currently selected entities.
     */
    public List<Entity> getSelectedEntities(List<Entity> allEntities) {
        List<Entity> selected = new ArrayList<>();
        for (Entity entity : allEntities) {
            Selectable sel = entity.getComponent(Selectable.class);
            if (sel != null && sel.isSelected) {
                selected.add(entity);
            }
        }
        return selected;
    }
}
