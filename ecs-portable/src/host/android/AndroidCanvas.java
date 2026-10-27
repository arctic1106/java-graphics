package host.android;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import ui.Canvas;

public class AndroidCanvas implements Canvas {
    android.graphics.Canvas c;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF rect = new RectF();

    public AndroidCanvas(android.graphics.Canvas c) {
        this.c = c;
    }

    void fill(int argb) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(argb);
    }

    public void fillRect(float x, float y, float w, float h, int argb) {
        fill(argb);
        c.drawRect(x, y, x + w, y + h, paint);
    }

    public void fillRoundRect(float x, float y, float w, float h, float r, int argb) {
        fill(argb);
        rect.set(x, y, x + w, y + h);
        c.drawRoundRect(rect, r, r, paint);
    }

    public void strokeRoundRect(float x, float y, float w, float h, float r, float stroke, int argb) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setColor(argb);
        rect.set(x, y, x + w, y + h);
        c.drawRoundRect(rect, r, r, paint);
    }

    public void fillCircle(float cx, float cy, float r, int argb) {
        fill(argb);
        c.drawCircle(cx, cy, r, paint);
    }

    public void line(float x1, float y1, float x2, float y2, float stroke, int argb) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(argb);
        c.drawLine(x1, y1, x2, y2, paint);
    }

    void font(float size, boolean bold) {
        paint.setTextSize(size);
        paint.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    public void text(String s, float x, float cy, float size, boolean bold, int argb, int align) {
        font(size, bold);
        fill(argb);
        float w = paint.measureText(s);
        float left = align == CENTER ? x - w / 2 : align == RIGHT ? x - w : x;
        Paint.FontMetrics fm = paint.getFontMetrics();
        c.drawText(s, left, cy - (fm.ascent + fm.descent) / 2, paint);
    }

    public float textWidth(String s, float size, boolean bold) {
        font(size, bold);
        return paint.measureText(s);
    }

    public void save() {
        c.save();
    }

    public void restore() {
        c.restore();
    }

    public void clipRect(float x, float y, float w, float h) {
        c.clipRect(x, y, x + w, y + h);
    }

    public void translate(float dx, float dy) {
        c.translate(dx, dy);
    }

    public void scale(float s) {
        c.scale(s, s);
    }
}
