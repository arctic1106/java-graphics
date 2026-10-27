package components;

import ecs.Component;

public class UnitType implements Component {

    public enum Type {
        SOLDIER(50, 2.0, 10.0, 80, 8),
        ARCHER(30, 2.5, 15.0, 120, 7),
        TANK(150, 1.0, 8.0, 200, 12);

        public final double baseHealth;
        public final double moveSpeed;
        public final double attackDamage;
        public final double attackRange;
        public final double size;

        Type(double baseHealth, double moveSpeed, double attackDamage, double attackRange, double size) {
            this.baseHealth = baseHealth;
            this.moveSpeed = moveSpeed;
            this.attackDamage = attackDamage;
            this.attackRange = attackRange;
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
