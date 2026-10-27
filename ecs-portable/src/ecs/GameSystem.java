package ecs;

import java.util.List;

/**
 * GameSystem - Processes entities that have specific components.
 * 
 * ARCHITECTURE PATTERN: Entity-Component System (ECS)
 * 
 * Systems contain the LOGIC. They:
 * 1. Query for entities with required components
 * 2. Process those entities each frame
 * 
 * Examples: MovementSystem moves all entities with Position+Velocity
 *           RenderSystem draws all entities with Position+Sprite
 */
public abstract class GameSystem {
    
    /**
     * Required component types that an entity must have
     * to be processed by this system.
     */
    private final Class<? extends Component>[] requiredComponents;
    
    @SafeVarargs
    public GameSystem(Class<? extends Component>... requiredComponents) {
        this.requiredComponents = requiredComponents;
    }
    
    /**
     * Check if an entity matches this system's requirements.
     */
    public boolean matches(Entity entity) {
        for (Class<? extends Component> type : requiredComponents) {
            if (!entity.hasComponent(type)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Called once per frame. Process all matching entities.
     * @param entities all entities in the world
     * @param dt delta time in seconds since last frame
     */
    public abstract void update(List<Entity> entities, double dt);
}
