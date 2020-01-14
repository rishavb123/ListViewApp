package io.github.rishavb123.intentdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        Toast.makeText(this, getIntent().getStringExtra("TEST"), Toast.LENGTH_SHORT).show();

        findViewById(R.id.id_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendInfoBack = new Intent();
                sendInfoBack.putExtra(MainActivity.TAG_INTENT_CODE, ((EditText) findViewById(R.id.id_edit)).getText().toString());
                setResult(RESULT_OK, sendInfoBack);
                finish();
            }
        });

    }
}
