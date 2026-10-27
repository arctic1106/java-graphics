package components;

import ecs.Component;

/**
 * MoveCommand Component - Target position for unit movement.
 * 
 * When the player right-clicks a location, the InputHandler
 * adds this component to selected units. The MovementSystem
 * then moves the unit toward the target and removes the
 * component when it arrives.
 * 
 * This is the COMMAND PATTERN — the player's intent is stored
 * as data, and a system executes it.
 */
public class MoveCommand implements Component {
    public double targetX;
    public double targetY;
    
    public MoveCommand(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return MoveCommand.class;
    }
}
