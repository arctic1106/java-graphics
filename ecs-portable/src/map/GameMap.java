package map;

import ui.Canvas;

public class GameMap {

    public enum Tile {
        GRASS(0xFF3C783C, true),
        WATER(0xFF2850B4, false),
        ROCK(0xFF646464, false);

        public final int color;
        public final boolean passable;

        Tile(int color, boolean passable) {
            this.color = color;
            this.passable = passable;
        }
    }

    public final int width;
    public final int height;
    public final int tileSize;
    private final Tile[][] tiles;

    public GameMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = Tile.GRASS;
            }
        }
        for (int x = 8; x < 12; x++) {
            for (int y = 5; y < 10; y++) {
                tiles[y][x] = Tile.WATER;
            }
        }
        tiles[3][3] = Tile.ROCK;
        tiles[3][4] = Tile.ROCK;
        tiles[4][3] = Tile.ROCK;
        tiles[12][15] = Tile.ROCK;
        tiles[12][16] = Tile.ROCK;
        tiles[13][15] = Tile.ROCK;
    }

    public Tile getTile(int gridX, int gridY) {
        if (gridX < 0 || gridX >= width || gridY < 0 || gridY >= height) {
            return Tile.ROCK;
        }
        return tiles[gridY][gridX];
    }

    public int toGridX(double pixelX) {
        return (int) (pixelX / tileSize);
    }

    public int toGridY(double pixelY) {
        return (int) (pixelY / tileSize);
    }

    public void render(Canvas c) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                c.fillRect(x * tileSize, y * tileSize, tileSize, tileSize, tiles[y][x].color);
                c.strokeRoundRect(x * tileSize, y * tileSize, tileSize, tileSize, 0, 1, 0x1E000000);
            }
        }
    }

    public int getPixelWidth() {
        return width * tileSize;
    }

    public int getPixelHeight() {
        return height * tileSize;
    }
}
