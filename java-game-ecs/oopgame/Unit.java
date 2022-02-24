
import java.awt.*;

/**
 * Unit - All data AND behavior in one class.
 * 
 * This is the OOP approach: the unit knows how to move,
 * attack, draw itself, and check if it's alive.
 * 
 * Compare with ECS: Position, Velocity, Health, Selectable,
 * UnitType, Team, MoveCommand, AttackCommand = 8 separate classes.
 * Here it's 1 class.
 */
public class Unit {

    public enum Type {
        SOLDIER(50, 2.0, 10.0, 80, 8),
        ARCHER(30, 2.5, 15.0, 120, 7),
        TANK(150, 1.0, 8.0, 200, 12);

        public final double baseHealth, moveSpeed, attackDamage, attackRange, size;

        Type(double baseHealth, double moveSpeed, double attackDamage, double attackRange, double size) {
            this.baseHealth = baseHealth;
            this.moveSpeed = moveSpeed;
            this.attackDamage = attackDamage;
            this.attackRange = attackRange;
            this.size = size;
        }
    }

    // --- Data (same as ECS components, but in one place) ---
    public double x, y;
    public double health, maxHealth;
    public boolean selected;
    public final Type type;
    public final int team; // 0 = player (blue), 1 = enemy (red)

    // State
    private double targetX, targetY;
    private boolean hasTarget;
    private Unit attackTarget;
    private boolean hasAttackTarget;

    // Team colors
    private static final Color[] TEAM_COLORS = {
        new Color(40, 100, 220),
        new Color(220, 50, 40)
    };

    public Unit(double x, double y, Type type, int team) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.team = team;
        this.health = type.baseHealth;
        this.maxHealth = type.baseHealth;
    }

    // --- Behavior (methods that DO things) ---

    public boolean isAlive() {
        return health > 0;
    }

    public void moveTo(double tx, double ty) {
        this.targetX = tx;
        this.targetY = ty;
        this.hasTarget = true;
        this.attackTarget = null;
        this.hasAttackTarget = false;
    }

    public void attack(Unit target) {
        this.attackTarget = target;
        this.hasAttackTarget = true;
        // Move toward target first
        this.targetX = target.x;
        this.targetY = target.y;
        this.hasTarget = true;
    }

    public void update(double dt) {
        if (!isAlive()) return;

        // If attacking, update target position
        if (hasAttackTarget) {
            if (!attackTarget.isAlive()) {
                hasAttackTarget = false;
                hasTarget = false;
                return;
            }
            targetX = attackTarget.x;
            targetY = attackTarget.y;
        }

        // Move toward target
        if (hasTarget) {
            double dx = targetX - x;
            double dy = targetY - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (hasAttackTarget && dist <= type.attackRange) {
                // In range — deal damage
                attackTarget.health -= type.attackDamage * dt;
                hasTarget = false; // stop moving
            } else if (dist > 2.0) {
                // Move toward target
                x += (dx / dist) * type.moveSpeed * dt * 60;
                y += (dy / dist) * type.moveSpeed * dt * 60;
            } else {
                // Arrived
                hasTarget = false;
            }
        }
    }

    public void render(Graphics2D g2d) {
        Color color = TEAM_COLORS[team % TEAM_COLORS.length];

        // Body
        g2d.setColor(color);
        g2d.fillOval((int)(x - type.size/2), (int)(y - type.size/2), (int)type.size, (int)type.size);

        // Border
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int)(x - type.size/2), (int)(y - type.size/2), (int)type.size, (int)type.size);

        // Selection ring
        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval((int)(x - type.size/2 - 4), (int)(y - type.size/2 - 4), (int)(type.size + 8), (int)(type.size + 8));
        }

        // Health bar
        if (isAlive()) {
            double barW = type.size, barH = 4;
            double pct = health / maxHealth;
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect((int)(x - barW/2), (int)(y - type.size/2 - 8), (int)barW, (int)barH);
            g2d.setColor(pct > 0.6 ? Color.GREEN : pct > 0.3 ? Color.YELLOW : Color.RED);
            g2d.fillRect((int)(x - barW/2), (int)(y - type.size/2 - 8), (int)(barW * pct), (int)barH);
        }

        // Label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 8));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(type.name().substring(0, 1), (int)(x - fm.stringWidth(type.name().substring(0, 1))/2), (int)(y + fm.getAscent()/2));
    }

    public boolean containsPoint(double px, double py) {
        double dx = px - x, dy = py - y;
        return Math.sqrt(dx*dx + dy*dy) < type.size + 5;
    }
}
