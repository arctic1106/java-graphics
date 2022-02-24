package components;

import ecs.Component;

/**
 * UnitType Component - Defines the unit's role and stats.
 * 
 * In a full game, different unit types would have different
 * stats, abilities, and behavior. For this prototype, we keep
 * it simple with just a type enum and display color.
 */
public class UnitType implements Component {
    
    public enum Type {
        SOLDIER(50, 2.0, 10.0, 80, java.awt.Color.BLUE, 8),
        ARCHER(30, 2.5, 15.0, 120, java.awt.Color.GREEN, 7),
        TANK(150, 1.0, 8.0, 200, java.awt.Color.RED, 12);
        
        public final double baseHealth;
        public final double moveSpeed;
        public final double attackDamage;
        public final double attackRange;
        public final java.awt.Color color;
        public final double size;
        
        Type(double baseHealth, double moveSpeed, double attackDamage, 
             double attackRange, java.awt.Color color, double size) {
            this.baseHealth = baseHealth;
            this.moveSpeed = moveSpeed;
            this.attackDamage = attackDamage;
            this.attackRange = attackRange;
            this.color = color;
            this.size = size;
        }
    }
    
    public final Type type;
    
    public UnitType(Type type) {
        this.type = type;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return UnitType.class;
    }
}
