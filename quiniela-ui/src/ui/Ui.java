package ui;

public class Ui {
    public Canvas c;
    public float w;
    public float h;
    public float insetTop;
    public float insetBottom;
    public float scrollY;
    public float contentH;
    public float clipTop;

    float downY;
    float lastY;
    boolean down;
    boolean dragging;
    boolean clickPending;
    float clickX;
    float clickY;
    float slop = 8;
    float velocity;
    long lastMoveNanos;
    long lastFrameNanos;
    boolean flinging;
    float flingVel;

    public void begin(Canvas canvas, float width, float height) {
        c = canvas;
        frame(width, height);
    }

    public void frame(float width, float height) {
        w = width;
        h = height;
        advanceFling();
        clampScroll();
    }

    public boolean hasPendingClick() {
        return clickPending;
    }

    public void endClick() {
        clickPending = false;
    }

    public boolean animating() {
        return flinging;
    }

    void advanceFling() {
        long now = System.nanoTime();
        if (flinging) {
            float dt = Math.min((now - lastFrameNanos) / 1e9f, 0.05f);
            float before = scrollY;
            scrollY += flingVel * dt;
            flingVel *= (float) Math.exp(-3.2 * dt);
            clampScroll();
            if (Math.abs(flingVel) < 12 || scrollY == before) flinging = false;
        }
        lastFrameNanos = now;
    }

    public void end() {
        clickPending = false;
        clampScroll();
    }

    public void pointerDown(float x, float y) {
        down = true;
        dragging = false;
        flinging = false;
        velocity = 0;
        downY = y;
        lastY = y;
        lastMoveNanos = System.nanoTime();
    }

    public void pointerMove(float x, float y) {
        if (!down) return;
        if (!dragging && Math.abs(y - downY) > slop) dragging = true;
        long now = System.nanoTime();
        if (dragging) {
            scrollY -= y - lastY;
            clampScroll();
            float dt = (now - lastMoveNanos) / 1e9f;
            if (dt > 0.0005f) velocity = 0.7f * ((y - lastY) / dt) + 0.3f * velocity;
        }
        lastMoveNanos = now;
        lastY = y;
    }

    public void pointerUp(float x, float y) {
        if (down && !dragging) {
            clickPending = true;
            clickX = x;
            clickY = y;
        }
        if (down && dragging && (System.nanoTime() - lastMoveNanos) < 80_000_000L && Math.abs(velocity) > 80) {
            flinging = true;
            flingVel = -velocity;
            lastFrameNanos = System.nanoTime();
        }
        down = false;
        dragging = false;
    }

    public void wheel(float dy) {
        scrollY += dy;
        clampScroll();
    }

    void clampScroll() {
        float max = Math.max(0, contentH - h);
        if (scrollY > max) scrollY = max;
        if (scrollY < 0) scrollY = 0;
    }

    public boolean clicked(float x, float y, float bw, float bh) {
        if (!clickPending || clickY < clipTop) return false;
        return hit(x, y - scrollY, bw, bh);
    }

    public boolean clickedFixed(float x, float y, float bw, float bh) {
        return clickPending && hit(x, y, bw, bh);
    }

    boolean hit(float x, float y, float bw, float bh) {
        if (clickX < x || clickX > x + bw || clickY < y || clickY > y + bh) return false;
        clickPending = false;
        return true;
    }
}
