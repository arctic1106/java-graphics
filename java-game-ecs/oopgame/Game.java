
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game - The whole game in one class.
 * 
 * OOP approach: one class owns everything.
 * Compare with ECS: World + 5 systems + 8 components = 14 classes.
 */
public class Game {

    private final List<Unit> units = new ArrayList<>();
    public final GameMap map;
    private final InputHandler input;

    public Game() {
        this.map = new GameMap(20, 15, 40);
        this.input = new InputHandler(this);
        spawnUnits();
    }

    private void spawnUnits() {
        // Player (team 0)
        units.add(new Unit(100, 100, Unit.Type.SOLDIER, 0));
        units.add(new Unit(150, 120, Unit.Type.SOLDIER, 0));
        units.add(new Unit(200, 100, Unit.Type.ARCHER, 0));
        units.add(new Unit(120, 180, Unit.Type.TANK, 0));
        // Enemy (team 1)
        units.add(new Unit(600, 300, Unit.Type.SOLDIER, 1));
        units.add(new Unit(650, 320, Unit.Type.SOLDIER, 1));
        units.add(new Unit(700, 300, Unit.Type.ARCHER, 1));
        units.add(new Unit(680, 380, Unit.Type.TANK, 1));
    }

    public void update(double dt) {
        // Update all units (each unit handles its own logic)
        for (Unit unit : units) {
            unit.update(dt);
        }
        // Remove dead units
        units.removeIf(u -> !u.isAlive());
    }

    public void selectUnit(int x, int y) {
        for (Unit u : units) u.selected = false;
        Unit closest = null;
        double closestDist = Double.MAX_VALUE;
        for (Unit u : units) {
            if (u.containsPoint(x, y)) {
                double dist = Math.sqrt(Math.pow(u.x - x, 2) + Math.pow(u.y - y, 2));
                if (dist < closestDist) { closest = u; closestDist = dist; }
            }
        }
        if (closest != null) closest.selected = true;
    }

    public void boxSelect(int x1, int y1, int x2, int y2) {
        for (Unit u : units) u.selected = false;
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        for (Unit u : units) {
            if (u.x >= minX && u.x <= maxX && u.y >= minY && u.y <= maxY)
                u.selected = true;
        }
    }

    public void issueCommand(int x, int y) {
        List<Unit> selected = getSelected();
        if (selected.isEmpty()) return;

        // Check if clicking an enemy
        Unit target = null;
        for (Unit u : units) {
            if (u.isAlive() && !selected.contains(u) && u.containsPoint(x, y)) {
                target = u;
                break;
            }
        }

        for (Unit u : selected) {
            if (target != null) {
                u.attack(target);
            } else {
                u.moveTo(x, y);
            }
        }
    }

    public List<Unit> getSelected() {
        List<Unit> result = new ArrayList<>();
        for (Unit u : units) if (u.selected) result.add(u);
        return result;
    }

    public List<Unit> getUnits() { return units; }
    public InputHandler getInput() { return input; }
}
