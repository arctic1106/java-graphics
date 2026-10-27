package components;

import ecs.Component;

/**
 * AttackCommand Component - Target entity to attack.
 * 
 * When the player right-clicks an enemy unit, this component
 * is added. The CombatSystem handles the attack logic.
 */
public class AttackCommand implements Component {
    public int targetEntityId;
    
    public AttackCommand(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return AttackCommand.class;
    }
}
