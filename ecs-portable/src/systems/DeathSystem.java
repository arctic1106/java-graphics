package systems;

import java.util.ArrayList;
import java.util.List;
import components.*;
import ecs.*;

/**
 * DeathSystem - Removes entities with 0 health from the world.
 * 
 * Required components: Health
 * 
 * This is a cleanup system — it runs after combat to remove
 * dead units from the world.
 */
public class DeathSystem extends GameSystem {
    
    private final World world;
    private final List<Entity> toRemove = new ArrayList<>();
    
    public DeathSystem(World world) {
        super(Health.class);
        this.world = world;
    }
    
    @Override
    public void update(List<Entity> entities, double dt) {
        toRemove.clear();
        
        for (Entity entity : entities) {
            Health health = entity.getComponent(Health.class);
            if (!health.isAlive()) {
                toRemove.add(entity);
            }
        }
        
        // Remove dead entities
        for (Entity entity : toRemove) {
            world.removeEntity(entity);
        }
    }
}
