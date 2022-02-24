public enum Suit {
    SPADES(Color.BLACK),
    HEARTS(Color.RED),
    DIAMONDS(Color.RED),
    CLUBS(Color.BLACK);

    public enum Color {
        BLACK, RED
    }

    private final Color color;

    Suit(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }
}
