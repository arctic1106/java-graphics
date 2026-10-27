package host.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import game.Game;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(new GameView(this));
    }

    static class GameView extends View {
        Game game = new Game();
        AndroidCanvas wrapper = new AndroidCanvas(null);
        float density;

        GameView(Context ctx) {
            super(ctx);
            density = ctx.getResources().getDisplayMetrics().density;
            setOnApplyWindowInsetsListener((v, insets) -> {
                game.insetTop = insets.getSystemWindowInsetTop() / density;
                return insets;
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            game.frame(System.nanoTime());
            canvas.scale(density, density);
            wrapper.c = canvas;
            game.render(wrapper, getWidth() / density, getHeight() / density);
            postInvalidateOnAnimation();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX() / density;
            float y = e.getY() / density;
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    game.pointerDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    game.pointerMove(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    game.pointerUp(x, y);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
