package io.bhagat.cscmember;

import android.icu.util.Output;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    EditText id;
    TextView name;
    TextView grade;
    TextView count;

    private static final String TAG = "TESTTEST";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.id_input);
        name = findViewById(R.id.name);
        grade = findViewById(R.id.grade);
        count = findViewById(R.id.count);

        try {
            Log.d(TAG, "onCreate: READING FILE . . .");
            JSONObject json = new JSONObject(FileHandler.read(new InputStreamReader(openFileInput("memberinfo.json"))));
            Log.d(TAG, "onCreate: " + json.toString());
            id.setText(json.getString("id"));
            name.setText(json.getString("name"));
            grade.setText(json.getString("grade"));
            count.setText(json.getString("count"));
            sendRequest(Integer.parseInt(json.getString("id")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() == 8)
                    sendRequest(Integer.parseInt(text));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void sendRequest(int id) {
        new GetMemberInfo().execute(id);
    }

    public void updateUI(JSONObject jsonObject) {
        try {
            name.setText("Name: " + jsonObject.getString("name"));
            grade.setText("Grade: " + jsonObject.getString("grade"));
            count.setText("Attendance Count: " + jsonObject.getInt("count"));
        } catch (JSONException e) {
            Toast.makeText(this, "Member Not Found", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            JSONObject json = new JSONObject();
            json.put("id", id.getText());
            json.put("name", name.getText());
            json.put("grade", grade.getText());
            json.put("count", count.getText());
            Log.d(TAG, "onDestroy: " + json.toString());
            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput("memberinfo.json", MODE_PRIVATE));
            writer.write(json.toString());
            writer.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public class GetMemberInfo extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... ids) {
            try {

                int id = ids[0];

                URL url = new URL("https://us-central1-sbhs-csc-membership.cloudfunctions.net/memberInfo?id=" + id);

                URLConnection urlConnection = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String temp;
                StringBuilder total = new StringBuilder(1024);

                while((temp =  reader.readLine()) != null)
                {
                    total.append(temp).append('\n');
                }

                reader.close();

                JSONObject jsonObject = new JSONObject(total.toString());
                return jsonObject;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                updateUI(jsonObject);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
}
