package map;

import java.awt.*;

/**
 * GameMap - Simple grid-based map for the RTS.
 * 
 * ARCHITECTURE PATTERN: Spatial partitioning
 * 
 * The map is a grid where each cell can be:
 * - GRASS (passable)
 * - WATER (impassable)
 * - ROCK (impassable, blocks projectiles)
 * 
 * For this prototype, we just render a simple grid.
 * A full game would use this for pathfinding (A* algorithm).
 */
public class GameMap {
    
    public enum Tile {
        GRASS(Color(60, 120, 60), true),
        WATER(Color(40, 80, 180), false),
        ROCK(Color(100, 100, 100), false);
        
        public final Color color;
        public final boolean passable;
        
        Tile(Color color, boolean passable) {
            this.color = color;
            this.passable = passable;
        }
    }
    
    private static Color Color(int r, int g, int b) {
        return new Color(r, g, b);
    }
    
    public final int width;   // in tiles
    public final int height;  // in tiles
    public final int tileSize; // pixels per tile
    private final Tile[][] tiles;
    
    public GameMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = new Tile[height][width];
        
        // Initialize with grass
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = Tile.GRASS;
            }
        }
        
        // Add some water
        for (int x = 8; x < 12; x++) {
            for (int y = 5; y < 10; y++) {
                tiles[y][x] = Tile.WATER;
            }
        }
        
        // Add some rocks
        tiles[3][3] = Tile.ROCK;
        tiles[3][4] = Tile.ROCK;
        tiles[4][3] = Tile.ROCK;
        tiles[12][15] = Tile.ROCK;
        tiles[12][16] = Tile.ROCK;
        tiles[13][15] = Tile.ROCK;
    }
    
    /**
     * Get tile at grid coordinates.
     */
    public Tile getTile(int gridX, int gridY) {
        if (gridX < 0 || gridX >= width || gridY < 0 || gridY >= height) {
            return Tile.ROCK; // out of bounds = impassable
        }
        return tiles[gridY][gridX];
    }
    
    /**
     * Convert pixel coordinates to grid coordinates.
     */
    public int toGridX(double pixelX) {
        return (int)(pixelX / tileSize);
    }
    
    public int toGridY(double pixelY) {
        return (int)(pixelY / tileSize);
    }
    
    /**
     * Draw the map.
     */
    public void render(Graphics2D g2d) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = tiles[y][x];
                g2d.setColor(tile.color);
                g2d.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                
                // Grid lines (subtle)
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }
    
    /**
     * Get map width in pixels.
     */
    public int getPixelWidth() {
        return width * tileSize;
    }
    
    /**
     * Get map height in pixels.
     */
    public int getPixelHeight() {
        return height * tileSize;
    }
}
