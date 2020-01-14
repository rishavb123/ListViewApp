package io.github.rishavb123.posttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        ((TextView) findViewById(R.id.textView)).setText(getIntent().getStringExtra(MainActivity.KEY));

    }
}
