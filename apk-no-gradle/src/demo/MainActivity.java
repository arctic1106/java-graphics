package demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int count;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.rgb(24, 24, 32));

        TextView label = new TextView(this);
        label.setText("Java without Gradle");
        label.setTextSize(28);
        label.setTextColor(Color.WHITE);
        label.setGravity(Gravity.CENTER);

        Button button = new Button(this);
        button.setText("Clicked 0 times");
        button.setOnClickListener(v -> button.setText("Clicked " + (++count) + " times"));

        root.addView(label);
        root.addView(button);
        setContentView(root);
    }
}
