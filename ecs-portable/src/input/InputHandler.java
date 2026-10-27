package input;

import java.util.List;
import components.*;
import ecs.*;
import systems.SelectionSystem;

public class InputHandler {

    private final World world;
    private final SelectionSystem selectionSystem;

    private float mouseX;
    private float mouseY;
    private float dragStartX;
    private float dragStartY;
    private boolean down;
    private boolean dragging;

    public InputHandler(World world, SelectionSystem selectionSystem) {
        this.world = world;
        this.selectionSystem = selectionSystem;
    }

    public void down(float x, float y) {
        mouseX = x;
        mouseY = y;
        dragStartX = x;
        dragStartY = y;
        down = true;
        dragging = false;
    }

    public void move(float x, float y) {
        mouseX = x;
        mouseY = y;
        if (down && Math.hypot(x - dragStartX, y - dragStartY) > 8) dragging = true;
    }

    public void up(float x, float y) {
        mouseX = x;
        mouseY = y;
        if (!down) return;
        if (dragging) {
            selectionSystem.setBoxSelection(dragStartX, dragStartY, x, y);
        } else {
            Entity hit = unitAt(x, y);
            boolean own = hit != null && hit.getComponent(Team.class).teamId == 0;
            if (own || hit == null && selectedUnits().isEmpty()) {
                selectionSystem.setClickSelection(x, y);
            } else if (!selectedUnits().isEmpty()) {
                command(x, y);
            }
        }
        down = false;
        dragging = false;
    }

    public void command(float x, float y) {
        List<Entity> selected = selectedUnits();
        if (selected.isEmpty()) return;

        Entity target = findEnemy(x, y, selected);
        if (target != null) {
            Position targetPos = target.getComponent(Position.class);
            for (Entity unit : selected) {
                unit.removeComponent(MoveCommand.class);
                unit.addComponent(new AttackCommand(target.id));
                unit.addComponent(new MoveCommand(targetPos.x, targetPos.y));
            }
        } else {
            for (Entity unit : selected) {
                unit.removeComponent(AttackCommand.class);
                unit.addComponent(new MoveCommand(x, y));
            }
        }
    }

    private List<Entity> selectedUnits() {
        return selectionSystem.getSelectedEntities(world.getAllEntities());
    }

    private Entity unitAt(float x, float y) {
        Entity best = null;
        double bestDist = Double.MAX_VALUE;
        for (Entity entity : world.getAllEntities()) {
            Position pos = entity.getComponent(Position.class);
            Selectable sel = entity.getComponent(Selectable.class);
            if (pos == null || sel == null) continue;
            double dist = Math.hypot(pos.x - x, pos.y - y);
            if (dist < sel.radius + 5 && dist < bestDist) {
                best = entity;
                bestDist = dist;
            }
        }
        return best;
    }

    private Entity findEnemy(float x, float y, List<Entity> friendly) {
        for (Entity entity : world.getAllEntities()) {
            Position pos = entity.getComponent(Position.class);
            Selectable sel = entity.getComponent(Selectable.class);
            Team team = entity.getComponent(Team.class);
            if (pos == null || sel == null || team == null) continue;

            boolean isFriendly = false;
            for (Entity f : friendly) {
                if (f.id == entity.id) {
                    isFriendly = true;
                    break;
                }
            }
            if (isFriendly) continue;

            if (Math.hypot(pos.x - x, pos.y - y) < sel.radius + 5) return entity;
        }
        return null;
    }

    public float getMouseX() { return mouseX; }
    public float getMouseY() { return mouseY; }
    public float getDragStartX() { return dragStartX; }
    public float getDragStartY() { return dragStartY; }
    public boolean isDragging() { return dragging; }
}
