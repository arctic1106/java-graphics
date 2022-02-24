import java.awt.Color;
import java.awt.Point;

public final class PiezaStaticData {

    public static Point[] getPoints(TipoPieza shape) {
        return switch (shape) {
            case NoShape -> new Point[] { new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0) };
            case ZShape -> new Point[] { new Point(0, -1), new Point(0, 0), new Point(-1, 0), new Point(-1, 1) };
            case SShape -> new Point[] { new Point(0, -1), new Point(0, 0), new Point(1, 0), new Point(1, 1) };
            case LineShape -> new Point[] { new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(0, 2) };
            case TShape -> new Point[] { new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(0, 1) };
            case SquareShape -> new Point[] { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) };
            case LShape -> new Point[] { new Point(-1, -1), new Point(0, -1), new Point(0, 0), new Point(0, 1) };
            case MirroredLShape -> new Point[] { new Point(1, -1), new Point(0, -1), new Point(0, 0), new Point(0, 1) };
            default -> throw new IllegalArgumentException("Shape no admitido");
        };
    }

    public static Color getColor(TipoPieza shape) {
        return switch (shape) {
            case NoShape -> new Color(0, 0, 0);
            case ZShape -> new Color(204, 102, 102);
            case SShape -> new Color(102, 204, 102);
            case LineShape -> new Color(102, 102, 204);
            case TShape -> new Color(204, 204, 102);
            case SquareShape -> new Color(204, 102, 204);
            case LShape -> new Color(102, 204, 204);
            case MirroredLShape -> new Color(218, 170, 0);
            default -> throw new IllegalArgumentException("Shape no admitido");
        };
    }
}
