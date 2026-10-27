package host.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import quiniela.QuinielaScreen;
import ui.Ui;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(new UiView(this));
    }

    static class UiView extends View {
        QuinielaScreen screen = new QuinielaScreen();
        Ui ui = new Ui();
        RenderNode content = new RenderNode("content");
        AndroidCanvas wrapper = new AndroidCanvas(null);
        float density;
        boolean dirty = true;

        UiView(Context ctx) {
            super(ctx);
            density = ctx.getResources().getDisplayMetrics().density;
            setOnApplyWindowInsetsListener((v, insets) -> {
                ui.insetTop = insets.getSystemWindowInsetTop() / density;
                ui.insetBottom = insets.getSystemWindowInsetBottom() / density;
                dirty = true;
                invalidate();
                return insets;
            });
        }

        @Override
        protected void onSizeChanged(int w, int h, int ow, int oh) {
            dirty = true;
        }

        void record(float w, float h) {
            content.setPosition(0, 0, (int) (w * density) + 1, (int) (6000 * density));
            Canvas rc = content.beginRecording();
            rc.scale(density, density);
            wrapper.c = rc;
            ui.c = wrapper;
            screen.content(ui);
            content.endRecording();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float w = getWidth() / density;
            float h = getHeight() / density;
            ui.frame(w, h);
            float barBottom = ui.insetTop + 56;
            ui.clipTop = barBottom;
            if (dirty || ui.hasPendingClick()) {
                boolean click = ui.hasPendingClick();
                record(w, h);
                if (click && !ui.hasPendingClick()) record(w, h);
                dirty = false;
            }
            canvas.save();
            canvas.scale(density, density);
            wrapper.c = canvas;
            ui.c = wrapper;
            screen.background(ui);
            canvas.clipRect(0, barBottom, w, h);
            canvas.translate(0, -ui.scrollY);
            canvas.scale(1 / density, 1 / density);
            canvas.drawRenderNode(content);
            canvas.restore();
            canvas.save();
            canvas.scale(density, density);
            screen.bar(ui);
            canvas.restore();
            ui.endClick();
            if (ui.animating()) postInvalidateOnAnimation();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX() / density;
            float y = e.getY() / density;
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    ui.pointerDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    ui.pointerMove(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    ui.pointerUp(x, y);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    ui.pointerUp(-1000, -1000);
                    break;
                default:
                    break;
            }
            invalidate();
            return true;
        }
    }
}
