package host.desktop;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayDeque;
import ui.Canvas;

public class Java2DCanvas implements Canvas {
    Graphics2D g;
    ArrayDeque<AffineTransform> transforms = new ArrayDeque<>();
    ArrayDeque<Shape> clips = new ArrayDeque<>();

    public Java2DCanvas(Graphics2D g) {
        this.g = g;
    }

    Font font(float size, boolean bold) {
        return new Font(Font.SANS_SERIF, bold ? Font.BOLD : Font.PLAIN, 1).deriveFont(size);
    }

    void color(int argb) {
        g.setColor(new Color(argb, true));
    }

    public void fillRect(float x, float y, float w, float h, int argb) {
        color(argb);
        g.fill(new Rectangle2D.Float(x, y, w, h));
    }

    public void fillRoundRect(float x, float y, float w, float h, float r, int argb) {
        color(argb);
        g.fill(new RoundRectangle2D.Float(x, y, w, h, 2 * r, 2 * r));
    }

    public void strokeRoundRect(float x, float y, float w, float h, float r, float stroke, int argb) {
        color(argb);
        g.setStroke(new BasicStroke(stroke));
        g.draw(new RoundRectangle2D.Float(x, y, w, h, 2 * r, 2 * r));
    }

    public void fillCircle(float cx, float cy, float r, int argb) {
        color(argb);
        g.fill(new Ellipse2D.Float(cx - r, cy - r, 2 * r, 2 * r));
    }

    public void line(float x1, float y1, float x2, float y2, float stroke, int argb) {
        color(argb);
        g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Float(x1, y1, x2, y2));
    }

    public void text(String s, float x, float cy, float size, boolean bold, int argb, int align) {
        Font f = font(size, bold);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        float w = textWidth(s, size, bold);
        float left = align == CENTER ? x - w / 2 : align == RIGHT ? x - w : x;
        float baseline = cy + (fm.getAscent() - fm.getDescent()) / 2f;
        color(argb);
        g.drawString(s, left, baseline);
    }

    public float textWidth(String s, float size, boolean bold) {
        Font f = font(size, bold);
        return (float) f.getStringBounds(s, g.getFontRenderContext()).getWidth();
    }

    public void save() {
        transforms.push(g.getTransform());
        clips.push(g.getClip() == null ? new Rectangle2D.Float(-1e6f, -1e6f, 2e6f, 2e6f) : g.getClip());
    }

    public void restore() {
        g.setTransform(transforms.pop());
        g.setClip(clips.pop());
    }

    public void clipRect(float x, float y, float w, float h) {
        g.clip(new Rectangle2D.Float(x, y, w, h));
    }

    public void translate(float dx, float dy) {
        g.translate(dx, dy);
    }
}
