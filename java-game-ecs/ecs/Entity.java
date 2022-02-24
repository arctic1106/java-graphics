package ecs;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity - A unique identifier with attached components.
 * 
 * ARCHITECTURE PATTERN: Entity-Component System (ECS)
 * 
 * Entities are lightweight IDs. They don't contain logic —
 * Systems operate on entities by querying their components.
 * 
 * Example entity: A soldier unit
 *   → has Position(100, 200)
 *   → has Health(100)
 *   → has Selectable(true)
 *   → has Velocity(0, 0)
 */
public class Entity {
    private static int nextId = 0;
    
    public final int id;
    private final Map<Class<? extends Component>, Component> components;
    
    public Entity() {
        this.id = nextId++;
        this.components = new HashMap<>();
    }
    
    /**
     * Attach a component to this entity.
     * Replaces any existing component of the same type.
     */
    public Entity addComponent(Component component) {
        components.put(component.getType(), component);
        return this; // fluent API for chaining
    }
    
    /**
     * Get a component by type. Returns null if not present.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.get(type);
    }
    
    /**
     * Check if this entity has a specific component.
     */
    public boolean hasComponent(Class<? extends Component> type) {
        return components.containsKey(type);
    }
    
    /**
     * Remove a component from this entity.
     */
    public void removeComponent(Class<? extends Component> type) {
        components.remove(type);
    }
    
    @Override
    public String toString() {
        return "Entity#" + id;
    }
}
