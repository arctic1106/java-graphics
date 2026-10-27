package ui;

public class Widgets {
    public static void panel(Canvas c, float x, float y, float w, float h, float r) {
        c.fillRoundRect(x, y + 1, w, h, r, Theme.SLATE200);
        c.fillRoundRect(x, y, w, h, r, Theme.WHITE);
    }

    public static void card(Canvas c, float x, float y, float w, float h, float r, int color) {
        c.fillRoundRect(x, y, w, h, r, color);
    }

    public static void divider(Canvas c, float x, float y, float w, int color) {
        c.fillRect(x, y, w, 0.5f, color);
    }

    public static void pill(Canvas c, float x, float cy, float w, float h, String text, float size, int bg, int fg) {
        c.fillRoundRect(x, cy - h / 2, w, h, h / 2, bg);
        c.text(text, x + w / 2, cy, size, true, fg, Canvas.CENTER);
    }

    public static void tag(Canvas c, float x, float cy, float s, String text, float size, int bg, int fg) {
        c.fillRoundRect(x, cy - s / 2, s, s, 4, bg);
        c.text(text, x + s / 2, cy, size, true, fg, Canvas.CENTER);
    }

    public static boolean button(Ui ui, float x, float y, float w, float h, float r, String text, float size,
                                 boolean selected, int selectedBg, int idleBg, int selectedFg, int idleFg) {
        Canvas c = ui.c;
        c.fillRoundRect(x, y, w, h, r, selected ? selectedBg : idleBg);
        c.text(text, x + w / 2, y + h / 2, size, true, selected ? selectedFg : idleFg, Canvas.CENTER);
        return ui.clicked(x, y, w, h);
    }

    public static boolean toggle(Ui ui, float x, float y, float w, float h, float r, String text, float size,
                                 boolean selected, int selectedBg) {
        return button(ui, x, y, w, h, r, text, size, selected, selectedBg, Theme.SLATE100, Theme.WHITE, Theme.SLATE600);
    }

    public static void label(Canvas c, String text, float x, float cy, float size, boolean bold, int color) {
        c.text(text, x, cy, size, bold, color, Canvas.LEFT);
    }

    public static void centered(Canvas c, String text, float cx, float cy, float size, boolean bold, int color) {
        c.text(text, cx, cy, size, bold, color, Canvas.CENTER);
    }

    public static void stack(Canvas c, String top, String bottom, float x, float cy, float size, float gap, boolean bold, int color) {
        c.text(top, x, cy - gap, size, bold, color, Canvas.LEFT);
        c.text(bottom, x, cy + gap, size, bold, color, Canvas.LEFT);
    }
}
