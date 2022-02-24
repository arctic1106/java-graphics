package components;

import ecs.Component;

/**
 * Velocity Component - Movement direction and speed.
 * 
 * Systems use this to update Position each frame:
 *   position.x += velocity.dx * dt
 *   position.y += velocity.dy * dt
 */
public class Velocity implements Component {
    public double dx; // horizontal speed
    public double dy; // vertical speed
    
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public void stop() {
        this.dx = 0;
        this.dy = 0;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return Velocity.class;
    }
}
