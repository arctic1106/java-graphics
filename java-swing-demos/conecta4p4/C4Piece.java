public enum C4Piece implements Piece {
    B, R, E; // E is Empty

    @Override
    public C4Piece opposite() {
        return switch (this) {
            case B -> C4Piece.R;
            case R -> C4Piece.B;
            default -> C4Piece.E; // E, empty
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case B -> "B";
            case R -> "R";
            default -> "";
        };
    }
}
