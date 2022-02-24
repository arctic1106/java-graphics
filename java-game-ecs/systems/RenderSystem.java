package systems;

import java.awt.*;
import java.util.List;
import components.*;
import ecs.*;

/**
 * RenderSystem - Draws all entities to the screen.
 * 
 * ARCHITECTURE PATTERN: System processes entities with specific components.
 * 
 * Required components: Position, UnitType
 * 
 * This system doesn't update game state — it only READS
 * component data and draws to the Graphics context.
 */
public class RenderSystem extends GameSystem {
    
    // Team colors: team 0 = blue (player), team 1 = red (enemy)
    private static final Color[] TEAM_COLORS = {
        new Color(40, 100, 220),   // Team 0 - bright blue
        new Color(220, 50, 40)    // Team 1 - bright red
    };
    
    public RenderSystem() {
        super(Position.class, UnitType.class, Team.class);
    }
    
    @Override
    public void update(List<Entity> entities, double dt) {
        // Rendering is handled by paintComponent, not here
        // This system is just for the ECS architecture demonstration
    }
    
    /**
     * Draw all matching entities.
     * Called from the GamePanel's paintComponent method.
     */
    public void render(Graphics2D g2d, List<Entity> entities) {
        for (Entity entity : entities) {
            Position pos = entity.getComponent(Position.class);
            UnitType unitType = entity.getComponent(UnitType.class);
            Health health = entity.getComponent(Health.class);
            Selectable selectable = entity.getComponent(Selectable.class);
            
            Team team = entity.getComponent(Team.class);
            
            double size = unitType.type.size;
            Color color = TEAM_COLORS[team.teamId % TEAM_COLORS.length];
            
            // Draw unit body
            g2d.setColor(color);
            g2d.fillOval(
                (int)(pos.x - size/2), 
                (int)(pos.y - size/2), 
                (int)size, 
                (int)size
            );
            
            // Draw border
            g2d.setColor(color.darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(
                (int)(pos.x - size/2), 
                (int)(pos.y - size/2), 
                (int)size, 
                (int)size
            );
            
            // Draw selection ring
            if (selectable != null && selectable.isSelected) {
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(
                    (int)(pos.x - size/2 - 4), 
                    (int)(pos.y - size/2 - 4), 
                    (int)(size + 8), 
                    (int)(size + 8)
                );
            }
            
            // Draw health bar
            if (health != null && health.isAlive()) {
                drawHealthBar(g2d, pos.x, pos.y - size/2 - 8, size, health);
            }
            
            // Draw unit type label
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 8));
            String label = unitType.type.name().substring(0, 1);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(label, 
                (int)(pos.x - fm.stringWidth(label)/2), 
                (int)(pos.y + fm.getAscent()/2)
            );
        }
    }
    
    private void drawHealthBar(Graphics2D g2d, double x, double y, 
                                double width, Health health) {
        double barWidth = width;
        double barHeight = 4;
        double healthPercent = health.getHealthPercent();
        
        // Background (dark gray)
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int)(x - barWidth/2), (int)y, (int)barWidth, (int)barHeight);
        
        // Health (green -> yellow -> red based on percentage)
        if (healthPercent > 0.6) {
            g2d.setColor(Color.GREEN);
        } else if (healthPercent > 0.3) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }
        g2d.fillRect((int)(x - barWidth/2), (int)y, 
                     (int)(barWidth * healthPercent), (int)barHeight);
    }
}
