package systems;

import java.util.List;
import components.*;
import ecs.*;

/**
 * CombatSystem - Handles unit combat.
 * 
 * ARCHITECTURE PATTERN: System processes entities with specific components.
 * 
 * Required components: Position, Health, AttackCommand, Team
 * 
 * Each frame:
 * 1. Find the target entity by ID
 * 2. Check if target is still alive
 * 3. Check if target is in range
 * 4. Deal damage
 * 5. Remove command if target is dead or out of range
 */
public class CombatSystem extends GameSystem {
    
    private final World world;
    
    public CombatSystem(World world) {
        super(Position.class, Health.class, AttackCommand.class, Team.class);
        this.world = world;
    }
    
    @Override
    public void update(List<Entity> entities, double dt) {
        for (Entity entity : entities) {
            Position pos = entity.getComponent(Position.class);
            Health health = entity.getComponent(Health.class);
            AttackCommand cmd = entity.getComponent(AttackCommand.class);
            Team team = entity.getComponent(Team.class);
            UnitType unitType = entity.getComponent(UnitType.class);
            
            // Skip dead units
            if (!health.isAlive()) {
                entity.removeComponent(AttackCommand.class);
                continue;
            }
            
            // Find target entity
            Entity target = findEntityById(cmd.targetEntityId);
            if (target == null) {
                entity.removeComponent(AttackCommand.class);
                continue;
            }
            
            // Check if target is alive
            Health targetHealth = target.getComponent(Health.class);
            if (targetHealth == null || !targetHealth.isAlive()) {
                entity.removeComponent(AttackCommand.class);
                continue;
            }
            
            // Check if target is on same team
            Team targetTeam = target.getComponent(Team.class);
            if (targetTeam != null && !team.isEnemy(targetTeam)) {
                entity.removeComponent(AttackCommand.class);
                continue;
            }
            
            // Check range
            Position targetPos = target.getComponent(Position.class);
            double dx = targetPos.x - pos.x;
            double dy = targetPos.y - pos.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            double attackRange = unitType != null ? unitType.type.attackRange : 80.0;
            
            if (distance <= attackRange) {
                // In range - deal damage
                double damage = unitType != null ? unitType.type.attackDamage : 10.0;
                targetHealth.takeDamage(damage * dt); // DPS
                
                // Stop moving while attacking
                entity.removeComponent(MoveCommand.class);
                Velocity vel = entity.getComponent(Velocity.class);
                if (vel != null) vel.stop();
            }
        }
    }
    
    private Entity findEntityById(int id) {
        for (Entity entity : world.getAllEntities()) {
            if (entity.id == id) {
                return entity;
            }
        }
        return null;
    }
}
