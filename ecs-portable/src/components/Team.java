package components;

import ecs.Component;

/**
 * Team Component - Determines ownership and hostility.
 * 
 * Units on different teams are enemies.
 * The CombatSystem uses this to find valid targets.
 */
public class Team implements Component {
    public final int teamId;
    
    public Team(int teamId) {
        this.teamId = teamId;
    }
    
    public boolean isEnemy(Team other) {
        return this.teamId != other.teamId;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return Team.class;
    }
}
