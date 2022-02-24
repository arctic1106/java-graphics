import java.awt.Point;

public class TileData {
    private final Point[] coords;

    public TileData(TipoPieza tipoPieza) {
        coords = PiezaStaticData.getPoints(tipoPieza);
    }

    public TileData(Point[] coords) {
        this.coords = coords;
    }

    public Point getPoint(int i) {
        return coords[i];
    }

    public int minY() {
        var minY = getPoint(0).y;
        for (var i = 0; i < 4; i++) {
            minY = Math.min(minY, getPoint(i).y);
        }
        return minY;
    }

    public TileData rotateLeft() {
        var newCoords = new Point[4];
        for (var i = 0; i < 4; ++i) {
            var tempX = getPoint(i).x;
            var tempY = getPoint(i).y;
            newCoords[i] = new Point(tempY, -tempX);
        }
        return new TileData(newCoords);
    }

    public TileData rotateRight() {
        var newCoords = new Point[4];
        for (int i = 0; i < 4; ++i) {
            var tempX = getPoint(i).x;
            var tempY = getPoint(i).y;
            newCoords[i] = new Point(-tempY, tempX);
        }
        return new TileData(newCoords);
    }

    public String getCoordString() {
        return this.toString();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (Point p : coords) {
            sb.append("(" + p.x + "," + p.y + ")");
        }

        return sb.toString();
    }
}
