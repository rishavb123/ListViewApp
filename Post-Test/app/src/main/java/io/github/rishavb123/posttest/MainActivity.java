package io.github.rishavb123.posttest;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private RadioGroup radioGroup;
    private Button run;
    private Button launch;
    private ConstraintLayout layout;

    private int curCheckedId;

    public static final int[] COLORS = {Color.RED, Color.BLUE, Color.GREEN};
    public static final String KEY = "KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        radioGroup = findViewById(R.id.radioGroup);
        run = findViewById(R.id.runBtn);
        launch = findViewById(R.id.launchBtn);
        layout = findViewById(R.id.layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                curCheckedId = checkedId;
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(curCheckedId)
                {
                    case R.id.radioToast:
                        Toast.makeText(getApplicationContext(), "Toast Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioColor:
                        layout.setBackgroundColor(COLORS[(int)(Math.random()*3)]);
                        break;
                    case R.id.radioName:
                        name.setText(name.getText().toString().toUpperCase());
                        break;
                }
            }
        });

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondaryActivity.class);
                String extra = "The " + ((RadioButton) findViewById(curCheckedId)).getText() + " Radio Button was selected!";
                intent.putExtra(KEY, extra);
                startActivity(intent);
            }
        });

    }
}
