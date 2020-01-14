package io.github.rishavb123.programmaticallyaddingwidgetsdemo;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = findViewById(R.id.constaintlayout);

        TextView textView1 = new TextView(this);
        textView1.setId(View.generateViewId());
        textView1.setText("Hello Again!");
        textView1.setTextSize(30);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textView1.setLayoutParams(layoutParams);

        constraintLayout.addView(textView1);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(textView1.getId(), ConstraintSet.TOP, R.id.textview, ConstraintSet.BOTTOM);
        constraintSet.connect(textView1.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(textView1.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(textView1.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(textView1.getId(), 0.25f);
        constraintSet.setHorizontalBias(textView1.getId(), 0.75f);
        constraintSet.applyTo(constraintLayout);

    }
}
