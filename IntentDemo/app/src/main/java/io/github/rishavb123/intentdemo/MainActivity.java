package io.github.rishavb123.intentdemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView number;
    Button launch;

    public static final int NUMBER_CODE = 1234;
    public static final String TAG_INTENT_CODE = "number code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = findViewById(R.id.id_number);
        launch = findViewById(R.id.id_launch);

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLoadActivity = new Intent(getApplicationContext(), NumberActivity.class);
                intentToLoadActivity.putExtra("TEST", "TEST VALUE");
                //startActivity(intentToLoadActivity);
                startActivityForResult(intentToLoadActivity, NUMBER_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NUMBER_CODE && resultCode == RESULT_OK)
            number.setText(data.getStringExtra(TAG_INTENT_CODE));
    }
}
