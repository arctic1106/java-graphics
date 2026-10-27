package components;

import ecs.Component;

/**
 * Selectable Component - Marks an entity as selectable by the player.
 * 
 * When the player clicks on a unit or drags a selection box,
 * the SelectionSystem checks this component.
 */
public class Selectable implements Component {
    public boolean isSelected;
    public double radius; // click detection radius
    
    public Selectable(double radius) {
        this.isSelected = false;
        this.radius = radius;
    }
    
    @Override
    public Class<? extends Component> getType() {
        return Selectable.class;
    }
}
