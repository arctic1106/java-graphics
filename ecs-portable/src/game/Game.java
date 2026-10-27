package game;

import java.util.List;
import components.*;
import ecs.*;
import input.InputHandler;
import map.GameMap;
import systems.*;
import ui.Canvas;

public class Game {
    public static final double FIXED_DT = 1.0 / 60.0;

    public float insetTop;

    private final World world = new World();
    private final GameMap gameMap = new GameMap(20, 15, 40);
    private final SelectionSystem selectionSystem = new SelectionSystem();
    private final RenderSystem renderSystem = new RenderSystem();
    private final InputHandler input;

    private float scale = 1;
    private float offsetX;
    private float offsetY;
    private long lastNanos;
    private double accumulator;
    private int frames;
    private int updates;
    private int fps;
    private int ups;
    private long fpsTimer;

    public Game() {
        world.addSystem(selectionSystem);
        world.addSystem(new MovementSystem());
        world.addSystem(new CombatSystem(world));
        world.addSystem(new DeathSystem(world));
        input = new InputHandler(world, selectionSystem);

        createUnit(100, 100, UnitType.Type.SOLDIER, 0);
        createUnit(150, 120, UnitType.Type.SOLDIER, 0);
        createUnit(200, 100, UnitType.Type.ARCHER, 0);
        createUnit(120, 180, UnitType.Type.TANK, 0);
        createUnit(600, 300, UnitType.Type.SOLDIER, 1);
        createUnit(650, 320, UnitType.Type.SOLDIER, 1);
        createUnit(700, 300, UnitType.Type.ARCHER, 1);
        createUnit(680, 380, UnitType.Type.TANK, 1);
    }

    private Entity createUnit(double x, double y, UnitType.Type type, int teamId) {
        Entity unit = world.createEntity();
        unit.addComponent(new Position(x, y))
            .addComponent(new Velocity(0, 0))
            .addComponent(new Health(type.baseHealth))
            .addComponent(new Selectable(type.size + 5))
            .addComponent(new UnitType(type))
            .addComponent(new Team(teamId));
        return unit;
    }

    public void frame(long nowNanos) {
        if (lastNanos == 0) {
            lastNanos = nowNanos;
            fpsTimer = nowNanos;
        }
        double dt = Math.min((nowNanos - lastNanos) / 1e9, 0.25);
        lastNanos = nowNanos;
        accumulator += dt;
        while (accumulator >= FIXED_DT) {
            world.update(FIXED_DT);
            accumulator -= FIXED_DT;
            updates++;
        }
        frames++;
        if (nowNanos - fpsTimer >= 1_000_000_000L) {
            fps = frames;
            ups = updates;
            frames = 0;
            updates = 0;
            fpsTimer = nowNanos;
        }
    }

    public void pointerDown(float x, float y) {
        input.down(toWorldX(x), toWorldY(y));
    }

    public void pointerMove(float x, float y) {
        input.move(toWorldX(x), toWorldY(y));
    }

    public void pointerUp(float x, float y) {
        input.up(toWorldX(x), toWorldY(y));
    }

    public void command(float x, float y) {
        input.command(toWorldX(x), toWorldY(y));
    }

    private float toWorldX(float x) {
        return (x - offsetX) / scale;
    }

    private float toWorldY(float y) {
        return (y - offsetY) / scale;
    }

    public void render(Canvas c, float w, float h) {
        float mw = gameMap.getPixelWidth();
        float mh = gameMap.getPixelHeight();
        scale = Math.min(w / mw, h / mh);
        offsetX = (w - mw * scale) / 2;
        offsetY = (h - mh * scale) / 2;

        c.fillRect(0, 0, w, h, 0xFF000000);
        c.save();
        c.translate(offsetX, offsetY);
        c.scale(scale);
        c.clipRect(0, 0, mw, mh);
        gameMap.render(c);
        renderSystem.render(c, world.getAllEntities());
        if (input.isDragging()) selectionBox(c);
        c.restore();

        hud(c, w, h);
    }

    private void selectionBox(Canvas c) {
        float x = Math.min(input.getDragStartX(), input.getMouseX());
        float y = Math.min(input.getDragStartY(), input.getMouseY());
        float w = Math.abs(input.getMouseX() - input.getDragStartX());
        float h = Math.abs(input.getMouseY() - input.getDragStartY());
        c.fillRect(x, y, w, h, 0x3200FF00);
        c.strokeRoundRect(x, y, w, h, 0, 1, 0xFF00FF00);
    }

    private void hud(Canvas c, float w, float h) {
        float top = 5 + insetTop;
        c.fillRoundRect(5, top, 190, 48, 4, 0x96000000);
        c.text("FPS: " + fps + " | UPS: " + ups, 10, top + 12, 12, false, 0xFFFFFFFF, Canvas.LEFT);
        c.text("Entities: " + world.getAllEntities().size(), 10, top + 26, 12, false, 0xFFFFFFFF, Canvas.LEFT);
        c.text("Tap: select / move / attack", 10, top + 40, 12, false, 0xFFFFFFFF, Canvas.LEFT);

        List<Entity> selected = selectionSystem.getSelectedEntities(world.getAllEntities());
        if (selected.isEmpty()) return;

        float py = h - 80;
        c.fillRoundRect(5, py, 200, 75, 4, 0x96000000);
        c.text("Selected: " + selected.size() + " units", 10, py + 14, 12, false, 0xFFFFFF00, Canvas.LEFT);
        if (selected.size() == 1) {
            Entity unit = selected.get(0);
            UnitType type = unit.getComponent(UnitType.class);
            Health health = unit.getComponent(Health.class);
            c.text("Type: " + type.type.name(), 10, py + 30, 12, false, 0xFFFFFFFF, Canvas.LEFT);
            c.text(String.format("HP: %.0f/%.0f", health.current, health.max), 10, py + 45, 12, false, 0xFFFFFFFF, Canvas.LEFT);
            c.text(String.format("DMG: %.0f | SPD: %.1f", type.type.attackDamage, type.type.moveSpeed), 10, py + 60, 12, false, 0xFFFFFFFF, Canvas.LEFT);
        }
    }
}
