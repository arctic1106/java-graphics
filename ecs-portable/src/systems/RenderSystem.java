package systems;

import java.util.List;
import components.*;
import ecs.*;
import ui.Canvas;

public class RenderSystem extends GameSystem {

    private static final int[] TEAM_COLORS = {0xFF2864DC, 0xFFDC3228};
    private static final int[] TEAM_BORDERS = {0xFF1C46A0, 0xFF9A241C};

    public RenderSystem() {
        super(Position.class, UnitType.class, Team.class);
    }

    @Override
    public void update(List<Entity> entities, double dt) {
    }

    public void render(Canvas c, List<Entity> entities) {
        for (Entity entity : entities) {
            Position pos = entity.getComponent(Position.class);
            UnitType unitType = entity.getComponent(UnitType.class);
            Health health = entity.getComponent(Health.class);
            Selectable selectable = entity.getComponent(Selectable.class);
            Team team = entity.getComponent(Team.class);

            float size = (float) unitType.type.size;
            float x = (float) pos.x;
            float y = (float) pos.y;
            int idx = team.teamId % TEAM_COLORS.length;

            c.fillCircle(x, y, size / 2, TEAM_COLORS[idx]);
            c.strokeRoundRect(x - size / 2, y - size / 2, size, size, size / 2, 2, TEAM_BORDERS[idx]);

            if (selectable != null && selectable.isSelected) {
                float r = size / 2 + 4;
                c.strokeRoundRect(x - r, y - r, 2 * r, 2 * r, r, 3, 0xFFFFFF00);
            }

            if (health != null && health.isAlive()) {
                healthBar(c, x, y - size / 2 - 8, size, health);
            }

            c.text(unitType.type.name().substring(0, 1), x, y, 8, true, 0xFFFFFFFF, Canvas.CENTER);
        }
    }

    private void healthBar(Canvas c, float x, float y, float width, Health health) {
        double percent = health.getHealthPercent();
        c.fillRect(x - width / 2, y, width, 4, 0xFF404040);
        int color = percent > 0.6 ? 0xFF00FF00 : percent > 0.3 ? 0xFFFFFF00 : 0xFFFF0000;
        c.fillRect(x - width / 2, y, (float) (width * percent), 4, color);
    }
}
