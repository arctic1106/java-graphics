package systems;

import java.util.List;
import components.*;
import ecs.*;

/**
 * MovementSystem - Handles unit movement toward targets.
 * 
 * ARCHITECTURE PATTERN: System processes entities with specific components.
 * 
 * Required components: Position, Velocity, MoveCommand
 * 
 * Each frame:
 * 1. Calculate direction from current position to target
 * 2. Set velocity toward target
 * 3. Move entity
 * 4. If close enough to target, stop and remove command
 */
public class MovementSystem extends GameSystem {
    
    public MovementSystem() {
        super(Position.class, Velocity.class, MoveCommand.class);
    }
    
    @Override
    public void update(List<Entity> entities, double dt) {
        for (Entity entity : entities) {
            Position pos = entity.getComponent(Position.class);
            Velocity vel = entity.getComponent(Velocity.class);
            MoveCommand cmd = entity.getComponent(MoveCommand.class);
            UnitType unitType = entity.getComponent(UnitType.class);
            
            // Get move speed from unit type
            double speed = unitType != null ? unitType.type.moveSpeed : 2.0;
            
            // Calculate direction to target
            double dx = cmd.targetX - pos.x;
            double dy = cmd.targetY - pos.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Close enough to target? Stop.
            if (distance < 2.0) {
                pos.x = cmd.targetX;
                pos.y = cmd.targetY;
                vel.stop();
                entity.removeComponent(MoveCommand.class);
                continue;
            }
            
            // Normalize and apply speed
            vel.dx = (dx / distance) * speed;
            vel.dy = (dy / distance) * speed;
            
            // Apply velocity
            pos.x += vel.dx * dt * 60; // 60 = assuming ~60fps for consistent movement
            pos.y += vel.dy * dt * 60;
        }
    }
}
