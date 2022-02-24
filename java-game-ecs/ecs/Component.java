package ecs;

/**
 * Component - Base interface for all ECS components.
 * 
 * ARCHITECTURE PATTERN: Entity-Component System (ECS)
 * 
 * Components are pure data containers with no logic.
 * They describe WHAT an entity has, not WHAT it does.
 * 
 * Examples: Position, Health, Velocity, Selectable
 */
public interface Component {
    /**
     * Return the Class type of this component for type-safe queries.
     * Example: Position.class
     */
    Class<? extends Component> getType();
}
