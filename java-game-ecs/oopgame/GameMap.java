
import java.awt.*;

/**
 * GameMap - Terrain grid. Same as ECS version.
 */
public class GameMap {

    public enum Tile { GRASS(true), WATER(false), ROCK(false);
        public final boolean passable;
        Tile(boolean passable) { this.passable = passable; }
    }

    public final int width, height, tileSize;
    private final Tile[][] tiles;

    public GameMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = new Tile[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                tiles[y][x] = Tile.GRASS;

        // Water
        for (int x = 8; x < 12; x++)
            for (int y = 5; y < 10; y++)
                tiles[y][x] = Tile.WATER;

        // Rocks
        tiles[3][3] = Tile.ROCK; tiles[3][4] = Tile.ROCK; tiles[4][3] = Tile.ROCK;
        tiles[12][15] = Tile.ROCK; tiles[12][16] = Tile.ROCK; tiles[13][15] = Tile.ROCK;
    }

    public void render(Graphics2D g2d) {
        Color[] colors = { new Color(60,120,60), new Color(40,80,180), new Color(100,100,100) };
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g2d.setColor(colors[tiles[y][x].ordinal()]);
                g2d.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
                g2d.setColor(new Color(0,0,0,30));
                g2d.drawRect(x*tileSize, y*tileSize, tileSize, tileSize);
            }
        }
    }

    public int getPixelWidth() { return width * tileSize; }
    public int getPixelHeight() { return height * tileSize; }
}
