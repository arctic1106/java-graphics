package ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * World - The central container for the ECS architecture.
 * 
 * ARCHITECTURE PATTERN: Entity-Component System (ECS)
 * 
 * The World manages:
 * - All entities (units, buildings, projectiles, etc.)
 * - All systems (movement, rendering, combat, etc.)
 * 
 * Each frame, the World tells each system to process its entities.
 */
public class World {
    private final List<Entity> entities;
    private final List<GameSystem> systems;
    
    public World() {
        this.entities = new ArrayList<>();
        this.systems = new ArrayList<>();
    }
    
    /**
     * Create and register a new entity.
     */
    public Entity createEntity() {
        Entity entity = new Entity();
        entities.add(entity);
        return entity;
    }
    
    /**
     * Remove an entity from the world.
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    
    /**
     * Register a system to process entities each frame.
     */
    public void addSystem(GameSystem system) {
        systems.add(system);
    }
    
    /**
     * Get all entities that match a system's requirements.
     */
    public List<Entity> getEntitiesFor(GameSystem system) {
        List<Entity> matching = new ArrayList<>();
        for (Entity entity : entities) {
            if (system.matches(entity)) {
                matching.add(entity);
            }
        }
        return matching;
    }
    
    /**
     * Update all systems. Called once per game tick.
     * @param dt delta time in seconds
     */
    public void update(double dt) {
        for (GameSystem system : systems) {
            List<Entity> matching = getEntitiesFor(system);
            system.update(matching, dt);
        }
    }
    
    /**
     * Get all entities (for rendering purposes).
     */
    public List<Entity> getAllEntities() {
        return new ArrayList<>(entities);
    }
}
