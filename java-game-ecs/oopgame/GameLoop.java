
/**
 * GameLoop - Fixed timestep loop. Same as ECS version.
 */
public class GameLoop {

    public static final double FIXED_DT = 1.0 / 60.0;
    private final Runnable updateCallback, renderCallback;
    private boolean running = false;
    private long lastTime = 0;
    private double accumulator = 0;
    private int fps = 0, ups = 0, frameCount = 0, updateCount = 0;
    private long fpsTimer = 0;

    public GameLoop(Runnable updateCallback, Runnable renderCallback) {
        this.updateCallback = updateCallback;
        this.renderCallback = renderCallback;
    }

    public void start() {
        running = true;
        lastTime = System.nanoTime();
        fpsTimer = System.currentTimeMillis();
        Thread t = new Thread(this::run, "GameLoop");
        t.setDaemon(true);
        t.start();
    }

    private void run() {
        while (running) {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;
            if (dt > 0.25) dt = 0.25;
            accumulator += dt;
            while (accumulator >= FIXED_DT) {
                updateCallback.run();
                accumulator -= FIXED_DT;
                updateCount++;
            }
            renderCallback.run();
            frameCount++;
            long ms = System.currentTimeMillis();
            if (ms - fpsTimer >= 1000) {
                fps = frameCount; ups = updateCount;
                frameCount = 0; updateCount = 0;
                fpsTimer = ms;
            }
            try { Thread.sleep(1); } catch (InterruptedException e) { break; }
        }
    }

    public int getFps() { return fps; }
    public int getUps() { return ups; }
}
