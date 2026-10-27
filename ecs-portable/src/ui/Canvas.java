package ui;

public interface Canvas {
    int LEFT = 0;
    int CENTER = 1;
    int RIGHT = 2;

    void fillRect(float x, float y, float w, float h, int argb);

    void fillRoundRect(float x, float y, float w, float h, float r, int argb);

    void strokeRoundRect(float x, float y, float w, float h, float r, float stroke, int argb);

    void fillCircle(float cx, float cy, float r, int argb);

    void line(float x1, float y1, float x2, float y2, float stroke, int argb);

    void text(String s, float x, float cy, float size, boolean bold, int argb, int align);

    float textWidth(String s, float size, boolean bold);

    void save();

    void restore();

    void clipRect(float x, float y, float w, float h);

    void translate(float dx, float dy);

    void scale(float s);
}
