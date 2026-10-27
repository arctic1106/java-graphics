package components;

import ecs.Component;

/**
 * Position Component - Stores the entity's location on the map.
 * 
 * Components are PURE DATA — no logic, no methods that do things.
 * This just holds x and y coordinates.
 */
public class Position implements Component {
    public double x;
    public double y;
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return Position.class;
    }
    
    @Override
    public String toString() {
        return String.format("Pos(%.1f, %.1f)", x, y);
    }
}
