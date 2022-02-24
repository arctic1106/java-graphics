package components;

import ecs.Component;

/**
 * Health Component - Tracks current and maximum HP.
 */
public class Health implements Component {
    public double current;
    public double max;
    
    public Health(double max) {
        this.max = max;
        this.current = max;
    }
    
    public boolean isAlive() {
        return current > 0;
    }
    
    public void takeDamage(double amount) {
        current = Math.max(0, current - amount);
    }
    
    public double getHealthPercent() {
        return current / max;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return Health.class;
    }
}
