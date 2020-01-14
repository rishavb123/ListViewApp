package io.github.rishavb123.filesavingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                array.put(((EditText) findViewById(R.id.editText)).getText());
                array.put(((EditText) findViewById(R.id.editText2)).getText());
                array.put(((EditText) findViewById(R.id.editText3)).getText());
                try {
                    FileHandler.write(new OutputStreamWriter(openFileOutput("info.json", MODE_PRIVATE)), array.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((TextView) findViewById(R.id.textView)).setText(FileHandler.read(new InputStreamReader(openFileInput("info.json"))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
