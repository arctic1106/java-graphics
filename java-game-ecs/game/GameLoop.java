package game;

/**
 * GameLoop - The heartbeat of any game.
 * 
 * ARCHITECTURE PATTERN: Fixed Timestep Game Loop
 * 
 * This is the most important pattern in game development.
 * It decouples:
 *   - LOGIC updates (fixed rate, e.g., 60 times/second)
 *   - RENDERING (as fast as possible, e.g., 144 fps)
 * 
 * Without this, game speed would depend on the player's
 * computer speed. A fast PC would make units move faster!
 * 
 * The loop:
 * 1. Calculate how much time has passed
 * 2. Accumulate that time
 * 3. Run fixed-size logic updates until caught up
 * 4. Render once
 * 
 * This ensures consistent behavior across all hardware.
 */
public class GameLoop {
    
    // Fixed timestep: 60 updates per second
    public static final double FIXED_DT = 1.0 / 60.0;
    
    private final Runnable updateCallback;
    private final Runnable renderCallback;
    
    private boolean running = false;
    private long lastTime = 0;
    private double accumulator = 0;
    
    // Performance tracking
    private int fps = 0;
    private int ups = 0;
    private int frameCount = 0;
    private int updateCount = 0;
    private long fpsTimer = 0;
    
    public GameLoop(Runnable updateCallback, Runnable renderCallback) {
        this.updateCallback = updateCallback;
        this.renderCallback = renderCallback;
    }
    
    /**
     * Start the game loop in a new thread.
     */
    public void start() {
        running = true;
        lastTime = System.nanoTime();
        fpsTimer = System.currentTimeMillis();
        
        Thread gameThread = new Thread(this::run, "GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }
    
    /**
     * Stop the game loop.
     */
    public void stop() {
        running = false;
    }
    
    /**
     * The main loop. Runs on its own thread.
     */
    private void run() {
        while (running) {
            long currentTime = System.nanoTime();
            double frameTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;
            
            // Clamp to avoid spiral of death
            if (frameTime > 0.25) {
                frameTime = 0.25;
            }
            
            accumulator += frameTime;
            
            // Fixed timestep updates
            while (accumulator >= FIXED_DT) {
                updateCallback.run();
                accumulator -= FIXED_DT;
                updateCount++;
            }
            
            // Render
            renderCallback.run();
            frameCount++;
            
            // Update FPS/UPS counters every second
            long now = System.currentTimeMillis();
            if (now - fpsTimer >= 1000) {
                fps = frameCount;
                ups = updateCount;
                frameCount = 0;
                updateCount = 0;
                fpsTimer = now;
            }
            
            // Small sleep to prevent CPU spinning
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public int getFps() { return fps; }
    public int getUps() { return ups; }
    public boolean isRunning() { return running; }
}
